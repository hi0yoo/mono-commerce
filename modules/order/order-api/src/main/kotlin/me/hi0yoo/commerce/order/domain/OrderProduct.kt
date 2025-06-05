package me.hi0yoo.commerce.order.domain

import jakarta.persistence.*

@Entity
class OrderProduct(
    id: Long,
    order: Order,
    productInfo: ProductInfo,
    quantity: Int,
) {
    @Id
    @Column(name = "order_product_id")
    val id: Long = id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", columnDefinition = "varchar(20)", nullable = false, foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    val order = order

    val productId: Long = productInfo.id
    val productName = productInfo.productName
    val optionName = productInfo.optionName
    val optionPrice: Int = productInfo.optionPrice
    val quantity: Int = quantity

    @Enumerated(EnumType.STRING)
    var orderProductStatus: OrderProductStatus = OrderProductStatus.PENDING
        protected set
}