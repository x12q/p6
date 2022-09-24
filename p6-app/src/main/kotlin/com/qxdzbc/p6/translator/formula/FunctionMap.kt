package com.qxdzbc.p6.translator.formula

import com.qxdzbc.common.error.ErrorReport
import com.github.michaelbull.result.Result
import com.qxdzbc.p6.translator.formula.function_def.FunctionDef

/**
 * A mapping of function name to function definition
 */
interface FunctionMap : Map<String, FunctionDef> {
    fun getFunc(name: String): FunctionDef?
    fun getFuncRs(name: String): Result<FunctionDef,ErrorReport>
    fun addFunc(name:String, func: FunctionDef): FunctionMap
    fun removeFunc(name:String): FunctionMap
}
