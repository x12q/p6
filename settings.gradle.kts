rootProject.name = "p6"

val p6Version by extra("1.0")


pluginManagement {
    includeBuild("p6-gradle-plugins")
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
    val kotlinVersion = "1.7.20"
    versionCatalogs{
        create("libs"){
            from(files("./libs.versions.toml"))

            // === not for app
            library("kotlin-gradlePlugin","org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")

            plugin("kotlin-jvm","org.jetbrains.kotlin.jvm").apply {
                this.version(kotlinVersion)
            }

        }

    }
}


include("common-compose","p6-app")
include("err")
include("common")
include("p6-antlr")
include("common-test")
//include("p6-gradle-plugins")
//include("p6-gradle-plugins:plugin")
//findProject(":p6-gradle-plugins:plugin")?.name = "plugin"
