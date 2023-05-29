package com.qxdzbc.p6.gradle_plugins
import com.qxdzbc.p6.gradle_plugins.Utils.dep
import org.gradle.api.Plugin
import org.gradle.api.Project
import com.qxdzbc.p6.gradle_plugins.Utils.getVersionCatalog
import com.qxdzbc.p6.gradle_plugins.Utils.implementation
import com.qxdzbc.p6.gradle_plugins.Utils.kapt
import com.qxdzbc.p6.gradle_plugins.Utils.kaptTest
import org.gradle.kotlin.dsl.dependencies

/**
 * A plugin that add dagger and anvil dependencies and plugin to a project
 */
class DaggerAnvilPlugin :Plugin<Project>{
    override fun apply(target: Project) {
        // TODO find a way to apply external anvil plugin here
        val libs = target.getVersionCatalog()
        target.dependencies {
            implementation(libs.dep("dagger"))
            kapt(libs.dep("dagger-compiler"))
            kaptTest(libs.dep("dagger-compiler"))
        }
    }
}

