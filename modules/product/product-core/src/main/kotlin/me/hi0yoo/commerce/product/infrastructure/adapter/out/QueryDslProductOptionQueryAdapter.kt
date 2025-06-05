package me.hi0yoo.commerce.product.infrastructure.adapter.out

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import me.hi0yoo.commerce.product.application.dto.ProductOptionSnapshotResult
import me.hi0yoo.commerce.product.application.port.out.ProductOptionQueryPort
import me.hi0yoo.commerce.product.domain.QProduct
import me.hi0yoo.commerce.product.domain.QProductOption
import org.springframework.stereotype.Repository

@Repository
class QueryDslProductOptionQueryAdapter(
    private val jpaQueryFactory: JPAQueryFactory
): ProductOptionQueryPort {

    override fun findAllSnapshotByIds(ids: List<Long>): List<ProductOptionSnapshotResult> {
        val qProduct = QProduct.product
        val qProductOption = QProductOption.productOption

        return jpaQueryFactory
            .select(
                Projections.constructor(
                    ProductOptionSnapshotResult::class.java,
                    qProductOption.product.id,
                    qProductOption.product.name,
                    qProductOption.product.price,
                    qProductOption.id,
                    qProductOption.name,
                    qProductOption.additionalPrice,

                    qProductOption.product.vendorId,
                )
            )
            .from(qProductOption)
            .join(qProduct).on(
                qProduct.eq(qProductOption.product)
            )
            .where(qProductOption.id.`in`(ids))
            .fetch()
    }
}