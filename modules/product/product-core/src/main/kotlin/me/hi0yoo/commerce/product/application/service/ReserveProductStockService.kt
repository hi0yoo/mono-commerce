package me.hi0yoo.commerce.product.application.service

import me.hi0yoo.commerce.product.application.dto.BulkReserveStockCommand
import me.hi0yoo.commerce.product.application.port.`in`.ReserveProductStockUseCase
import me.hi0yoo.commerce.product.domain.ProductNotFountException
import me.hi0yoo.commerce.product.domain.ProductOptionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ReserveProductStockService(
    private val productOptionRepository: ProductOptionRepository
): ReserveProductStockUseCase {

    override fun reserveStock(command: BulkReserveStockCommand) {
        val optionIds = command.commands.map { it.productOptionId }
        val optionsMap = productOptionRepository.findAllByIdForUpdate(optionIds).associateBy { it.id }

        for (reserveStockCommand in command.commands) {
            optionsMap[reserveStockCommand.productOptionId]
                ?.increaseReservedStockQuantity(reserveStockCommand.quantity)
                ?: throw ProductNotFountException(reserveStockCommand.productOptionId)
        }
    }
}