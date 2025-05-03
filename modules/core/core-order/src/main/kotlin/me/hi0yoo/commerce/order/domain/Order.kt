package me.hi0yoo.commerce.order.domain

import jakarta.persistence.*
import me.hi0yoo.commerce.common.domain.exception.ProductNotFountException
import me.hi0yoo.commerce.common.domain.id.ProductOptionId
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "orders")
class Order(
    id: String,
    customer: Customer,
    deliveryAddress: DeliveryAddress,
    productInfos: List<ProductInfo>,
    orderProductQuantities: List<Pair<ProductOptionId, Long>>,
) {
    // 주문번호
    @Id
    @Column(name = "order_id", length = 20)
    val id: String = id

    // 주문자 정보
    @Embedded
    val customer: Customer = customer

    // 배송지 정보
    @Embedded
    val deliveryAddress: DeliveryAddress = deliveryAddress

    // 주문 상품 정보
    @OneToMany(mappedBy = "order", cascade = [(CascadeType.ALL)], orphanRemoval = true)
    val orderProducts: MutableList<OrderProduct> = mutableListOf()

    // 주문 금액
    var orderPrice: BigDecimal = BigDecimal.ZERO
        protected set

    // 주문 상태
    @Enumerated(EnumType.STRING)
    var status: OrderStatus = OrderStatus.PENDING
        protected set

    val createdAt: LocalDateTime = LocalDateTime.now()
    val modifiedAt: LocalDateTime = LocalDateTime.now()

    init {
        val orderProducts = orderProductQuantities.map {
            val productInfo = (productInfos.find { productInfo -> productInfo.id == it.first }
                ?: throw ProductNotFountException(it.first))
            OrderProduct(
                order = this,
                productInfo = productInfo,
                quantity = it.second,
            )
        }

        this.orderProducts.addAll(orderProducts)
        this.orderPrice = orderProducts.map {
            it.optionPrice.multiply(it.quantity.toBigDecimal())
        }.fold(BigDecimal.ZERO) { o1, o2 -> o1.add(o2) }
    }

    override fun toString(): String {
        return "Order(id='$id', customer=$customer, deliveryAddress=$deliveryAddress, orderProducts=$orderProducts, orderPrice=$orderPrice, status=$status, createdAt=$createdAt, modifiedAt=$modifiedAt)"
    }
}