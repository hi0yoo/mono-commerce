package me.hi0yoo.commerce.product.application.dto

data class CategoryResult(
    val mainId: Long,
    val mainName: String,
    val subId: Long? = null,
    val subName: String? = null,
)