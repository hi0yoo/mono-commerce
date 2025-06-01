package me.hi0yoo.commerce.order.infrastructure.product

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import me.hi0yoo.commerce.order.application.port.ProductDetailResponse
import org.springframework.stereotype.Repository

@Repository
class ProductQueryRepository(
    private val jpaQueryFactory: JPAQueryFactory
) {
    fun getProductDetails(requests: List<Long>): List<ProductDetailResponse> {
        val qProduct = QProduct.product
        val qProductOption = QProductOption.productOption

        return jpaQueryFactory
            .select(
                Projections.constructor(
                    ProductDetailResponse::class.java,
                    qProductOption.id,
                    qProduct.productName,
                    qProductOption.optionName,
                    qProductOption.optionPrice,
                )
            )
            .from(qProductOption)
            .join(qProduct).on(
                qProduct.id.eq(qProductOption.productId)
                    .and(qProduct.id.eq(qProductOption.productId))
            )
            .where(qProductOption.id.`in`(requests))
            .fetch()
    }
}