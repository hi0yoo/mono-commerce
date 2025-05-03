package me.hi0yoo.commerce.order.infrastructure.adapter

import jakarta.persistence.EntityNotFoundException
import jakarta.transaction.Transactional
import me.hi0yoo.commerce.common.domain.id.ProductOptionId
import me.hi0yoo.commerce.order.application.port.ProductInventoryPort
import me.hi0yoo.commerce.order.application.port.ReserveStockRequest
import me.hi0yoo.commerce.order.infrastructure.product.ProductOptionJpaRepository
import org.springframework.stereotype.Component

@Component
class ProductInventoryAdapter(
    private val productOptionJpaRepository: ProductOptionJpaRepository
): ProductInventoryPort {
    @Transactional
    override fun reserveStocks(requests: List<ReserveStockRequest>) {
        val optionIds = requests.map { ProductOptionId(it.vendorId, it.productId, it.optionId) }
        val optionsMap = productOptionJpaRepository.findAllByIdForUpdate(optionIds).associateBy { it.id }

        for (request in requests) {
            optionsMap[ProductOptionId(request.vendorId, request.productId, request.optionId)]
                ?.increaseReservedStockQuantity(request.quantity)
                ?: throw EntityNotFoundException()
        }
    }
}