package com.qxdzbc.p6.ui.format.marked

import com.qxdzbc.p6.ui.format.FormatAttribute

/**
 * A marked attribute is an attribute that is marked with additional information such as:
 *  - validity
 *  - reference counter
 *  this data is for managing attributes inside attribute table
 */
interface MarkedAttribute {
    val attr: FormatAttribute
    val isValid: Boolean
    val isNotValid: Boolean
    val refCounter: Int
    fun switch(): MarkedAttribute
    fun invalidate(): MarkedAttribute
    fun validate():MarkedAttribute
    fun upCounter(): MarkedAttribute = changeCounterBy(1)
    fun downCounter(): MarkedAttribute = changeCounterBy(-1)
    fun changeCounterBy(v:Int):MarkedAttribute
    val isCounterZero: Boolean get() = refCounter == 0
    val isCounterNotZero: Boolean get() = refCounter != 0
}
