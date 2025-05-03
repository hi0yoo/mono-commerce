package me.hi0yoo.commerce.common.domain.exception

import me.hi0yoo.commerce.common.domain.id.ProductOptionId

class OutOfStockException(
    val itemId: ProductOptionId,
    val availableStock: Long,
    val requestedStock: Long,
): RuntimeException(
    "Out of stock:" +
            " vendorId=${itemId.vendorId}," +
            " productId=${itemId.productId}," +
            " optionId=${itemId.optionId}," +
            " available=$availableStock," +
            " requested=$requestedStock"
)