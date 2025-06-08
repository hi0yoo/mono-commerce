package me.hi0yoo.commerce.product.infrastructure.adapter.out

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.CaseBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
import me.hi0yoo.commerce.product.application.dto.CategoryResult
import me.hi0yoo.commerce.product.application.dto.ProductPageByCursorResult
import me.hi0yoo.commerce.product.application.dto.ProductPagedListByCursorQuery
import me.hi0yoo.commerce.product.application.dto.ProductPagedListResult
import me.hi0yoo.commerce.product.application.port.out.ProductPageByCursorQueryPort
import me.hi0yoo.commerce.product.domain.QProduct
import me.hi0yoo.commerce.product.infrastructure.vendor.QVendor
import org.springframework.stereotype.Repository

@Repository
class QueryDslProductPageByCursorQueryAdapter(
    private val jpaQueryFactory: JPAQueryFactory,
): ProductPageByCursorQueryPort {
    override fun findPagedListByCursor(query: ProductPagedListByCursorQuery): ProductPageByCursorResult {
        val qProduct = QProduct.product
        val qVendor = QVendor.vendor
        val qCategory = qProduct.category

        val whereConditions = mutableListOf<BooleanBuilder>()

        // 상품 커서
        query.lastProductId?.let { lastProductId ->
            val cursorProductIdCondition = BooleanBuilder()
                .and(qProduct.id.lt(lastProductId))
            whereConditions.add(cursorProductIdCondition)
        }

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

        val baseResult = jpaQueryFactory
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
            .limit((query.size + 1).toLong())
            .fetch()

        val hasNext = baseResult.size > query.size
        val baseProducts = baseResult.take(query.size)

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

        return ProductPageByCursorResult(
            hasNext = hasNext,
            content = results
        )
    }
}