//import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
//    kotlin("jvm") version "1.6.10"
//    kotlin("kapt") version "1.6.10"

    kotlin("jvm") version "1.7.20"
    kotlin("kapt") version "1.7.20"
    id("maven-publish")
    idea
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
val kotestVersion="5.5.4"
val mockitoVersion = "4.5.1"
dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    implementation(kotlin("stdlib-jdk8"))

    testImplementation("org.mockito:mockito-core:${mockitoVersion}")
    testImplementation("org.mockito:mockito-inline:${mockitoVersion}")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")

    testImplementation("io.mockk:mockk:1.12.3")
    testImplementation(kotlin("test"))
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

java {
    sourceCompatibility = JavaVersion.VERSION_15
    targetCompatibility = JavaVersion.VERSION_15
    withSourcesJar()
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
