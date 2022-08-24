package com.qxdzbc.p6.translator

import com.qxdzbc.common.Rs
import com.qxdzbc.common.error.ErrorReport

interface P6Translator<T> {
    fun translate(formula:String): Rs<T, ErrorReport>
}
