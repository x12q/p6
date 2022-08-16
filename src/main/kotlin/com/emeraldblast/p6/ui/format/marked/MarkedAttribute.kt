package com.emeraldblast.p6.ui.format.marked

import com.emeraldblast.p6.ui.format.FormatAttribute

interface MarkedAttribute {
    val attr: FormatAttribute
    val isValid: Boolean
    val isNotValid: Boolean
    val refCounter: Int
    fun switch(): MarkedAttribute
    fun invalidate(): MarkedAttribute
    fun upCounter(): MarkedAttribute = changeCounterBy(1)
    fun downCounter(): MarkedAttribute = changeCounterBy(-1)
    fun changeCounterBy(v:Int):MarkedAttribute
    val isCounterZero: Boolean get() = refCounter == 0
    val isCounterNotZero: Boolean get() = refCounter != 0
}
