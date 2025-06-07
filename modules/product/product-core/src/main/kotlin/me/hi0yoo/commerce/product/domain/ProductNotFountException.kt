package me.hi0yoo.commerce.product.domain

class ProductNotFountException(
    itemId: Long
): RuntimeException(
    "상품 정보를 찾을 수 없습니다. :productOptionId=${itemId}"
)