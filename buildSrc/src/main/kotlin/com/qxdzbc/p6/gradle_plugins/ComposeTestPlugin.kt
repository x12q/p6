package com.qxdzbc.p6.gradle_plugins

import com.qxdzbc.p6.gradle_plugins.Utils.dep
import com.qxdzbc.p6.gradle_plugins.Utils.getVersionCatalog
import com.qxdzbc.p6.gradle_plugins.Utils.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Add dependencies for
 * - compose unit test 4
 * - compose desktop test junit 4
 */
class ComposeTestPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val libs = target.getVersionCatalog()
        target.dependencies {
            testImplementation(libs.dep("compose.ui.test.junit4"))
            testImplementation(libs.dep("compose.ui.test.junit4.desktop"))
        }
    }
}