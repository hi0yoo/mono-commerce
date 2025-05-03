package me.hi0yoo.commerce.order.application

import me.hi0yoo.commerce.common.application.auth.UserContext
import me.hi0yoo.commerce.common.domain.id.ProductOptionId
import me.hi0yoo.commerce.order.application.port.OrderNumberPort
import me.hi0yoo.commerce.order.application.port.ProductDetailRequest
import me.hi0yoo.commerce.order.application.port.ProductInventoryPort
import me.hi0yoo.commerce.order.application.port.ProductQueryPort
import me.hi0yoo.commerce.order.application.port.ReserveStockRequest
import me.hi0yoo.commerce.order.domain.Customer
import me.hi0yoo.commerce.order.domain.DeliveryAddress
import me.hi0yoo.commerce.order.domain.Order
import me.hi0yoo.commerce.order.domain.OrderRepository
import me.hi0yoo.commerce.order.domain.ProductInfo
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class PlaceOrderUseCase(
    private val orderNumberPort: OrderNumberPort,
    private val orderRepository: OrderRepository,
    private val userContext: UserContext,
    private val productInventoryPort: ProductInventoryPort,
    private val productQueryPort: ProductQueryPort,
) {

    companion object {
        private val log = LoggerFactory.getLogger(PlaceOrderUseCase::class.java)
    }

    @Transactional
    fun placeOrder(request: PlaceOrderRequest): String {
        // 상품 예약 재고 등록
        val reserveStockRequests =
            request.productQuantities.map { ReserveStockRequest(it.vendorId, it.productId, it.optionId, it.quantity) }
        productInventoryPort.reserveStocks(reserveStockRequests)

        // 주문 상품 정보 조회
        val productDetailRequests =
            request.productQuantities.map { ProductDetailRequest(it.vendorId, it.productId, it.optionId) }
        val productDetails = productQueryPort.getProductDetails(productDetailRequests)

        // 주문 번호 생성 yyyyMMdd[10자리 시퀀스]
        val yyyymmdd = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        val orderId = "$yyyymmdd${orderNumberPort.nextOrderNumber(yyyymmdd).toString().padStart(10, '0')}"

        // 주문 생성
        val userInfo = userContext.currentUserInfo()
        val customer = Customer(userInfo.id, userInfo.name, userInfo.email)
        val deliveryAddress = DeliveryAddress(request.receiverName, request.receiverAddress, request.receiverEmail)
        val productInfos = productDetails.map {
            ProductInfo(
                ProductOptionId(it.vendorId, it.productId, it.optionId),
                it.productName,
                it.optionName,
                it.price
            )
        }
        val orderProductQuantities = request.productQuantities.map {
            Pair(
                ProductOptionId(it.vendorId, it.productId, it.optionId),
                it.quantity
            )
        }
        return orderRepository.save(Order(orderId, customer, deliveryAddress, productInfos, orderProductQuantities)).id
    }
}