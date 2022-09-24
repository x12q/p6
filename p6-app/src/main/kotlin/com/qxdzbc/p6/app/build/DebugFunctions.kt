package com.qxdzbc.p6.app.build

import androidx.compose.ui.graphics.Color

object DebugFunctions {
    fun Color.debug(productionColor: Color = Color.Transparent): Color {
        if (BuildConfig.buildVariant == BuildVariant.DEBUG) {
            return this
        } else {
            return productionColor
        }
    }
    fun Boolean.debugTrue():Boolean{
        if(BuildConfig.buildVariant == BuildVariant.DEBUG){
            return true
        }else{
            return this
        }
    }
    fun Boolean.debugFalse():Boolean{
        if(BuildConfig.buildVariant == BuildVariant.DEBUG){
            return false
        }else{
            return this
        }
    }
}
