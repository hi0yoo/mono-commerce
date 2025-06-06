package me.hi0yoo.commerce.order.infrastructure.adapter.out

import jakarta.transaction.Transactional
import me.hi0yoo.commerce.order.application.port.out.StockReservationPort
import me.hi0yoo.commerce.order.application.port.out.BulkReserveStockCommand
import me.hi0yoo.commerce.product.application.dto.BulkReserveStockCommand as ProductModuleBulkReserveStockCommand
import me.hi0yoo.commerce.product.application.dto.ReserveStockCommand as ProductModuleReserveStockCommand
import me.hi0yoo.commerce.product.application.port.`in`.ReserveProductStockUseCase
import org.springframework.stereotype.Component

@Component
class StockReservationAdapter(
    private val reserveProductStockUseCase: ReserveProductStockUseCase
): StockReservationPort {

    @Transactional
    override fun reserveStocks(command: BulkReserveStockCommand) {
        reserveProductStockUseCase.reserveStock(
            ProductModuleBulkReserveStockCommand(
                commands = command.commands.map { ProductModuleReserveStockCommand(it.productOptionId, it.quantity) }
            )
        )
    }
}