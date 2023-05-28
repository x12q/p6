package com.qxdzbc.p6.gradle_plugins

import com.qxdzbc.p6.gradle_plugins.Utils.dep
import com.qxdzbc.p6.gradle_plugins.Utils.getVersionCatalog
import com.qxdzbc.p6.gradle_plugins.Utils.implementation
import com.qxdzbc.p6.gradle_plugins.Utils.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * A plugin that add grpc and protocol buffer dependencies
 */
class GrpcPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val libs = target.getVersionCatalog()
        target.dependencies {
/*
protobuf-java = { module = "com.google.protobuf:protobuf-java", version.ref = "protocolBufferVersion" }
protobuf-kotlin = { module = "com.google.protobuf:protobuf-kotlin", version.ref = "protocolBufferVersion" }
grpc-kotlin-stub = { module = "io.grpc:grpc-kotlin-stub", version.ref = "kotlinGRPCVersion" }
grpc-stub = { module = "io.grpc:grpc-stub", version.ref = "grpcVersion" }
grpc-protobuf = { module = "io.grpc:grpc-protobuf", version.ref = "grpcVersion" }
grpc-netty-shaded = { module = "io.grpc:grpc-netty-shaded", version.ref = "grpcVersion" }
grpc-testing = { module = "io.grpc:grpc-testing", version.ref = "grpcVersion" }

 */
            implementation(libs.dep("protobuf.java"))
            implementation(libs.dep("protobuf.kotlin"))
            implementation(libs.dep("grpc.kotlin.stub"))
            implementation(libs.dep("grpc.stub"))
            implementation(libs.dep("grpc.protobuf"))
            implementation(libs.dep("grpc.netty.shaded"))
            testImplementation(libs.dep("grpc.testing"))
        }
    }
}