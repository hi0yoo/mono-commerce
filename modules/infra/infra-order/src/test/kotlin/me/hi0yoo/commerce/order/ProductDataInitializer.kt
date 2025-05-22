package me.hi0yoo.commerce.order

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import me.hi0yoo.commerce.order.infrastructure.product.Product
import me.hi0yoo.commerce.order.infrastructure.product.ProductOption
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.support.TransactionTemplate
import java.math.BigDecimal
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@Disabled
@DataJpaTest
@ContextConfiguration(classes = [JpaTestConfiguration::class])
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductDataInitializer {

    private val BULK_INSERT_SIZE = 2000
    private val TOTAL_PRODUCTS_PER_VENDOR = 1_000_000
    private val EXECUTE_COUNT = TOTAL_PRODUCTS_PER_VENDOR / BULK_INSERT_SIZE
    private val adjectives = listOf("프리미엄", "심플", "럭셔리", "모던", "베이직", "클래식", "컴팩트", "스마트")
    private val materials = listOf("우드", "메탈", "패브릭", "레더", "유니섹스", "에코", "글로시", "알루미늄")
    private val productTypes = listOf("의자", "책상", "가방", "셔츠", "조명", "커튼", "헤드폰", "머그컵")

    @PersistenceContext
    lateinit var entityManager: EntityManager

    @Autowired
    lateinit var transactionTemplate: TransactionTemplate

    val latch = CountDownLatch(EXECUTE_COUNT)

    @Test
    fun initProductsAndOptions() {
        // 업체당 상품 100만건, 업체 5개 = 500만건
        // 상품당 옵션 3개 = 1500만건
        val executorService = Executors.newFixedThreadPool(10)

        for (vendorIndex in 1..5) {
            for (i in 0 until EXECUTE_COUNT) {
                val start = i * BULK_INSERT_SIZE
                val end = (i + 1) * BULK_INSERT_SIZE
                executorService.submit {
                    insertProduct(vendorIndex, start, end)
                    latch.countDown()
                    println("latch.getCount() = " + latch.count)
                }
            }
        }
        latch.await()
        executorService.shutdown()
    }

    fun insertProduct(vendorIndex: Int, start: Int, end: Int) {
        transactionTemplate.executeWithoutResult {
            for (i in start until end) {
                val sequence = (vendorIndex - 1) * TOTAL_PRODUCTS_PER_VENDOR + i
                val vendorId = "V${vendorIndex.toString().padStart(3, '0')}"
                val productId = (i + 1).toString().padStart(8, '0')
                val productName = generateProductName(sequence)

                val product = Product(
                    vendorId = vendorId,
                    productId = productId,
                    productName = productName,
                )
                entityManager.persist(product)

                insertProductOption(product)
            }
        }
    }

    fun generateProductName(index: Int): String {
        val adj = adjectives[index % adjectives.size]
        val mat = materials[(index / adjectives.size) % materials.size]
        val type = productTypes[(index / (adjectives.size * materials.size)) % productTypes.size]
        return "$adj $mat $type"
    }

    fun insertProductOption(product: Product) {
        val optionNames = listOf("A", "B", "C")

        for (i in 1..3) {
            val optionId = "OPT$i"
            val optionName = "${optionNames[(i - 1) % optionNames.size]} 옵션 $i"
            val optionPrice = BigDecimal(1000 + i * 100)

            val option = ProductOption(
                optionId = optionId,
                optionName = optionName,
                optionPrice = optionPrice,
                product = product,
                realStockQuantity = 10_000,
            )

            entityManager.persist(option)
        }
    }
}
