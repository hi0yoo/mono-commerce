package me.hi0yoo.commerce.order.domain

class OrderNotFoundException(
    orderId: String
): RuntimeException(
    "주문 정보를 찾을 수 없습니다. : orderId=$orderId"
)