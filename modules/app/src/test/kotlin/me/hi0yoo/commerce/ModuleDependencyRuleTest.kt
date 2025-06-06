package me.hi0yoo.commerce

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import com.tngtech.archunit.library.Architectures
import org.junit.jupiter.api.Test

@AnalyzeClasses(packages = ["me.hi0yoo.commerce"], importOptions = [ImportOption.DoNotIncludeTests::class])
class ModuleDependencyRuleTest {
    private val importedClasses: JavaClasses = ClassFileImporter().importPackages("me.hi0yoo.commerce")

    /**
     * presentation 계층은 presentation, application 계층만 참조할 수 있다.
     * application 계층은 domain, application 계층만 참조할 수 있다.
     * domain 계층은 domain 계층만 참조할 수 있다.
     * infrastructure 계층은 전체 계층을 참조 가능하다.
     *
     * 다른 모듈에 대한 의존은 infrastructure 에서만 가능하다.
     */
    @ArchTest
    val layerRule = Architectures.layeredArchitecture().consideringOnlyDependenciesInLayers()
        .layer("Presentation").definedBy("..presentation..")
        .layer("Application").definedBy("..application..")
        .layer("Domain").definedBy("..domain..")
        .layer("Infrastructure").definedBy("..infrastructure..")

        .whereLayer("Presentation").mayOnlyAccessLayers("Presentation", "Application")
        .whereLayer("Application").mayOnlyAccessLayers("Application", "Domain")
        .whereLayer("Domain").mayOnlyAccessLayers("Domain")
        .whereLayer("Infrastructure").mayOnlyAccessLayers("Presentation", "Application", "Domain", "Infrastructure")

    @Test
    fun `패키지 구조 테스트`() {
        layerRule.check(importedClasses)
    }

    @Test
    fun `패키지 출력`() {
        importedClasses.map { it.packageName }.distinct().sorted().forEach(::println)
    }

    /**
     * 지정한 모듈은 infrastructure를 제외한 위치에서 다른 모듈에 의존하면 안 됨
     */
    private fun assertNoDependencyToOtherModules(module: String) {
        val basePackage = "me.hi0yoo.commerce"
        val modulePackage = "$basePackage.$module"

        // 현재 모듈을 제외한 모든 모듈 패키지를 찾음
        val otherModulePackages = listOf("order", "product", "member", "coupon") // 여기에 프로젝트의 실제 모듈 목록을 나열
            .filterNot { it == module }
            .map { "$basePackage.$it.." }

        val rule = noClasses()
            .that()
            .resideInAPackage("$modulePackage..")
            .and().resideOutsideOfPackage("..infrastructure..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage(*otherModulePackages.toTypedArray())
            .`as`("모듈 '$module' 은 infrastructure 외부에서 다른 모듈에 의존하면 안 됩니다.")

        rule.check(importedClasses)
    }

    @Test
    fun `order 모듈은 다른 모듈에 infrastructure 외에서 의존하면 안 된다`() {
        assertNoDependencyToOtherModules("order")
    }

    @Test
    fun `product 모듈은 다른 모듈에 infrastructure 외에서 의존하면 안 된다`() {
        assertNoDependencyToOtherModules("product")
    }

}