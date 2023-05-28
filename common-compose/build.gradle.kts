import org.jetbrains.compose.compose

plugins {
    val kv = libs.versions.kotlinVersion.get()
    kotlin("jvm") version kv
    alias(libs.plugins.jetbrain.compose)
    `maven-publish`
    id("com.qxdzbc.p6.gradle_plugins.common_project_plugin")
}

java {
    sourceCompatibility = JavaVersion.VERSION_15
    targetCompatibility = JavaVersion.VERSION_15
    withSourcesJar()
}

val id="common-compose"
group = "com.qxdzbc"
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
val coroutineVersion = "1.6.1"
val mockitoVersion = "4.5.1"
val p6Version = "1.0"
val kotestVersion="5.5.4"
dependencies {
    implementation(compose.desktop.currentOs)
    implementation(project(":err"))
    implementation(project(":common"))
    testImplementation(project(":common-test"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
tasks {
    val jvmVersion = "15"
    compileKotlin {
        kotlinOptions { jvmTarget = jvmVersion }
//        sourceCompatibility = jvmVersion
    }
}

tasks.test {
    useJUnit()
    testLogging.showStandardStreams = true
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = group.toString()
            artifactId = id
            version = version

            from(components["java"])
        }
    }
}
