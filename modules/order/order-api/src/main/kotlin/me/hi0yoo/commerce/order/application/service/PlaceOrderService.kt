package me.hi0yoo.commerce.order.application.service

import me.hi0yoo.commerce.common.snowflake.Snowflake
import me.hi0yoo.commerce.order.application.dto.PlaceOrderCommand
import me.hi0yoo.commerce.order.application.port.`in`.PlaceOrderUseCase
import me.hi0yoo.commerce.order.application.port.out.BulkReserveStockCommand
import me.hi0yoo.commerce.order.application.port.out.NextOrderNumberGeneratorPort
import me.hi0yoo.commerce.order.application.port.out.StockReservationPort
import me.hi0yoo.commerce.order.application.port.out.ProductOptionSnapshotQuery
import me.hi0yoo.commerce.order.application.port.out.ProductOptionSnapshotReaderPort
import me.hi0yoo.commerce.order.application.port.out.ReserveStockCommand
import me.hi0yoo.commerce.order.application.port.out.UserContextPort
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
class PlaceOrderService(
    private val orderRepository: OrderRepository,
    private val userContextPort: UserContextPort,
    private val stockReservationPort: StockReservationPort,
    private val nextOrderNumberGeneratorPort: NextOrderNumberGeneratorPort,
    private val productOptionSnapshotReaderPort: ProductOptionSnapshotReaderPort,
): PlaceOrderUseCase {
    private val snowflake = Snowflake()

    companion object {
        private val log = LoggerFactory.getLogger(PlaceOrderService::class.java)
    }

    @Transactional
    override fun placeOrder(command: PlaceOrderCommand): String {
        val start = System.currentTimeMillis()
        log.info(
            "Request received to place an order. User: {}, Product count: {}",
            userContextPort.getCurrentUserInfo().id,
            command.productQuantities.size
        )

        // 상품 예약 재고 등록
        val bulkReserveStockCommand = BulkReserveStockCommand(
            command.productQuantities.map {
                ReserveStockCommand(it.productOptionId, it.quantity)
            }
        )

        log.info("Calling ProductInventoryPort.reserveStocks for {} items", bulkReserveStockCommand.commands.size)
        val reserveStart = System.currentTimeMillis()
        try {
            stockReservationPort.reserveStocks(bulkReserveStockCommand)
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
        val productOptionIds = command.productQuantities.map { it.productOptionId }

        log.info("Calling ProductQueryPort.getProductDetails for {} items", productOptionIds.size)
        val queryStart = System.currentTimeMillis()
        val productDetails = try {
            productOptionSnapshotReaderPort.fetchSnapshots(ProductOptionSnapshotQuery(productOptionIds))
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
        val orderId =
            "$yyyymmddHHmmss${nextOrderNumberGeneratorPort.fetchFrom(yyyymmddHHmmss).toString().padStart(4, '0')}"
        log.info("Generated Order ID: {}", orderId)

        // 주문 생성
        val userInfo = userContextPort.getCurrentUserInfo()
        val customer = Customer(userInfo.id, userInfo.name, userInfo.email)
        val deliveryAddress = DeliveryAddress(command.receiverName, command.receiverAddress, command.receiverEmail)

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
                    command.productQuantities.map {
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