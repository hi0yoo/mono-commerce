package me.hi0yoo.commerce.order.infrastructure.redis

import jakarta.annotation.PostConstruct
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.script.DefaultRedisScript
import org.springframework.stereotype.Component

@Component
class UniqueSequencePerSecondProvider(
    private val redisTemplate: RedisTemplate<String, String>
) {
    private lateinit var script: DefaultRedisScript<Long>

    @PostConstruct
    fun loadScript() {
        script = DefaultRedisScript()
        script.setScriptText(
            """
            local key = KEYS[1]
            local val = redis.call("INCR", key)
            if val == 1 then
              redis.call("EXPIRE", key, 1)
            end
            return val
        """.trimIndent()
        )
        script.resultType = Long::class.java
    }

    fun nextSequenceOf(key: String): Long {
        return redisTemplate.execute(script, listOf(key))
    }
}