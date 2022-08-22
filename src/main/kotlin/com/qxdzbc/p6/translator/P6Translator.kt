package com.qxdzbc.p6.translator

import com.qxdzbc.p6.app.common.utils.Rs
import com.qxdzbc.p6.common.exception.error.ErrorReport

interface P6Translator<T> {
    fun translate(formula:String): Rs<T, ErrorReport>
}
