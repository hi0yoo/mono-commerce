package me.hi0yoo.commerce.order.infrastructure.adapter.out

import me.hi0yoo.commerce.order.application.port.out.NextOrderNumberGeneratorPort
import me.hi0yoo.commerce.order.infrastructure.redis.UniqueSequencePerSecondProvider
import org.springframework.stereotype.Component

@Component
class NextOrderNumberGeneratorAdapter(
    private val uniqueSequencePerSecondProvider: UniqueSequencePerSecondProvider,
): NextOrderNumberGeneratorPort {
    override fun fetchFrom(yyyyMMddHHmmss: String): Long {
        val key = "order_seq:$yyyyMMddHHmmss"
        return uniqueSequencePerSecondProvider.nextSequenceOf(key)
    }
}