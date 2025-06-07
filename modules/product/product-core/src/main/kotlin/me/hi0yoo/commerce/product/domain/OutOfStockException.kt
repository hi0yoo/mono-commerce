package me.hi0yoo.commerce.product.domain

class OutOfStockException(
    val itemId: Long,
    val availableStock: Int,
    val requestedStock: Int,
): RuntimeException(
    "Out of stock:" +
            " itemId=${itemId}," +
            " available=$availableStock," +
            " requested=$requestedStock"
)