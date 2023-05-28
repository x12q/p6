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
 * TODO add plugin
 */
class DaggerAnvilPlugin :Plugin<Project>{
    override fun apply(target: Project) {
        val l = target.getVersionCatalog()
//        with(target.plugins){
//            val q = l.findPlugin("anvil").get().get()

//            println(q)
//            apply("${q.get().pluginId}:${q.get().version}")
//            this.apply("com.squareup.anvil:gradle-plugin:2.4.2") // => not working
//            this.apply("com.squareup.anvil") //not working

//        }
//        target.plugins {
//            alias(l.plugin("anvil"))
//            kotlin("kapt")
//        }

        target.dependencies {
            implementation(l.dep("dagger"))
            kapt(l.dep("dagger-compiler"))
            kaptTest(l.dep("dagger-compiler"))
        }
    }
}

