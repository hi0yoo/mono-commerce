package me.hi0yoo.commerce.common.domain.exception

import java.lang.RuntimeException

class OrderProductDuplicatedException(
    val itemId: Long,
    val quantities: List<Long>,
): RuntimeException("주문 상품 요청에 중복건이 있습니다. itemId=$itemId, quantities=$quantities")