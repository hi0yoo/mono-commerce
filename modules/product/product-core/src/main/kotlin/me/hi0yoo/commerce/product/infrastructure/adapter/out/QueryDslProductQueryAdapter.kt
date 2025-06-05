package me.hi0yoo.commerce.product.infrastructure.adapter.out

import com.querydsl.jpa.impl.JPAQueryFactory
import me.hi0yoo.commerce.product.application.dto.ProductDetailResult
import me.hi0yoo.commerce.product.application.dto.ProductPagedListQuery
import me.hi0yoo.commerce.product.application.dto.ProductPagedListResult
import me.hi0yoo.commerce.product.application.port.out.ProductQueryPort
import org.springframework.stereotype.Repository

@Repository
class QueryDslProductQueryAdapter(
    private val jpaQueryFactory: JPAQueryFactory
): ProductQueryPort {

    override fun findPagedListByCondition(query: ProductPagedListQuery): List<ProductPagedListResult> {
        TODO("Not yet implemented")
    }

    override fun findDetailById(productId: Long): ProductDetailResult? {
        TODO("Not yet implemented")
    }
}