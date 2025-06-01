package me.hi0yoo.commerce.order.infrastructure.product

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import me.hi0yoo.commerce.common.domain.exception.OutOfStockException
import java.math.BigDecimal

@Entity
class ProductOption(
    id: Long,
    productId: Long,
    optionName: String,
    optionPrice: BigDecimal,
    realStockQuantity: Long = 0,
) {
    @Id
    @Column(name = "product_option_id")
    val id: Long = id

    val productId: Long = productId

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