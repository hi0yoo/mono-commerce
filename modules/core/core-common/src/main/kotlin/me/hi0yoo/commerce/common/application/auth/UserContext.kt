package me.hi0yoo.commerce.common.application.auth

interface UserContext {
    fun currentUserInfo(): UserInfo
}