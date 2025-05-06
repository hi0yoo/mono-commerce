package me.hi0yoo.commerce.common.infrastructure.auth

import me.hi0yoo.commerce.common.application.auth.UserContext
import me.hi0yoo.commerce.common.application.auth.UserInfo
import org.springframework.stereotype.Component

@Component
class FakeUserContext: UserContext {
    override fun currentUserInfo(): UserInfo {
        return UserInfo(
            id = 1L,
            name = "테스트맨",
            email = "user1@email.com",
        )
    }
}