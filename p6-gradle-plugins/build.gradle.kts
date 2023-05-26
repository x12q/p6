
plugins {
    `kotlin-dsl`
}

group = "com.qxdzbc.p6.gradle.plugins"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
//    gradleApi()
//    compileOnly(libs.kotlin.gradlePlugin)
//
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.20")
}
gradlePlugin {
    plugins {
        register("testPlugin") {
            id = "com.qxdzbc.p6.gradle_plugin.test_plugin"
            implementationClass = "com.qxdzbc.p6.gradle_plugin.TestPlugin"
        }
    }
}
