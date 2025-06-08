package me.hi0yoo.commerce.product.presentation.v3

import me.hi0yoo.commerce.product.application.dto.CategoryResult
import me.hi0yoo.commerce.product.application.dto.ProductPagedListByCursorQuery
import me.hi0yoo.commerce.product.application.dto.ProductPagedListResult
import me.hi0yoo.commerce.product.application.port.`in`.FetchProductPagedListByCursorUseCase
import me.hi0yoo.commerce.product.presentation.dto.CategoryApiResponse
import me.hi0yoo.commerce.product.presentation.dto.ProductSearchByCursorRequest
import me.hi0yoo.commerce.product.presentation.dto.ProductSearchByCursorResponse
import me.hi0yoo.commerce.product.presentation.dto.ProductSummaryResponse
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

// 무한 스크롤 API
@RestController
@RequestMapping("/api/v3/products")
class ProductListPageControllerV3(
    private val fetchProductPagedListByCursorUseCase: FetchProductPagedListByCursorUseCase
) {
    companion object {
        private val log = LoggerFactory.getLogger(ProductListPageControllerV3::class.java)
    }

    @GetMapping
    fun handleQueryProductListPageByCursor(@ModelAttribute request: ProductSearchByCursorRequest): ProductSearchByCursorResponse {
        val start = System.currentTimeMillis()
        val results = fetchProductPagedListByCursorUseCase.fetchPagedList(request.toQuery())

        val response = ProductSearchByCursorResponse(
            hasNext = results.hasNext,
            products = results.content.map { it.toResponse() },
        )

        log.info(
            "ProductController.handleQueryProductListPageByCursor completed in {}ms",
            System.currentTimeMillis() - start
        )

        return response
    }

    // DTO mapping
    internal fun ProductSearchByCursorRequest.toQuery(): ProductPagedListByCursorQuery {
        return ProductPagedListByCursorQuery(
            categoryId = categoryId,
            keyword = keyword,
            lastProductId = lastProductId,
            size = size
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