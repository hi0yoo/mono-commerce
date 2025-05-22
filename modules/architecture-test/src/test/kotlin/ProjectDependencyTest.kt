package me.hi0yoo.commerce.architecture.test

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.library.Architectures
import org.junit.jupiter.api.Test

@AnalyzeClasses(packages = ["me.hi0yoo.commerce"], importOptions = [ImportOption.DoNotIncludeTests::class])
class LayerDependencyRuleTest {

    private val importedClasses: JavaClasses = ClassFileImporter().importPackages("me.hi0yoo.commerce")

    /**
     * domain 계층은 domain 계층만 참조 가능,
     * application 계층은 domain, application 계층만 참조 가능,
     * infrastructure 계층은 domain, application, infrastructure 계층만 참조 가능하다.
     */
    @ArchTest
    val layerRule = Architectures.layeredArchitecture().consideringOnlyDependenciesInLayers()
        .layer("Domain").definedBy("..domain..")
        .layer("Application").definedBy("..application..")
        .layer("Infrastructure").definedBy("..infrastructure..")

        .whereLayer("Domain").mayOnlyAccessLayers("Domain")
        .whereLayer("Application").mayOnlyAccessLayers("Application", "Domain")
        .whereLayer("Infrastructure").mayOnlyAccessLayers("Infrastructure", "Application", "Domain")

    @Test
    fun `패키지 구조 테스트`() {
        layerRule.check(importedClasses)
    }

    @Test
    fun `패키지 출력`() {
        importedClasses.map { it.packageName }.distinct().sorted().forEach(::println)
    }
}
