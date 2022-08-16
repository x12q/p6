package com.emeraldblast.p6.translator

import com.emeraldblast.p6.app.common.Rs
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.github.michaelbull.result.Result

interface P6Translator<T> {
    fun translate(formula:String): Rs<T, ErrorReport>
}
