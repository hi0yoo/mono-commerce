package me.hi0yoo.commerce.product.domain

import jakarta.persistence.Column
import jakarta.persistence.ConstraintMode
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class Category(
    @Id
    @Column(name = "category_id")
    val id: Long,

    val name: String,

    @Enumerated(EnumType.STRING)
    val depth: Depth,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    val parent: Category? = null  // 대분류면 null, 중분류면 상위 대분류 참조
) {
    enum class Depth {
        MAIN, SUB
    }
}