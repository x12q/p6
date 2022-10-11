package com.qxdzbc.p6.translator

import com.qxdzbc.common.Rs
import com.qxdzbc.common.error.ErrorReport
import org.antlr.v4.runtime.tree.ParseTree

interface P6Translator<T> {
    fun translate(formula:String): Rs<T, ErrorReport>
}
