package me.hi0yoo.commerce.order.domain

enum class OrderStatus(private val description: String) {
    PENDING("주문 생성, 결제 대기"),
    PAID("결제 완료"),
    CANCELLED("주문 취소"),
    COMPLETED("주문 확정"),
    ;
}