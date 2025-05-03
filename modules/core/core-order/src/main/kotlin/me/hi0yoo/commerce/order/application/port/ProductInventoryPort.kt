package me.hi0yoo.commerce.order.application.port

interface ProductInventoryPort {
    fun reserveStocks(requests: List<ReserveStockRequest>)
}