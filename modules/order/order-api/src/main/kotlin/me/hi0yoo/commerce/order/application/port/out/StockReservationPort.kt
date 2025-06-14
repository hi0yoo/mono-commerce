package me.hi0yoo.commerce.order.application.port.out

interface StockReservationPort {
    fun reserveStocks(command: BulkReserveStockCommand)
}

data class BulkReserveStockCommand(
    val commands: List<ReserveStockCommand>,
)

data class ReserveStockCommand(
    val productOptionId: Long,
    val quantity: Int,
)
