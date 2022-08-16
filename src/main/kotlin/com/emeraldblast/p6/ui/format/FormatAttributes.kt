package com.emeraldblast.p6.ui.format

import androidx.compose.ui.Modifier

object FormatAttributes{
    fun merge(attrs:Collection<FormatAttribute>): Modifier {
        var rt: Modifier = Modifier
        for(attr in attrs){
            rt = rt.then(attr.modifier)
        }
        Modifier
        return rt
    }
}
