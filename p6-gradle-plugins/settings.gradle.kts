rootProject.name = "p6-gradle-plugins"

dependencyResolutionManagement{
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs{
        create("libs"){
            from(files("../libs.versions.toml"))
        }
    }
}