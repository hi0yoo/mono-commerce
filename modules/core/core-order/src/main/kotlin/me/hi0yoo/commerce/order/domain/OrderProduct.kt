package me.hi0yoo.commerce.order.domain

import jakarta.persistence.*
import me.hi0yoo.commerce.common.domain.id.OrderProductId
import java.math.BigDecimal

@Entity
class OrderProduct(
    order: Order,
    productInfo: ProductInfo,
    quantity: Long,
) {
    @EmbeddedId
    val id = OrderProductId(
        order.id,
        productInfo.id,
    )

    @MapsId("orderId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", columnDefinition = "varchar(20)", nullable = false, foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    val order = order

    val productName = productInfo.productName
    val optionName = productInfo.optionName
    val optionPrice: BigDecimal = productInfo.optionPrice
    val quantity: Long = quantity

    @Enumerated(EnumType.STRING)
    var orderProductStatus: OrderProductStatus = OrderProductStatus.PENDING
        protected set
}