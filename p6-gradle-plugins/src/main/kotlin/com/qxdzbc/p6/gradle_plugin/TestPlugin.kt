package com.qxdzbc.p6.gradle_plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.kotlin
import com.qxdzbc.p6.gradle_plugin.CF.testImplementation

class TestPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target){
            dependencies {

                val l = extensions.getByType<VersionCatalogsExtension>().named("libs")
                fun libs(lib:String){
                    l.findLibrary(lib)
                }
                testImplementation(libs("libs.mockito.core"))
                testImplementation(libs("libs.mockito.inline"))
                testImplementation(libs("libs.mockito.kotlin"))
                testImplementation(libs("mockk"))
                testImplementation(libs("compose.ui.test.junit4"))
                testImplementation(libs("compose.ui.test.junit4.desktop"))
                testImplementation(libs("junit4"))
                testImplementation(libs("kotest.assertions.core"))
                testImplementation(kotlin("test"))
            }
        }
    }
}