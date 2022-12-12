plugins {
    java
    kotlin("jvm") version "1.7.20"
    kotlin("kapt") version "1.7.20"
    id("maven-publish")
}

val id = "common-test"
group = "com.qxdzbc"
version = "1.0"

repositories {
    mavenCentral()
    google()
}
val kotestVersion="5.5.4"
dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    testImplementation(kotlin("test"))
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")

}

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
