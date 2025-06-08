package me.hi0yoo.commerce.product.presentation.v2

import me.hi0yoo.commerce.product.application.dto.CategoryResult
import me.hi0yoo.commerce.product.application.dto.ProductPagedListQuery
import me.hi0yoo.commerce.product.application.dto.ProductPagedListResult
import me.hi0yoo.commerce.product.application.port.`in`.FetchProductPagedListUseCase
import me.hi0yoo.commerce.product.presentation.dto.CategoryApiResponse
import me.hi0yoo.commerce.product.presentation.dto.ProductPagedListApiResponse
import me.hi0yoo.commerce.product.presentation.dto.ProductSearchRequest
import me.hi0yoo.commerce.product.presentation.dto.ProductSummaryResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

// 네이티브 쿼리 사용 API
@RestController
@RequestMapping("/api/v2/products")
class ProductListPageControllerV2(
    @Qualifier("productQueryServiceV2")
    private val fetchProductPagedListUseCase: FetchProductPagedListUseCase
) {
    companion object {
        private val log = LoggerFactory.getLogger(ProductListPageControllerV2::class.java)
    }

    @GetMapping
    fun handleQueryProductListPage(@ModelAttribute request: ProductSearchRequest): ProductPagedListApiResponse {
        val start = System.currentTimeMillis()
        val results = fetchProductPagedListUseCase.fetchPagedList(request.toQuery())

        val response = ProductPagedListApiResponse(
            page = results.page,
            size = results.size,
            totalPages = results.totalPages,
            totalElements = results.totalElements,
            hasNext = results.hasNext,
            hasPrevious = results.hasPrevious,
            products = results.content.map { it.toResponse() },
        )

        log.info(
            "ProductController.handleQueryProductListPage completed in {}ms",
            System.currentTimeMillis() - start
        )

        return response
    }

    // DTO mapping
    internal fun ProductSearchRequest.toQuery(): ProductPagedListQuery {
        return ProductPagedListQuery(
            categoryId = categoryId,
            keyword = keyword,
            page = page,
            size = size,
        )
    }

    internal fun ProductPagedListResult.toResponse(): ProductSummaryResponse {
        return ProductSummaryResponse(
            productId = productId.toString(),
            name = name,
            thumbnailUrl = thumbnailUrl,
            price = price,
            vendorId = vendorId,
            vendorName = vendorName,
            category = category.toResponse(),
            badges = badges,
        )
    }

    internal fun CategoryResult.toResponse(): CategoryApiResponse {
        return CategoryApiResponse(
            mainId = mainId,
            mainName = mainName,
            subId = subId,
            subName = subName,
        )
    }
}