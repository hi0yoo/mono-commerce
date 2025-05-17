package me.hi0yoo.commerce.order.infrastructure.config

import jakarta.annotation.PostConstruct
import org.springframework.data.redis.core.script.DefaultRedisScript
import org.springframework.stereotype.Component

@Component
class RedisScriptLoader {
    lateinit var script: DefaultRedisScript<Long>

    @PostConstruct
    fun loadScript() {
        script = DefaultRedisScript()
        script.setScriptText(
            """
            local key = KEYS[1]
            local val = redis.call("INCR", key)
            if val == 1 then
              redis.call("EXPIRE", key, 2)
            end
            return val
        """.trimIndent()
        )
        script.resultType = Long::class.java
    }
}