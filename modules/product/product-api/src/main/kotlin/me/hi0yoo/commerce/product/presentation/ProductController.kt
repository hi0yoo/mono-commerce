package me.hi0yoo.commerce.product.presentation

import me.hi0yoo.commerce.product.application.dto.CategoryResult
import me.hi0yoo.commerce.product.application.dto.ProductOptionResult
import me.hi0yoo.commerce.product.application.dto.ProductDetailResult
import me.hi0yoo.commerce.product.application.dto.ProductDetailQuery
import me.hi0yoo.commerce.product.application.dto.ProductPagedListQuery
import me.hi0yoo.commerce.product.application.dto.ProductPagedListResult
import me.hi0yoo.commerce.product.application.port.`in`.FetchProductDetailUseCase
import me.hi0yoo.commerce.product.application.port.`in`.FetchProductPagedListUseCase
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/products")
class ProductController(
    private val fetchProductPagedListUseCase: FetchProductPagedListUseCase,
    private val fetchProductDetailUseCase: FetchProductDetailUseCase,
) {
    companion object {
        private val log = LoggerFactory.getLogger(ProductController::class.java)
    }

    @GetMapping
    fun handleQueryProductListPage(@ModelAttribute request: ProductSearchRequest): ProductPagedListApiResponse {
        val start = System.currentTimeMillis()
        val productSummaryResponses = fetchProductPagedListUseCase.fetchPagedList(request.toQuery()).map { it.toResponse() }

        val response = ProductPagedListApiResponse(
            page = request.page,
            size = request.size,
            total = productSummaryResponses.size,
            products = productSummaryResponses,
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
            productId = productId,
            name = name,
            thumbnailUrl = thumbnailUrl,
            price = price,
            vendorId = vendorId,
            vendorName = vendorName,
            category = category.toResponse(),
            badges = badges,
        )
    }

    @GetMapping("/{productId}")
    fun handleFetchProductDetail(@PathVariable productId: Long): FetchProductDetailApiResponse {
        val start = System.currentTimeMillis()
        val toResponse = fetchProductDetailUseCase.fetchDetail(
            ProductDetailQuery(
                productId = productId,
            )
        ).toResponse()

        log.info(
            "ProductController.handleFetchProductDetail completed in {}ms",
            System.currentTimeMillis() - start
        )
        return toResponse
    }

    // DTO Mapping
    internal fun ProductDetailResult.toResponse(): FetchProductDetailApiResponse {
        return FetchProductDetailApiResponse(
            productId = productId,
            name = name,
            thumbnailUrl = thumbnailUrl,
            description = description,
            imageUrls = imageUrls,
            price = price,
            vendorId = vendorId,
            vendorName = vendorName,
            category = category.toResponse(),
            badges = badges,
            options = options.map { it.toResponse() },
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

    internal fun ProductOptionResult.toResponse(): ProductOptionResponse {
        return ProductOptionResponse(
            optionId = optionId,
            name = name,
            additionalPrice = additionalPrice,
            availableStock = if (availableStock > 5) null else availableStock,
        )
    }
}