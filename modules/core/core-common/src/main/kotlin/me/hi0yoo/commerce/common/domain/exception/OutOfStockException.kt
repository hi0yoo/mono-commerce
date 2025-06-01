package me.hi0yoo.commerce.common.domain.exception

class OutOfStockException(
    val itemId: Long,
    val availableStock: Long,
    val requestedStock: Long,
): RuntimeException(
    "Out of stock:" +
            " itemId=${itemId}," +
            " available=$availableStock," +
            " requested=$requestedStock"
)