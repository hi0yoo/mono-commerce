package me.hi0yoo.commerce.product

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import me.hi0yoo.commerce.common.snowflake.Snowflake
import me.hi0yoo.commerce.product.domain.Category
import me.hi0yoo.commerce.product.domain.Product
import me.hi0yoo.commerce.product.domain.ProductOptionSpec
import me.hi0yoo.commerce.product.infrastructure.vendor.Vendor
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.support.TransactionTemplate
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import javax.sql.DataSource

//@Disabled
@DataJpaTest
@Rollback(false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = [JpaTestConfiguration::class])
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductDataInitializer {
    companion object {
        private val log = LoggerFactory.getLogger(ProductDataInitializer::class.java)
    }


    @PersistenceContext
    lateinit var entityManager: EntityManager

    @Autowired
    lateinit var transactionTemplate: TransactionTemplate

    @Autowired
    lateinit var dataSource: DataSource

    @BeforeAll
    fun printDbInfo() {
        dataSource.connection.use { conn ->
            val meta = conn.metaData
            log.info("=== DB 연결 정보 ===")
            log.info("URL         : ${meta.url}")
            log.info("Driver Name : ${meta.driverName}")
            log.info("Version     : ${meta.databaseProductVersion}")
            log.info("User Name   : ${meta.userName}")
            log.info("===================")
        }
    }

    @Test
    fun initVendors() {
        val vendors = listOf(
            "FM" to "패션마켓",
            "URB" to "어반룩",
            "CLT" to "컬렉트",
            "MNS" to "모던수트",
            "GRL" to "걸스무드",
            "BYS" to "바이서울",
            "STR" to "스트리트샵",
            "OVS" to "오버사이즈",
            "CLS" to "클래식수트",
            "DNI" to "데일리니트",
            "CVC" to "캔버스컬렉션",
            "BLN" to "블랙앤",
            "TWL" to "트윌스토어",
            "MRD" to "미니멀라운드",
            "SCL" to "스쿨룩",
            "VNT" to "빈티지타운",
            "DPR" to "디퍼런트",
            "MLN" to "모노라인",
            "EXF" to "에센셜핏",
            "STY" to "스타일링",
            "JNS" to "진스플레이",
            "ACT" to "액티브웨어",
            "CTY" to "시티모드",
            "LNB" to "린넨브리즈",
            "PRM" to "프리미엄핏",
            "SPC" to "스페이스클로젯",
            "RWD" to "로우디자인",
            "EDG" to "엣지웨어",
            "NBL" to "네이비라인",
            "SLT" to "솔리드템포",
            "MOD" to "모던핏",
            "HDD" to "후디디스트릭트",
            "CRW" to "크루웨어",
            "BCL" to "베이직클럽",
            "FTS" to "핏스튜디오",
            "LUX" to "럭스라인",
            "UNX" to "유니섹스랩",
            "RFX" to "리플렉스",
            "AVT" to "아웃핏",
            "FLR" to "플로럴",
            "TRD" to "트렌디샵",
            "BMT" to "블루밍타운",
            "CMF" to "컴포트웨어",
            "FRK" to "프리크",
            "STN" to "스탠다드",
            "WKN" to "위켄더",
            "HRB" to "하이리버",
            "ZRO" to "제로클로즈",
            "MNT" to "먼트"
        )

        transactionTemplate.executeWithoutResult {
            vendors.forEach { (id, name) ->
                entityManager.persist(Vendor(id = id, name = name))
            }
        }
    }

    @Test
    fun initCategories() {
        val mainCategories = listOf(
            Category(id = 1L, name = "남성의류", depth = Category.Depth.MAIN),
            Category(id = 2L, name = "여성의류", depth = Category.Depth.MAIN),
            Category(id = 3L, name = "아동의류", depth = Category.Depth.MAIN),
            Category(id = 4L, name = "신발", depth = Category.Depth.MAIN),
            Category(id = 5L, name = "가방/잡화", depth = Category.Depth.MAIN)
        )

        val subCategories = listOf(
            Category(id = 101L, name = "셔츠", depth = Category.Depth.SUB, parent = mainCategories[0]),
            Category(id = 102L, name = "원피스", depth = Category.Depth.SUB, parent = mainCategories[1]),
            Category(id = 103L, name = "아동 상의", depth = Category.Depth.SUB, parent = mainCategories[2]),
            Category(id = 104L, name = "스니커즈", depth = Category.Depth.SUB, parent = mainCategories[3]),
            Category(id = 105L, name = "백팩", depth = Category.Depth.SUB, parent = mainCategories[4])
        )

        mainCategories.forEach { category -> entityManager.persist(category) }
        subCategories.forEach { category -> entityManager.persist(category) }
    }

    private val BULK_INSERT_SIZE = 5_000
    private val TOTAL_PRODUCT_COUNT_PER_VENDOR = 50_000 // 업체당 상품 갯수

    val snowflake = Snowflake()

    @Test
    fun initProductsWithOptions() {
        // 카테고리 데이터 조회
        val subCategories = entityManager.createQuery(
            "SELECT c FROM Category c WHERE c.depth = :depth",
            Category::class.java
        ).setParameter("depth", Category.Depth.SUB)
            .resultList

        // 카테고리별 상품 수
        val categoryCount = subCategories.size

        // 회사 데이터 조회
        val vendors = entityManager.createQuery(
            "SELECT v FROM Vendor v",
            Vendor::class.java
        ).resultList

        val executeCountPerVendor = TOTAL_PRODUCT_COUNT_PER_VENDOR / BULK_INSERT_SIZE
        val latch = CountDownLatch(vendors.size * executeCountPerVendor)
        val executorService = Executors.newFixedThreadPool(100)

        // 회사별 상품 생성
        for (vendor in vendors) {
            for (i in 0 until executeCountPerVendor) {
                val start = i * BULK_INSERT_SIZE
                val end = (i + 1) * BULK_INSERT_SIZE

                executorService.submit {
                    transactionTemplate.executeWithoutResult {
                        for (i in start until end) {
                            val sequence = start + i
                            val category = subCategories[sequence % categoryCount]

                            val product = generateProduct(sequence = sequence, category = category, vendor = vendor)
                            entityManager.persist(product)
                        }
                    }
                    latch.countDown()
                    log.info("latch.getCount() = ${latch.count}")
                }
            }
        }
        latch.await()
        executorService.shutdown()
    }

    fun generateProduct(
        sequence: Int,
        category: Category,
        vendor: Vendor
    ): Product {
        // 상품 정보 구성
        val productId = snowflake.nextId()
        val productName = generateProductName(category.name, sequence)
        val description = generateProductDescription(category.name)

        return Product(
            id = productId,
            name = productName,
            category = category,
            thumbnailUrl = "https://www.dummyimage.com/300/000/fff?text=$productId",
            price = 10_000 + ((sequence % 91) * 1_000),
            vendorId = vendor.id,
            description = description,
            optionSpecs = generateOptionSpecs(),
            productOptionIdGenerator = snowflake::nextId,
            imageUrls = (1..5).map { idx -> "https://www.dummyimage.com/300/000/fff?text=${productId}_$idx" },
            badges = emptyList()
        )
    }

    val styles = listOf(
        "베이직", "슬림핏", "오버핏", "모던", "빈티지", "세미오버", "컴포트핏", "크롭핏", "루즈핏", "레귤러핏",
        "하이넥", "브이넥", "라운드넥", "헨리넥", "집업", "카라넥", "슬릿핏", "스트레이트핏", "테이퍼드핏", "롱슬리브",
        "숏슬리브", "반팔", "긴팔", "나시", "래글런", "벌룬핏", "퍼프핏", "트렌치핏", "트레이닝핏", "하프집업",
        "셔츠형", "자켓형", "후드형", "스웨터형", "아노락", "세트업", "슬리브리스", "포멀핏", "아웃도어핏", "슬림라인",
        "프리핏", "레더핏", "샤이니핏", "플로우핏", "소프트핏", "하드핏", "로우핏", "업핏", "이지핏", "베스트핏"
    )
    val colors = listOf(
        "화이트", "블랙", "네이비", "그레이", "카키", "브라운", "베이지", "스카이블루", "라이트퍼플", "차콜",
        "아이보리", "연핑크", "인디고", "딥그린", "머스타드", "오렌지", "버건디", "올리브", "레드", "블루",
        "옐로우", "라벤더", "와인", "민트", "코랄", "탄", "크림", "실버", "골드", "퍼플",
        "스틸블루", "라이트카키", "연두", "다크브라운", "라일락", "네온옐로우", "딥블루", "딥카키", "딥레드", "네온핑크",
        "스톤", "청록", "레몬", "피치", "로즈", "마린블루", "밤색", "스모키그레이", "모카", "에메랄드"
    )
    val materials = listOf(
        "코튼", "린넨", "폴리", "울", "플리스", "스웨이드", "데님", "나일론", "레더", "트위드",
        "저지", "벨벳", "레이온", "캐시미어", "텐셀", "앙고라", "샤틴", "모달", "혼방", "메쉬",
        "플란넬", "피케", "옥스퍼드", "실크", "크레이프", "마이크로화이버", "네오프렌", "라미", "트리코트", "기모",
        "퀼팅", "스판덱스", "드라이핏", "블렌디드", "워싱면", "리사이클원단", "소로나", "폼", "하이브리드", "에어로쿨",
        "에코퍼", "유광", "무광", "친환경원단", "방수소재", "흡한속건", "통기성패브릭", "3D니트", "골지", "사선직"
    )
    val extras = listOf(
        "무지", "스트라이프", "체크", "프린트", "자수", "워싱", "슬릿", "포켓", "지퍼", "카라",
        "버튼", "벨트", "셔링", "배색", "패턴", "트임", "퍼프", "앞트임", "뒤트임", "프릴",
        "터틀", "스티치", "메탈", "플랩", "비즈", "홀터", "스트랩", "퍼", "스냅", "퀼트",
        "배색라인", "반전포인트", "언밸런스", "리본", "플리츠", "랩", "볼드", "에이라인", "딥넥", "하이웨스트",
        "미니멀", "빅카라", "페플럼", "컷아웃", "더블버튼", "투웨이", "후드", "케이프", "브이컷", "디링"
    )

    fun generateProductName(category: String, index: Int): String {
        val style = styles[index % styles.size]
        val color = colors[(index / 50) % colors.size]
        val material = materials[(index / 2_500) % materials.size]
        val extra = extras[(index / 125_000) % extras.size]

        return "$style $color $material $extra $category"
    }

    fun generateProductDescription(category: String): String {
        return "$category 카테고리의 인기 상품입니다. 고급 소재와 트렌디한 디자인이 특징입니다."
    }

    fun generateOptionSpecs(): List<ProductOptionSpec> {
        val realStockQuantity = 999_999
        return listOf(
            ProductOptionSpec("S", 0, realStockQuantity),
            ProductOptionSpec("M", 3000, realStockQuantity),
            ProductOptionSpec("L", 5000, realStockQuantity)
        )
    }
}

@Configuration
@EnableJpaRepositories(basePackages = ["me.hi0yoo.commerce.product"])
@EntityScan(basePackages = ["me.hi0yoo.commerce.product"])
class JpaTestConfiguration