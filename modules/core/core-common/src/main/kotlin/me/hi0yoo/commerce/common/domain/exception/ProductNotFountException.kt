package me.hi0yoo.commerce.common.domain.exception

import me.hi0yoo.commerce.common.domain.id.ProductOptionId

class ProductNotFountException(
    itemId: ProductOptionId
): RuntimeException(
    "상품 정보를 찾을 수 없습니다. :" +
            " vendorId=${itemId.vendorId}," +
            " productId=${itemId.productId}," +
            " optionId=${itemId.optionId}"
)