package me.hi0yoo.commerce.order.domain

import jakarta.persistence.*
import me.hi0yoo.commerce.common.domain.enums.OrderProductStatus
import java.math.BigDecimal

@Entity
class OrderProduct(
    id: Long,
    order: Order,
    productInfo: ProductInfo,
    quantity: Long,
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
    val optionPrice: BigDecimal = productInfo.optionPrice
    val quantity: Long = quantity

    @Enumerated(EnumType.STRING)
    var orderProductStatus: OrderProductStatus = OrderProductStatus.PENDING
        protected set
}