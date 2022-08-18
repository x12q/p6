package com.emeraldblast.p6.translator

import com.emeraldblast.p6.app.common.utils.Rs
import com.emeraldblast.p6.common.exception.error.ErrorReport

interface P6Translator<T> {
    fun translate(formula:String): Rs<T, ErrorReport>
}
