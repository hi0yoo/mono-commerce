package me.hi0yoo.commerce.product.infrastructure.adapter.out

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.CaseBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
import me.hi0yoo.commerce.product.application.dto.CategoryResult
import me.hi0yoo.commerce.product.application.dto.ProductDetailResult
import me.hi0yoo.commerce.product.application.dto.ProductOptionResult
import me.hi0yoo.commerce.product.application.dto.ProductPageResult
import me.hi0yoo.commerce.product.application.dto.ProductPagedListQuery
import me.hi0yoo.commerce.product.application.dto.ProductPagedListResult
import me.hi0yoo.commerce.product.application.port.out.ProductQueryPort
import me.hi0yoo.commerce.product.domain.QProduct
import me.hi0yoo.commerce.product.domain.QProductOption
import me.hi0yoo.commerce.product.infrastructure.vendor.QVendor
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Repository

@Primary
@Repository
class QueryDslProductQueryAdapter(
    private val jpaQueryFactory: JPAQueryFactory,
): ProductQueryPort {

    override fun findPagedListByCondition(query: ProductPagedListQuery): ProductPageResult {
        val qProduct = QProduct.product
        val qVendor = QVendor.vendor
        val qCategory = qProduct.category

        val whereConditions = mutableListOf<BooleanBuilder>()

        // 카테고리 검색
        query.categoryId?.let { categoryId ->
            val categoryCondition = BooleanBuilder()
                .or(qCategory.id.eq(categoryId))
                .or(qCategory.parent.id.eq(categoryId))
            whereConditions.add(categoryCondition)
        }

        // 키워드 검색 (상품명)
        query.keyword?.takeIf { it.isNotBlank() }?.let { keyword ->
            val keywordCondition = BooleanBuilder()
                .and(qProduct.name.containsIgnoreCase(keyword))
            whereConditions.add(keywordCondition)
        }

        // 상품 페이지 조회
        data class ProductPagedListBaseDto(
            val productId: Long,
            val name: String,
            val thumbnailUrl: String,
            val price: Int,
            val vendorId: String,
            val vendorName: String,
            val mainCategoryId: Long,
            val mainCategoryName: String,
            val subCategoryId: Long? = null,
            val subCategoryName: String? = null,
        )

        val baseProducts = jpaQueryFactory
            .select(
                Projections.constructor(
                    ProductPagedListBaseDto::class.java,
                    qProduct.id,
                    qProduct.name,
                    qProduct.thumbnailUrl,
                    qProduct.price,
                    qProduct.vendorId,
                    qVendor.name,
                    CaseBuilder()
                        .`when`(qProduct.category.parent.isNotNull)
                        .then(qProduct.category.parent.id)
                        .otherwise(qProduct.category.id),
                    CaseBuilder()
                        .`when`(qProduct.category.parent.isNotNull)
                        .then(qProduct.category.parent.name)
                        .otherwise(qProduct.category.name),
                    qProduct.category.id,
                    qProduct.category.name,
                )
            )
            .from(qProduct)
            .leftJoin(qVendor).on(qProduct.vendorId.eq(qVendor.id))
            .where(*whereConditions.toTypedArray())
            .orderBy(qProduct.id.desc())
            .offset((query.page * query.size).toLong())
            .limit(query.size.toLong())
            .fetch()

        // 상품 Badges 조회
        val productIds = baseProducts?.map { it.productId } ?: emptyList()

        val badgesByProductId = jpaQueryFactory
            .select(qProduct.id, qProduct.badges)
            .from(qProduct)
            .where(qProduct.id.`in`(productIds))
            .fetch()
            .groupBy({ it.get(qProduct.id)!! }, { it.get(qProduct.badges.any())?.name!! })

        // DTO 조립
        val results = baseProducts.map {
            ProductPagedListResult(
                productId = it.productId,
                name = it.name,
                thumbnailUrl = it.thumbnailUrl,
                price = it.price,
                vendorId = it.vendorId,
                vendorName = it.vendorName,
                category = CategoryResult(
                    mainId = it.mainCategoryId,
                    mainName = it.mainCategoryName,
                    subId = it.subCategoryId,
                    subName = it.subCategoryName,
                ),
                badges = badgesByProductId[it.productId] ?: emptyList(),
            )
        }

        // 전체 개수 count
        val totalElements = jpaQueryFactory
            .select(qProduct.count())
            .from(qProduct)
            .where(*whereConditions.toTypedArray())
            .fetchOne() ?: 0L
        // 페이징 계산
        val totalPages = if (totalElements == 0L) 1 else ((totalElements + query.size - 1) / query.size).toInt()
        val hasPrevious = query.page > 0
        val hasNext = (query.page + 1) * query.size < totalElements

        return ProductPageResult(
            page = query.page,
            size = query.size,
            totalPages = totalPages,
            totalElements = totalElements,
            hasNext = hasPrevious,
            hasPrevious = hasNext,
            content = results,
        )
    }

    override fun findDetailById(productId: Long): ProductDetailResult? {
        val qProduct = QProduct.product
        val qVendor = QVendor.vendor

        // 상품 조회
        data class ProductDetailBaseDto(
            val productId: Long,
            val name: String,
            val thumbnailUrl: String,
            val description: String?,
            val price: Int,
            val vendorId: String,
            val vendorName: String,
            val mainCategoryId: Long,
            val mainCategoryName: String,
            val subCategoryId: Long? = null,
            val subCategoryName: String? = null,
        )

        val baseProduct = jpaQueryFactory
            .select(
                Projections.constructor(
                    ProductDetailBaseDto::class.java, // 중간 Dto로 임시 사용
                    qProduct.id,
                    qProduct.name,
                    qProduct.thumbnailUrl,
                    qProduct.description,
                    qProduct.price,
                    qProduct.vendorId,
                    qVendor.name,
                    CaseBuilder()
                        .`when`(qProduct.category.parent.isNotNull)
                        .then(qProduct.category.parent.id)
                        .otherwise(qProduct.category.id),
                    CaseBuilder()
                        .`when`(qProduct.category.parent.isNotNull)
                        .then(qProduct.category.parent.name)
                        .otherwise(qProduct.category.name),
                    qProduct.category.id,
                    qProduct.category.name
                )
            )
            .from(qProduct)
            .leftJoin(qVendor).on(qProduct.vendorId.eq(qVendor.id))
            .where(qProduct.id.eq(productId))
            .fetchOne()
            ?: return null

        // 상품 Badges 조회
        val badges = jpaQueryFactory
            .select(qProduct.badges)
            .from(qProduct)
            .where(qProduct.id.eq(productId))
            .fetchOne()

        // 상품 상세 이미지 조회
        val imageUrls = jpaQueryFactory
            .select(qProduct.imageUrls)
            .from(qProduct)
            .where(qProduct.id.eq(productId))
            .fetch()
            .filterIsInstance<String>()

        // 상품 옵션 조회
        val qProductOption = QProductOption.productOption
        val optionResults = jpaQueryFactory
            .select(
                Projections.constructor(
                    ProductOptionResult::class.java,
                    qProductOption.id,
                    qProductOption.name,
                    qProductOption.additionalPrice,
                    qProductOption.realStockQuantity.subtract(qProductOption.reservedStockQuantity)
                )
            )
            .from(qProductOption)
            .where(qProductOption.product.id.eq(productId))
            .fetch()

        // DTO 조립
        val result = ProductDetailResult(
            productId = baseProduct.productId,
            name = baseProduct.name,
            thumbnailUrl = baseProduct.thumbnailUrl,
            description = baseProduct.description,
            imageUrls = imageUrls,
            price = baseProduct.price,
            vendorId = baseProduct.vendorId,
            vendorName = baseProduct.vendorName,
            category = CategoryResult(
                mainId = baseProduct.mainCategoryId,
                mainName = baseProduct.mainCategoryName,
                subId = baseProduct.subCategoryId,
                subName = baseProduct.subCategoryName,
            ),
            badges = badges?.map { it.name } ?: emptyList(),
            options = optionResults,
        )

        return result
    }
}