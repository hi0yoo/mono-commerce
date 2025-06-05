package me.hi0yoo.commerce.order.infrastructure.adapter.out

import me.hi0yoo.commerce.common.auth.UserContext
import me.hi0yoo.commerce.order.application.port.out.UserContextPort
import me.hi0yoo.commerce.order.application.port.out.UserInfoResult
import org.springframework.stereotype.Component

@Component
class SystemUserContextAdapter(
    private val userContext: UserContext,
): UserContextPort {
    override fun getCurrentUserInfo(): UserInfoResult {
        val (id, name, email) = userContext.currentUserInfo()
        return UserInfoResult(id = id, name = name, email = email)
    }
}