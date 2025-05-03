package me.hi0yoo.commerce.order.domain

enum class OrderProductStatus(private val description: String) {
    PENDING("입점사 주문 확인 대기중"),
    PREPARING_FOR_SHIPMENT("상품 준비중"),
    READY_FOR_DELIVERY("출고 완료, 배송 대기"),
    SHIPPED("배송중"),
    DELIVERED("배송완료"),
    ;
}