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

kotlin{
    java{
        withSourcesJar()
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
