package com.qxdzbc.p6.gradle_plugins

import com.qxdzbc.p6.gradle_plugins.Utils.dep
import com.qxdzbc.p6.gradle_plugins.Utils.getVersionCatalog
import com.qxdzbc.p6.gradle_plugins.Utils.implementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class LogPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val libs = target.getVersionCatalog()
        target.dependencies {
            implementation(libs.dep("slf4j.api"))
            implementation(libs.dep("log4j.api"))
            implementation(libs.dep("log4j.core"))
            implementation(libs.dep("log4j.slf4j.impl"))
            implementation(libs.dep("jackson.core"))
            implementation(libs.dep("jackson.databind"))
            implementation(libs.dep("jackson.annotations"))
            implementation(libs.dep("testng"))
        }
    }
}