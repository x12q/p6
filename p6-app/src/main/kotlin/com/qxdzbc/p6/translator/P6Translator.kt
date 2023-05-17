package com.qxdzbc.p6.translator

import com.qxdzbc.common.Rse

/**
 * Translate a formula in string to a [T] type
 */
interface P6Translator<T> {
    fun translate(formula:String): Rse<T>
}
