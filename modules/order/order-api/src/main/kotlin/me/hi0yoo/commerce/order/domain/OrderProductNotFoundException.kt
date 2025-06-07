package me.hi0yoo.commerce.order.domain

import java.lang.RuntimeException

class OrderProductNotFoundException(
    productOptionId: Long,
): RuntimeException("주문 상품 정보를 찾을 수 없습니다. :productOptionId=${productOptionId}")