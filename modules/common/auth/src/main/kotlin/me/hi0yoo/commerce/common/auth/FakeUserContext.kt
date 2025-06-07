package me.hi0yoo.commerce.common.auth

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