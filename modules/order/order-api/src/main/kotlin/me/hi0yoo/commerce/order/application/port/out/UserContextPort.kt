package me.hi0yoo.commerce.order.application.port.out

interface UserContextPort {
    fun getCurrentUserInfo(): UserInfoResult
}

data class UserInfoResult(
    val id: Long,
    val name: String,
    val email: String,
)