package me.hi0yoo.commerce.product.domain

import jakarta.persistence.Column
import jakarta.persistence.ConstraintMode
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class ProductOption(
    id: Long,
    product: Product,
    name: String,
    additionalPrice: Int,
    realStockQuantity: Int = 0,
) {
    @Id
    @Column(name = "product_option_id")
    val id: Long = id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "product_id",
        nullable = false,
        foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )
    val product: Product = product

    @Column(name = "option_name", nullable = false)
    var name: String = name
        protected set

    @Column(nullable = false)
    var additionalPrice: Int = additionalPrice
        protected set

    @Column(nullable = false)
    var realStockQuantity: Int = realStockQuantity
        protected set

    @Column(nullable = false)
    var reservedStockQuantity: Int = 0
        protected set

    fun increaseRealStockQuantity(quantity: Int) {
        realStockQuantity += quantity
    }

    fun increaseReservedStockQuantity(quantity: Int) {
        val availableStockQuantity = getAvailableStockQuantity()
        if (availableStockQuantity < quantity) {
            throw OutOfStockException(id, availableStockQuantity, quantity)
        }

        reservedStockQuantity += quantity
    }

    fun getAvailableStockQuantity(): Int {
        return realStockQuantity - reservedStockQuantity
    }
}