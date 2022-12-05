package com.qxdzbc.p6.ui.format

import androidx.compose.ui.Modifier

object FormatAttributes{

    fun Collection<FormatAttribute>.mergeIntoModifier():Modifier{
        var rt: Modifier = Modifier
        for(attr in this){
            rt = rt.then(attr.modifier)
        }
        Modifier
        return rt
    }
}
