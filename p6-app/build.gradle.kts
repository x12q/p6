import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.gradle.internal.os.OperatingSystem
plugins {
    val kv = libs.versions.kotlinVersion.get()
    kotlin("jvm") version kv
    kotlin("kapt") version kv
    alias (libs.plugins.jetbrain.compose)
    `maven-publish`
    alias(libs.plugins.anvil)
    id("com.qxdzbc.p6.gradle_plugins.common_project_plugin")
    id("com.qxdzbc.p6.gradle_plugins.log_plugin")
    id("com.qxdzbc.p6.gradle_plugins.dagger_anvil_plugin")
    id("com.qxdzbc.p6.gradle_plugins.grpc_plugin")
    id("com.qxdzbc.p6.gradle_plugins.compose_test_plugin")
}

group = "com.qxdzbc.p6"
version = "1.0"

repositories {
    google()
    mavenCentral()
    mavenLocal()
    maven {
        url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        name = "Compose for Desktop DEV"
    }
    maven("https://plugins.gradle.org/m2/")

}

val p6Version = libs.versions.p6Version.get()

kotlin{
    jvmToolchain(17)
}
dependencies {
    implementation(libs.godaddy.composeColorPickerJvm)
    implementation(libs.apache.commons.text)
    implementation(libs.apache.commons.csv)
    implementation(libs.apache.commons.io)
    implementation(libs.apache.commons.math3)
    implementation(libs.kotlin.reflect)
    implementation(libs.rsyntaxtextarea)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.swing)
    implementation(libs.material.iconsExtendedDesktop)

    implementation(compose.desktop.currentOs)

    implementation("com.qxdzbc.p6:p6-proto:${p6Version}")
    implementation("com.qxdzbc.p6:p6-antlr:${p6Version}")

    implementation(project(":err"))
    implementation(project(":common"))
    implementation(project(":common-compose"))
    implementation(project(":common-test"))

}

tasks.test {
    // this is for Junit5
//    useJUnitPlatform()
    // this is for Junit4
    useJUnit()
    testLogging.showStandardStreams = true
}

val p6AppBuildDir = "p6App"

compose.desktop {
    application {
        mainClass = "com.Main2Kt"
        nativeDistributions {
            outputBaseDir.set(project.buildDir.resolve(p6AppBuildDir))
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "p6"
            packageVersion = "1.0.0"
            modules("java.sql")
//            includeAllModules = true
        }
    }
}
