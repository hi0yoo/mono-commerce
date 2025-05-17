package me.hi0yoo.commerce.order.domain

import jakarta.persistence.*
import me.hi0yoo.commerce.common.domain.enums.OrderStatus
import me.hi0yoo.commerce.common.domain.exception.InvalidOrderIdException
import me.hi0yoo.commerce.common.domain.exception.OrderProductDuplicatedException
import me.hi0yoo.commerce.common.domain.exception.ProductNotFountException
import me.hi0yoo.commerce.common.domain.id.ProductOptionId
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

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

    var paymentInfo: PaymentInfo? = null
        protected set

    val createdAt: LocalDateTime = LocalDateTime.now()
    val modifiedAt: LocalDateTime = LocalDateTime.now()

    init {
        // 주문번호 양식이 맞는지 확인
        isValidOrderId(id) || throw InvalidOrderIdException(id)

        val productInfoMaps = productInfos.associateBy { it.id }

        val orderProducts = orderProductQuantities.map {
            val productInfo = productInfoMaps[it.first] ?: throw ProductNotFountException(it.first)
            OrderProduct(
                order = this,
                productInfo = productInfo,
                quantity = it.second,
            )
        }

        // 주문 상품 중복건 확인
        orderProducts.groupBy { it.id }.entries
            .find { it.value.size > 1 }
            ?.let {
                throw OrderProductDuplicatedException(
                    itemId = it.key,
                    quantities = it.value.map { op -> op.quantity }
                )
            }

        this.orderProducts.addAll(orderProducts)
        this.orderPrice = orderProducts.map {
            it.optionPrice.multiply(it.quantity.toBigDecimal())
        }.fold(BigDecimal.ZERO) { o1, o2 -> o1.add(o2) }
    }

    private fun isValidOrderId(id: String): Boolean {
        // 정규식을 사용하여 yyyyMMddHHmmss + 4자리 숫자 형식 확인
        val regex = Regex("""\d{14}\d{4}""") // 14자리 날짜 + 4자리 숫자
        if (!regex.matches(id)) {
            return false
        }

        // yyyyMMddHHmmss 부분을 LocalDateTime으로 유효성 확인
        val dateTimePart = id.substring(0, 14)
        val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
        return try {
            LocalDateTime.parse(dateTimePart, formatter)
            true
        } catch (e: DateTimeParseException) {
            false
        }
    }


    fun paid(paymentInfo: PaymentInfo) {
        this.paymentInfo = paymentInfo
        status = OrderStatus.PAID
    }

    override fun toString(): String {
        return "Order(id='$id', customer=$customer, deliveryAddress=$deliveryAddress, orderProducts=$orderProducts, orderPrice=$orderPrice, status=$status, createdAt=$createdAt, modifiedAt=$modifiedAt)"
    }
}