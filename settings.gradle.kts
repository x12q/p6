rootProject.name = "p6"

val p6Version by extra("1.0")


pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement{
    repositories {
        google()
        mavenCentral()
    }
//    val kotlinVersion = "1.7.20"
//    versionCatalogs{
//        create("libs"){
//            from(files("./gradle/libs.versions.toml"))
//
//            // === for build plugin
////            library("kotlin-gradlePlugin","org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
////
////            plugin("kotlin-jvm","org.jetbrains.kotlin.jvm").apply {
////                this.version(kotlinVersion)
////            }
//
//        }
//
//    }
}


include("common-compose","p6-app")
include("err")
include("common")
include("p6-antlr")
include("common-test")
