package com.qxdzbc.p6.app.build

import androidx.compose.ui.text.toUpperCase

/**
 * Contain build config
 * TODO see if I can wire this into the build system
 */
object BuildConfig {

//    val buildVariant:BuildVariant get(){
//        val v = System.getenv("P6_BUILD_VARIANT")?.uppercase()?.trim()
//        return when(v){
//            "DEBUG"->BuildVariant.DEBUG
//            "PRODUCTION"->BuildVariant.PRODUCTION
//            else -> BuildVariant.PRODUCTION
//        }
//    }
    val buildVariant = BuildVariant.DEBUG
}
