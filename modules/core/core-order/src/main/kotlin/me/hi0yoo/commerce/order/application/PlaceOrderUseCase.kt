package me.hi0yoo.commerce.order.application

import me.hi0yoo.commerce.common.application.auth.UserContext
import me.hi0yoo.commerce.common.snowflake.Snowflake
import me.hi0yoo.commerce.order.application.port.OrderNumberPort
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
    private val snowflake = Snowflake()

    companion object {
        private val log = LoggerFactory.getLogger(PlaceOrderUseCase::class.java)
    }

    @Transactional
    fun placeOrder(request: PlaceOrderRequest): String {
        val start = System.currentTimeMillis()
        log.info(
            "Request received to place an order. User: {}, Product count: {}",
            userContext.currentUserInfo().id,
            request.productQuantities.size
        )

        // 상품 예약 재고 등록
        val reserveStockRequests = request.productQuantities.map {
            ReserveStockRequest(it.productOptionId, it.quantity)
        }

        log.info("Calling ProductInventoryPort.reserveStocks for {} items", reserveStockRequests.size)
        val reserveStart = System.currentTimeMillis()
        try {
            productInventoryPort.reserveStocks(reserveStockRequests)
            log.info("ProductInventoryPort.reserveStocks completed in {}ms", System.currentTimeMillis() - reserveStart)
        } catch (e: Exception) {
            log.error(
                "Error while reserving stocks. Took {}ms. Error: {}",
                System.currentTimeMillis() - reserveStart,
                e.message,
                e
            )
            throw e
        }

        // 주문 상품 정보 조회
        val productDetailRequests = request.productQuantities.map { it.productOptionId }

        log.info("Calling ProductQueryPort.getProductDetails for {} items", productDetailRequests.size)
        val queryStart = System.currentTimeMillis()
        val productDetails = try {
            productQueryPort.getProductDetails(productDetailRequests)
                .also {
                    log.info(
                        "ProductQueryPort.getProductDetails completed in {}ms",
                        System.currentTimeMillis() - queryStart
                    )
                }
        } catch (e: Exception) {
            log.error(
                "Error while fetching product details. Took {}ms. Error: {}",
                System.currentTimeMillis() - queryStart,
                e.message,
                e
            )
            throw e
        }

        // 주문 번호 생성
        val yyyymmddHHmmss = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
        val orderId = "$yyyymmddHHmmss${orderNumberPort.nextOrderNumber(yyyymmddHHmmss).toString().padStart(4, '0')}"
        log.info("Generated Order ID: {}", orderId)

        // 주문 생성
        val userInfo = userContext.currentUserInfo()
        val customer = Customer(userInfo.id, userInfo.name, userInfo.email)
        val deliveryAddress = DeliveryAddress(request.receiverName, request.receiverAddress, request.receiverEmail)

        val orderStart = System.currentTimeMillis()
        val order = try {
            orderRepository.save(
                Order(
                    orderId,
                    customer,
                    deliveryAddress,
                    productDetails.map {
                        ProductInfo(
                            it.productOptionId,
                            it.productName,
                            it.optionName,
                            it.price
                        )
                    },
                    request.productQuantities.map {
                        Pair(it.productOptionId, it.quantity)
                    },
                    { snowflake.nextId() },
                )
            ).also { log.info("OrderRepository.save completed in {}ms", System.currentTimeMillis() - orderStart) }
        } catch (e: Exception) {
            log.error(
                "Error while saving order. Took {}ms. Error: {}",
                System.currentTimeMillis() - orderStart,
                e.message,
                e
            )
            throw e
        }

        log.info(
            "PlaceOrder process completed successfully for orderId: {} in {}ms",
            order.id,
            System.currentTimeMillis() - start
        )
        return order.id
    }
}