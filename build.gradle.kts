import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose.plugin)
    alias(libs.plugins.compose.compiler)
}
group = "garden.ephemeral"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)

    implementation(libs.lwjgl)
    implementation(libs.lwjgl.openal)
    implementation(libs.flogger)

    runtimeOnly(variantOf(libs.lwjgl) { classifier("natives-windows") })
    runtimeOnly(variantOf(libs.lwjgl.openal) { classifier("natives-windows") })
    runtimeOnly(libs.flogger.system.backend)

    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.kotest.assertions.core.jvm)
    testImplementation(libs.kotest.framework.datatest)
    testImplementation(libs.kotest.property)
}

compose.desktop {
    application {
        mainClass = "garden.ephemeral.audio.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Environment"
            packageVersion = "1.0.0"
        }
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
