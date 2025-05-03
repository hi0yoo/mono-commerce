package me.hi0yoo.commerce.order.infrastructure.product

import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import me.hi0yoo.commerce.common.domain.exception.OutOfStockException
import me.hi0yoo.commerce.common.domain.id.ProductOptionId
import java.math.BigDecimal

@Entity
class ProductOption(
    product: Product,
    optionId: String,
    optionName: String,
    optionPrice: BigDecimal,
    realStockQuantity: Long = 0,
) {
    @EmbeddedId
    val id: ProductOptionId = ProductOptionId(
        productId = product.id,
        optionId = optionId,
    )

    var optionName: String = optionName
        protected set

    var optionPrice: BigDecimal = optionPrice
        protected set

    var realStockQuantity: Long = realStockQuantity
        protected set

    var reservedStockQuantity: Long = 0
        protected set

    fun increaseRealStockQuantity(quantity: Long) {
        realStockQuantity += quantity
    }

    fun increaseReservedStockQuantity(quantity: Long) {
        val availableStockQuantity = getAvailableStockQuantity()
        if (availableStockQuantity < quantity) {
            throw OutOfStockException(id, availableStockQuantity, quantity)
        }

        reservedStockQuantity += quantity
    }

    fun getAvailableStockQuantity(): Long {
        return realStockQuantity - reservedStockQuantity
    }
}