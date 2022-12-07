package com.qxdzbc.p6.ui.format.marked

import com.qxdzbc.p6.ui.format.FormatAttribute

/**
 * A marked attribute is an attribute that is marked with additional information such as:
 *  - validity
 *  - reference counter
 *  this data is for managing attributes inside attribute table
 */
interface MarkedAttribute<T> {
    val attr: FormatAttribute<T>
    val refCount: Int
    fun upCounter(): MarkedAttribute<T> = changeCounterBy(1)
    fun downCounter(): MarkedAttribute<T> = changeCounterBy(-1)
    fun changeCounterBy(v:Int):MarkedAttribute<T>
    val isCounterZero: Boolean get() = refCount == 0
    val isCounterNotZero: Boolean get() = refCount != 0
}
