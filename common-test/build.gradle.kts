plugins {
    val kv = libs.versions.kotlinVersion.get()
    kotlin("jvm") version kv
    kotlin("kapt") version kv
    `maven-publish`
    id("com.qxdzbc.p6.gradle_plugins.common_project_plugin")
}

val id = "common-test"
group = "com.qxdzbc"
version = "1.0"

repositories {
    mavenCentral()
    google()
}
val kotestVersion="5.5.4"
dependencies {}

java {
    sourceCompatibility = JavaVersion.VERSION_15
    targetCompatibility = JavaVersion.VERSION_15
    withSourcesJar()
}
tasks {
    compileKotlin {
        kotlinOptions { jvmTarget = "15" }
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
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
