package me.hi0yoo.commerce.order.domain

import jakarta.persistence.*

@Entity
class OrderProduct(
    id: Long,
    order: Order,
    orderProductSpec: OrderProductSpec,
    quantity: Int,
) {
    @Id
    @Column(name = "order_product_id")
    val id: Long = id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", columnDefinition = "varchar(20)", nullable = false, foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    val order = order

    val productId: Long = orderProductSpec.productId
    val productName = orderProductSpec.productName
    val price = orderProductSpec.price
    val optionId: Long = orderProductSpec.optionId
    val optionName = orderProductSpec.optionName
    val additionalPrice: Int = orderProductSpec.additionalPrice
    val quantity: Int = quantity

    val productVendorId: String = orderProductSpec.productVendorId

    @Enumerated(EnumType.STRING)
    var orderProductStatus: OrderProductStatus = OrderProductStatus.PENDING
        protected set
}