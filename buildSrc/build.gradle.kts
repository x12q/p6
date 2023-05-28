plugins {
    `kotlin-dsl` // this is for building gradle plugin in kotlin
}

group = "com.qxdzbc.p6.gradle_plugins"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
}

gradlePlugin {
    plugins {
        create("TestPlugin") {
            id = "com.qxdzbc.p6.gradle_plugins.test_plugin"
            implementationClass = "com.qxdzbc.p6.gradle_plugins.TestPlugin"
        }
        create("DaggerAnvilPlugin") {
            id = "com.qxdzbc.p6.gradle_plugins.dagger_anvil_plugin"
            implementationClass = "com.qxdzbc.p6.gradle_plugins.DaggerAnvilPlugin"
        }

        create("LogPlugin") {
            id = "com.qxdzbc.p6.gradle_plugins.log_plugin"
            implementationClass = "com.qxdzbc.p6.gradle_plugins.LogPlugin"
        }
        create("GrpcPlugin") {
            id = "com.qxdzbc.p6.gradle_plugins.grpc_plugin"
            implementationClass = "com.qxdzbc.p6.gradle_plugins.GrpcPlugin"
        }
        create("CommonProjectPlugin") {
            id = "com.qxdzbc.p6.gradle_plugins.common_project_plugin"
            implementationClass = "com.qxdzbc.p6.gradle_plugins.CommonProjectPlugin"
        }
    }
}
