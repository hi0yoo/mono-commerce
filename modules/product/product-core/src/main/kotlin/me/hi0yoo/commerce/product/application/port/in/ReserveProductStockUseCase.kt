package me.hi0yoo.commerce.product.application.port.`in`

import me.hi0yoo.commerce.product.application.dto.BulkReserveStockCommand

interface ReserveProductStockUseCase {
    fun reserveStock(command: BulkReserveStockCommand)
}