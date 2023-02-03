package com.qxdzbc.p6.translator

import com.qxdzbc.common.Rse

interface P6Translator<T> {
    fun translate(formula:String): Rse<T>
}
