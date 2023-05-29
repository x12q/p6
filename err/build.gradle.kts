//import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kv = libs.versions.kotlinVersion.get()
    kotlin("jvm") version kv
    kotlin("kapt") version kv
    `maven-publish`
    id("com.qxdzbc.p6.gradle_plugins.common_project_plugin")
}
val id = "err"
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

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

kotlin{
    java{
        withSourcesJar()
    }
}

dependencies{
    implementation("com.michael-bull.kotlin-result:kotlin-result-jvm:1.1.12")
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
