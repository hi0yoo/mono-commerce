package me.hi0yoo.commerce.order.domain

import java.lang.RuntimeException

class OrderProductDuplicatedException(
    val itemId: Long,
    val quantities: List<Int>,
): RuntimeException("주문 상품 요청에 중복건이 있습니다. itemId=$itemId, quantities=$quantities")