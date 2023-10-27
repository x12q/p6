import com.google.protobuf.gradle.*
plugins {
    kotlin("jvm") version "1.6.10"
    id("com.google.protobuf") version "0.8.18"
    id("maven-publish")
    java
}

group = "com.qxdzbc.p6"
version = "1.0"
repositories {
    mavenCentral()
    google()
    mavenCentral()
    mavenLocal()
    maven("https://plugins.gradle.org/m2/")

}
val grpcVersion = "1.54.0"
val protocolBufferVersion = "3.19.4"
val kotlinGRPCVersion = "1.2.1"
val protoCompilerVersion = "3.19.4"
protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${protoCompilerVersion}"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:$grpcVersion"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:$kotlinGRPCVersion:jdk7@jar"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("grpc")
                id("grpckt")
            }
            it.builtins {
                id("kotlin")
                id("python")
            }
        }
    }
}

dependencies {
    implementation("com.google.protobuf:protobuf-java:$protocolBufferVersion")
    implementation("com.google.protobuf:protobuf-kotlin:$protocolBufferVersion")

    implementation("io.grpc:grpc-kotlin-stub:$kotlinGRPCVersion")
    implementation("io.grpc:grpc-stub:${grpcVersion}")
    implementation ("io.grpc:grpc-protobuf:${grpcVersion}")
    testImplementation ("io.grpc:grpc-testing:${grpcVersion}")
}

//tasks.getByName<Test>("test") {
//    useJUnitPlatform()
//}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = group.toString()
            artifactId = "p6-proto"
            version = version

            from(components["java"])
        }
    }
}

java{
    targetCompatibility = JavaVersion.VERSION_15
    sourceCompatibility = JavaVersion.VERSION_15
    withSourcesJar()
}
tasks {
    val jvmVersion = "15"

    compileKotlin {
        kotlinOptions { jvmTarget = jvmVersion }
        sourceCompatibility = jvmVersion
    }
    compileJava{
        this.targetCompatibility = jvmVersion
    }
}
