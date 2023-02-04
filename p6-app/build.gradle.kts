import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.gradle.internal.os.OperatingSystem

plugins {
    java
    kotlin("jvm") version "1.7.20"
    kotlin("kapt") version "1.7.20"
    id("org.jetbrains.compose") version "1.2.1"
    id("maven-publish")
    idea
    id ("com.squareup.anvil") version "2.4.2"
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

val protocolBufferVersion = "3.19.4"
val kotlinGRPCVersion = "1.2.1"
val grpcVersion = "1.41.0"
val dagger2Version = "2.43.1"
val composeTestVersion = "1.1.1"
val coroutineVersion = "1.6.1"
val mockitoVersion = "4.5.1"
val p6Version = "1.0"
//extra["p6Version"]
val apacheCommonTextVersion = "1.10.0"
val apacheCommonCsvVersion = "1.9.0"
val apacheCommonIOVersion = "2.11.0"
val kotestVersion="5.5.4"
dependencies {

    implementation("com.godaddy.android.colorpicker:compose-color-picker-jvm:0.5.1")

    implementation("org.apache.commons:commons-text:${apacheCommonTextVersion}")
    implementation("org.apache.commons:commons-csv:${apacheCommonCsvVersion}")
    implementation("commons-io:commons-io:${apacheCommonIOVersion}")

    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.10")

    implementation("com.fifesoft:rsyntaxtextarea:3.2.0")
    implementation(compose.desktop.currentOs)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:$coroutineVersion")
    implementation("org.jetbrains.compose.material:material-icons-extended-desktop:1.1.1")
    implementation("com.michael-bull.kotlin-result:kotlin-result-jvm:1.1.12")

    implementation("com.qxdzbc.p6:p6-proto:${p6Version}")
    implementation("com.qxdzbc.p6:p6-antlr:${p6Version}")

//    implementation("com.qxdzbc:common:${p6Version}")
//    implementation("com.qxdzbc:err:${p6Version}")
//    implementation("com.qxdzbc:common-compose:${p6Version}")

    implementation(project(":err"))
    implementation(project(":common"))
    implementation(project(":common-compose"))
    implementation(project(":common-test"))

    implementation("org.slf4j:slf4j-api:1.7.36")
    implementation("org.apache.logging.log4j:log4j-api:2.17.2")
    implementation("org.apache.logging.log4j:log4j-core:2.17.2")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.17.2")

    // jackson for log4j2 only.
    implementation("com.fasterxml.jackson.core:jackson-core:2.13.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.2")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.13.2")

    implementation("org.testng:testng:7.1.0")

    implementation("com.google.dagger:dagger:${dagger2Version}")
    kapt("com.google.dagger:dagger-compiler:${dagger2Version}")
    kaptTest("com.google.dagger:dagger-compiler:${dagger2Version}")

    implementation("com.google.protobuf:protobuf-java:$protocolBufferVersion")
    implementation("com.google.protobuf:protobuf-kotlin:$protocolBufferVersion")

    implementation("io.grpc:grpc-kotlin-stub:$kotlinGRPCVersion")
    implementation("io.grpc:grpc-stub:${grpcVersion}")
    implementation("io.grpc:grpc-protobuf:${grpcVersion}")
    implementation("io.grpc:grpc-netty-shaded:${grpcVersion}")
    testImplementation("io.grpc:grpc-testing:${grpcVersion}")

    testImplementation("org.mockito:mockito-core:${mockitoVersion}")
    testImplementation("org.mockito:mockito-inline:${mockitoVersion}")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")

    testImplementation("io.mockk:mockk:1.12.3")
    testImplementation("org.jetbrains.compose.ui:ui-test-junit4:${composeTestVersion}")
    testImplementation("org.jetbrains.compose.ui:ui-test-junit4-desktop:${composeTestVersion}")

    testImplementation("junit:junit:4.13.2")
    testImplementation(kotlin("test"))

    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")

}

tasks.test {
    // this is for Junit5
//    useJUnitPlatform()
    // this is for Junit4
    useJUnit()
    testLogging.showStandardStreams = true
}


val p6AppBuildDir = "p6App"
fun getBinaryDir():String?{
    val linuxBuildDir = "${p6AppBuildDir}/main/app/p6/bin"
    val windowBuildDir = p6AppBuildDir
    var binDir:String? = null
    if(os.isLinux){
        binDir = linuxBuildDir
    }
    if(os.isWindows){
        binDir = "${p6AppBuildDir}"
    }
    return binDir
}

fun getDistDir():String?{
    val linuxBuildDir = "${p6AppBuildDir}/main/app/p6/"
    val windowBuildDir = p6AppBuildDir
    var binDir:String? = null
    if(os.isLinux){
        binDir = linuxBuildDir
    }
    if(os.isWindows){
        binDir = "${p6AppBuildDir}"
    }
    return binDir
}

val os = OperatingSystem.current();

tasks.register<Copy>("copyP6PythonEnv") {
    from(layout.projectDirectory.dir("p6Env"))
    val binDir = getBinaryDir()
    if(binDir!=null){
        into(layout.buildDirectory.dir("${binDir}/p6Env"))
    }
}

/**
 * zip the p6 distribution into a zip file, store the zip file in build/dist
 */
tasks.register<Zip>("zipP6"){
    archiveFileName.set("p6App.zip")
    isZip64 = true // so that large folder can be zip
    destinationDirectory.set(layout.buildDirectory.dir("dist"))
    val distDir = getDistDir()
    if(distDir!=null){
        from(layout.buildDirectory.dir(distDir))
    }
}

/**
 * copy the jp config file into the distribution dir, right next to the execution file
 */
tasks.register<Copy>("copyJpConfig") {
    from(layout.projectDirectory.file("p6PythonConfig.json"))
    val binDir = getBinaryDir()
    if(binDir!=null){
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
    finalizedBy("copyJpConfig","copyP6PythonEnv")
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
