package com.qxdzbc.p6.gradle_plugins

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

object Utils {
    const val testImplementation = "testImplementation"
    const val implementation = "implementation"
    const val kapt = "kapt"
    const val kaptTest="kaptTest"
    const val versionCatalogName = "libs"

    fun Project.getVersionCatalog(): VersionCatalog {
        val l = this.extensions.getByType<VersionCatalogsExtension>().named(versionCatalogName)
        return l
    }

    fun VersionCatalog.dep(s: String) = findLibrary(s).get()
    fun VersionCatalog.plugin(s:String) = findPlugin(s).get()
    fun VersionCatalog.bundle(s:String) = findBundle(s).get()
}