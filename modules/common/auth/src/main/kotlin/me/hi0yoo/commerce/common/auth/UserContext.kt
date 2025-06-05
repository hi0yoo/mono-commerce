package me.hi0yoo.commerce.common.auth

interface UserContext {
    fun currentUserInfo(): UserInfo
}