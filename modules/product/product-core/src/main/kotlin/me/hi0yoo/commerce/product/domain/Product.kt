package me.hi0yoo.commerce.product.domain

import jakarta.persistence.CascadeType
import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ConstraintMode
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(
    name = "product",
    indexes = [
        Index(name = "idx_product_category_id", columnList = "category_id")
    ]
)
class Product(
    id: Long,
    name: String,
    category: Category,
    thumbnailUrl: String,
    price: Int,
    vendorId: String,
    description: String? = null,
    optionSpecs: List<ProductOptionSpec>,
    productOptionIdGenerator: IdGenerator,
    imageUrls: List<String> = emptyList(),
    badges: List<ProductBadge> = emptyList(),
) {
    @Id
    @Column(name = "product_id")
    val id: Long = id

    @Column(name = "product_name", nullable = false)
    var name: String = name
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false, foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    var category: Category = category
        protected set

    @Column(nullable = false)
    var thumbnailUrl: String = thumbnailUrl
        private set

    @Column(nullable = false)
    var price: Int = price
        private set

    @Column(nullable = false)
    val vendorId: String = vendorId

    @Column(columnDefinition = "TEXT")
    var description: String? = description
        protected set

    @ElementCollection
    @CollectionTable(
        name = "product_image",
        joinColumns = [JoinColumn(
            name = "product_id",
            nullable = false,
            foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT)
        )]
    )
    @Column(name = "image_url")
    val imageUrls: List<String> = imageUrls

    @ElementCollection(targetClass = ProductBadge::class)
    @CollectionTable(
        name = "product_badge",
        joinColumns = [JoinColumn(
            name = "product_id",
            nullable = false,
            foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT)
        )]
    )
    @Column(name = "badge")
    @Enumerated(EnumType.STRING)
    val badges: List<ProductBadge> = badges

    @OneToMany(mappedBy = "product", cascade = [CascadeType.ALL], orphanRemoval = true)
    val options: MutableList<ProductOption> = mutableListOf()

    init {
        // 옵션 목록 생성
        options.addAll(
            optionSpecs.map {
                ProductOption(
                    id = productOptionIdGenerator.nextId(),
                    product = this,
                    name = it.name,
                    additionalPrice = it.additionalPrice,
                    realStockQuantity = it.realStockQuantity,
                )
            }
        )
    }

    fun addOption(option: ProductOption) {
        options.add(option)
    }
}