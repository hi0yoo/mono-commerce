package me.hi0yoo.commerce.order.infrastructure.adapter.out

import me.hi0yoo.commerce.order.application.port.out.NextOrderNumberGeneratorPort
import me.hi0yoo.commerce.order.config.OrderApiRedisScriptLoader
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component

@Component
class NextOrderNumberGeneratorAdapter(
    private val redisTemplate: StringRedisTemplate,
    private val orderApiRedisScriptLoader: OrderApiRedisScriptLoader,
): NextOrderNumberGeneratorPort {
    override fun fetchFrom(yyyyMMddHHmmss: String): Long {
        val key = "order_seq:$yyyyMMddHHmmss"
        val seq = redisTemplate.execute(orderApiRedisScriptLoader.script, listOf(key))
        return seq
    }
}