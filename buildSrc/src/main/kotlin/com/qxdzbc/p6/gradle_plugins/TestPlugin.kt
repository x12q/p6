package com.qxdzbc.p6.gradle_plugins

import com.qxdzbc.p6.gradle_plugins.Utils.getVersionCatalog
import com.qxdzbc.p6.gradle_plugins.Utils.testImplementation
import com.qxdzbc.p6.gradle_plugins.Utils.dep
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

/**
 * Plugin for setting up dependencies for testing, including:
 * - junit 4
 * - kotlin test
 * - mockito
 * - mockk
 * - kotest assertion
 */
class TestPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val libs = target.getVersionCatalog()
        target.dependencies {
            testImplementation(libs.dep("kotest.assertions.core"))
            testImplementation(kotlin("test"))
            testImplementation(libs.dep("mockito.core"))
            testImplementation(libs.dep("mockito.inline"))
            testImplementation(libs.dep("mockito.kotlin"))
            testImplementation(libs.dep("mockk"))
            testImplementation(libs.dep("junit4"))
        }
    }
}