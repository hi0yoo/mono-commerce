package me.hi0yoo.commerce.order.infrastructure.sequence

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@Entity
class OrderNumberSequence(
    @Id
    val date: String,
    var seq: Long = 0,
) {
    init {
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        try {
            formatter.parse(date)
        } catch (e: DateTimeParseException) {
            throw IllegalArgumentException("Invalid date format for OrderNumberSequence.date: expected yyyyMMdd")
        }
    }

    fun increment() = ++seq
}