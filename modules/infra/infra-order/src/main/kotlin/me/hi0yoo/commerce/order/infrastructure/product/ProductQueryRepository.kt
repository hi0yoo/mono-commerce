package me.hi0yoo.commerce.order.infrastructure.product

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import me.hi0yoo.commerce.common.domain.id.ProductOptionId
import me.hi0yoo.commerce.order.application.port.ProductDetailResponse
import me.hi0yoo.commerce.order.application.port.ProductDetailRequest
import org.springframework.stereotype.Repository

@Repository
class ProductQueryRepository(
    private val jpaQueryFactory: JPAQueryFactory
) {
    fun getProductDetails(requests: List<ProductDetailRequest>): List<ProductDetailResponse> {
        val qProduct = QProduct.product
        val qProductOption = QProductOption.productOption

        val ids: List<ProductOptionId> = requests.map {
            ProductOptionId(it.vendorId, it.productId, it.optionId)
        }

        return jpaQueryFactory
            .select(
                Projections.constructor(
                    ProductDetailResponse::class.java,
                    qProductOption.id.vendorId,
                    qProductOption.id.productId,
                    qProductOption.id.optionId,
                    qProduct.productName,
                    qProductOption.optionName,
                    qProductOption.optionPrice,
                )
            )
            .from(qProductOption)
            .join(qProduct).on(
                qProduct.id.vendorId.eq(qProductOption.id.vendorId)
                    .and(qProduct.id.productId.eq(qProductOption.id.productId))
            )
            .where(qProductOption.id.`in`(ids))
            .fetch()
    }
}