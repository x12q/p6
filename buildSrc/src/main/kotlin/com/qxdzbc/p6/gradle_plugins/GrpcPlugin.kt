package com.qxdzbc.p6.gradle_plugins

import com.qxdzbc.p6.gradle_plugins.Utils.dep
import com.qxdzbc.p6.gradle_plugins.Utils.getVersionCatalog
import com.qxdzbc.p6.gradle_plugins.Utils.implementation
import com.qxdzbc.p6.gradle_plugins.Utils.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Add grpc and protocol buffer dependencies
 */
class GrpcPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val libs = target.getVersionCatalog()
        target.dependencies {
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