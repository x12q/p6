import org.jetbrains.compose.compose

plugins {
    java
//    kotlin("jvm") version "1.6.10"

    kotlin("jvm") version "1.7.20"
    id("org.jetbrains.compose") version "1.2.1"
    id("maven-publish")
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
//    implementation("com.qxdzbc:common:${p6Version}")
//    implementation("com.qxdzbc:err:${p6Version}")
    implementation(project(":err"))
    implementation(project(":common"))
    implementation("com.michael-bull.kotlin-result:kotlin-result-jvm:1.1.12")
    testImplementation("org.mockito:mockito-core:${mockitoVersion}")
    testImplementation("org.mockito:mockito-inline:${mockitoVersion}")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
    testImplementation("junit:junit:4.13.2")
    testImplementation(kotlin("test"))
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
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
