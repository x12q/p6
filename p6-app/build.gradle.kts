import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.gradle.internal.os.OperatingSystem
plugins {
    java
    kotlin("jvm") version "1.7.20"
    kotlin("kapt") version "1.7.20"
    id("org.jetbrains.compose") version "1.2.1"
    id("maven-publish")
    idea
    alias(libs.plugins.anvil)

    id("com.qxdzbc.p6.gradle_plugins.test_plugin")
    id("com.qxdzbc.p6.gradle_plugins.log_plugin")
    id("com.qxdzbc.p6.gradle_plugins.dagger_anvil_plugin")
    id("com.qxdzbc.p6.gradle_plugins.grpc_plugin")
}

idea {
    this.module {
        this.isDownloadSources = true
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_15
    targetCompatibility = JavaVersion.VERSION_15
}

tasks {
    compileKotlin {
        kotlinOptions { jvmTarget = "15" }
    }
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
dependencies {


    implementation(libs.godaddy.composeColorPickerJvm)

    implementation(libs.apache.commons.text)
    implementation(libs.apache.commons.csv)
    implementation(libs.apache.commons.io)

    implementation(libs.kotlin.reflect)

    implementation(libs.rsyntaxtextarea)
    implementation(compose.desktop.currentOs)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.swing)
    implementation(libs.material.iconsExtendedDesktop)

    implementation(libs.michaelbull.kotlinResult)

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
fun getBinaryDir(): String? {
    val linuxBuildDir = "${p6AppBuildDir}/main/app/p6/bin"
    val windowBuildDir = p6AppBuildDir
    var binDir: String? = null
    if (os.isLinux) {
        binDir = linuxBuildDir
    }
    if (os.isWindows) {
        binDir = "${p6AppBuildDir}"
    }
    return binDir
}

fun getDistDir(): String? {
    val linuxBuildDir = "${p6AppBuildDir}/main/app/p6/"
    val windowBuildDir = p6AppBuildDir
    var binDir: String? = null
    if (os.isLinux) {
        binDir = linuxBuildDir
    }
    if (os.isWindows) {
        binDir = "${p6AppBuildDir}"
    }
    return binDir
}

val os = OperatingSystem.current();

tasks.register<Copy>("copyP6PythonEnv") {
    from(layout.projectDirectory.dir("p6Env"))
    val binDir = getBinaryDir()
    if (binDir != null) {
        into(layout.buildDirectory.dir("${binDir}/p6Env"))
    }
}

/**
 * zip the p6 distribution into a zip file, store the zip file in build/dist
 */
tasks.register<Zip>("zipP6") {
    archiveFileName.set("p6App.zip")
    isZip64 = true // so that large folder can be zip
    destinationDirectory.set(layout.buildDirectory.dir("dist"))
    val distDir = getDistDir()
    if (distDir != null) {
        from(layout.buildDirectory.dir(distDir))
    }
}

/**
 * copy the jp config file into the distribution dir, right next to the execution file
 */
tasks.register<Copy>("copyJpConfig") {
    from(layout.projectDirectory.file("p6PythonConfig.json"))
    val binDir = getBinaryDir()
    if (binDir != null) {
        into(layout.buildDirectory.dir(binDir))
    }
}
/**
 * build p6 and create a distribution with a ready-to-use python env then create a zip file from it.
 */
tasks.register("buildP6WithPythonZip") {
    dependsOn("buildP6WithPython")
    finalizedBy("zipP6")
}

/**
 * build a standalone p6 without python then create a zip file from it.
 */
tasks.register("buildP6Zip") {
    dependsOn("createDistributable")
    finalizedBy("zipP6")
}


/**
 * build p6 and create a distribution with a ready-to-use python env.
 * This distribution is extremely heavy, not recommended.
 */
tasks.register("buildP6WithPython") {
    dependsOn("createDistributable")
    finalizedBy("copyJpConfig", "copyP6PythonEnv")
}

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
