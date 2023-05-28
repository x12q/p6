package com.qxdzbc.p6.gradle_plugins

import com.qxdzbc.p6.gradle_plugins.Utils.dep
import com.qxdzbc.p6.gradle_plugins.Utils.bundle
import com.qxdzbc.p6.gradle_plugins.Utils.getVersionCatalog
import com.qxdzbc.p6.gradle_plugins.Utils.implementation
import com.qxdzbc.p6.gradle_plugins.Utils.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

/**
 * Consist of the basic test library, assertion library, and result library
 */
class CommonProjectPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val libs = target.getVersionCatalog()
        // TODO run TestPlugin inside this plugin
        target.dependencies {
            // TODO replace these with a bundle in toml
            testImplementation(libs.dep("kotest.assertions.core"))
            testImplementation(kotlin("test"))
            testImplementation(libs.dep("mockito.core"))
            testImplementation(libs.dep("mockito.inline"))
            testImplementation(libs.dep("mockito.kotlin"))
            testImplementation(libs.dep("mockk"))
            testImplementation(libs.dep("compose.ui.test.junit4"))
            testImplementation(libs.dep("compose.ui.test.junit4.desktop"))
            testImplementation(libs.dep("junit4"))
            implementation(libs.dep("michaelbull.kotlinResult"))
        }
    }
}