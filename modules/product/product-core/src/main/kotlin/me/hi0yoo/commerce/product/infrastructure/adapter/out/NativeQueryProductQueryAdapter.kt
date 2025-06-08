package me.hi0yoo.commerce.product.infrastructure.adapter.out

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import me.hi0yoo.commerce.product.application.dto.CategoryResult
import me.hi0yoo.commerce.product.application.dto.ProductDetailResult
import me.hi0yoo.commerce.product.application.dto.ProductPageResult
import me.hi0yoo.commerce.product.application.dto.ProductPagedListQuery
import me.hi0yoo.commerce.product.application.dto.ProductPagedListResult
import me.hi0yoo.commerce.product.application.port.out.ProductQueryPort
import org.hibernate.query.TypedParameterValue
import org.hibernate.type.StandardBasicTypes
import org.springframework.stereotype.Repository

@Repository("nativeQueryProductQueryAdapter")
class NativeQueryProductQueryAdapter(
    @PersistenceContext
    private val em: EntityManager,
    private val queryDslProductQueryAdapter: QueryDslProductQueryAdapter,
): ProductQueryPort {

    override fun findPagedListByCondition(query: ProductPagedListQuery): ProductPageResult {
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

        val sql = """
            SELECT
                p1_0.product_id AS productId,
                p1_0.product_name AS name,
                p1_0.thumbnail_url AS thumbnailUrl,
                p1_0.price AS price,
                p1_0.vendor_id AS vendorId,
                v1_0.name AS vendorName,
                CASE
                    WHEN c1_0.parent_id IS NOT NULL THEN c1_0.parent_id
                    ELSE p1_0.category_id
                END AS mainCategoryId,
                CASE
                    WHEN c1_0.parent_id IS NOT NULL THEN p2_0.name
                    ELSE c1_0.name
                END AS mainCategoryName,
                p1_0.category_id AS subCategoryId,
                c1_0.name AS subCategoryName
            FROM (
                SELECT p.product_id
                FROM product p
                JOIN category c ON p.category_id = c.category_id
                WHERE (:categoryId IS NULL OR c.category_id = :categoryId OR c.parent_id = :categoryId)
                  AND (:keyword IS NULL OR p.product_name ILIKE CONCAT('%', :keyword, '%'))
                ORDER BY p.product_id DESC
                OFFSET :offset ROWS FETCH FIRST :size ROWS ONLY
            ) p
            LEFT JOIN product p1_0 ON p.product_id = p1_0.product_id
            LEFT JOIN vendor v1_0 ON p1_0.vendor_id = v1_0.id
            JOIN category c1_0 ON c1_0.category_id = p1_0.category_id
            JOIN category p2_0 ON p2_0.category_id = c1_0.parent_id
        """.trimIndent()

        val baseProducts = (em.createNativeQuery(sql)
            .setParameter("categoryId",  TypedParameterValue(StandardBasicTypes.LONG, query.categoryId))
            .setParameter("keyword",  TypedParameterValue(StandardBasicTypes.STRING, query.keyword))
            .setParameter("size", query.size)
            .setParameter("offset", (query.page * query.size).toLong())
            .resultList as List<Array<Any?>>)
            .map { cols ->
                ProductPagedListBaseDto(
                    productId = (cols[0] as Number).toLong(),
                    name = cols[1] as String,
                    thumbnailUrl = cols[2] as String,
                    price = (cols[3] as Number).toInt(),
                    vendorId = cols[4] as String,
                    vendorName = cols[5] as String,
                    mainCategoryId = (cols[6] as Number).toLong(),
                    mainCategoryName = cols[7] as String,
                    subCategoryId = (cols[8] as? Number)?.toLong(),
                    subCategoryName = cols[9] as? String
                )
            }

        // 상품 Badges 조회
        val productIds = baseProducts?.map { it.productId } ?: emptyList()
        val badgesByProductId = (em.createNativeQuery("""
                SELECT product_id, badge
                FROM product_badge
                WHERE product_id IN (:ids)
            """.trimIndent())
            .setParameter("ids", productIds)
            .resultList  as List<Array<Any?>>)
            .map { cols ->
                Pair(cols[0] as Long, cols[1] as  String)
            }
            .groupBy({it.first }, {it.second})

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
        val totalElements = (em.createNativeQuery("""
            SELECT count(p.product_id)
            FROM product p
            JOIN category c ON p.category_id = c.category_id
            WHERE (:categoryId IS NULL OR c.category_id = :categoryId OR c.parent_id = :categoryId)
              AND (:keyword IS NULL OR p.product_name ILIKE CONCAT('%', :keyword, '%'))
        """.trimIndent())
            .setParameter("categoryId",  TypedParameterValue(StandardBasicTypes.LONG, query.categoryId))
            .setParameter("keyword",  TypedParameterValue(StandardBasicTypes.STRING, query.keyword))
            .singleResult as Number).toLong()

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

    // 기존 구현체에 위임
    override fun findDetailById(productId: Long): ProductDetailResult? {
        return queryDslProductQueryAdapter.findDetailById(productId)
    }
}