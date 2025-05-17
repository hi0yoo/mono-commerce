package me.hi0yoo.commerce.order.infrastructure.adapter

import me.hi0yoo.commerce.order.application.port.OrderNumberPort
import me.hi0yoo.commerce.order.infrastructure.config.RedisScriptLoader
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component

@Component
class OrderNumberAdapter(
    private val redisTemplate: StringRedisTemplate,
    private val redisScriptLoader: RedisScriptLoader,
): OrderNumberPort {
    override fun nextOrderNumber(yyyyMMddHHmmss: String): Long {
        val key = "order_seq:$yyyyMMddHHmmss"
        val seq = redisTemplate.execute(redisScriptLoader.script, listOf(key)) ?: 1L
        return seq
    }
}