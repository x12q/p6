package com.emeraldblast.p6.translator.formula

import com.emeraldblast.p6.common.exception.error.ErrorReport
import kotlin.reflect.KFunction
import com.github.michaelbull.result.Result

/**
 * A mapping of function name to function definition
 */
interface FunctionMap : Map<String,FunctionDef> {
    fun getFunc(name: String): FunctionDef?
    fun getFuncRs(name: String): Result<FunctionDef,ErrorReport>
    fun addFunc(name:String, func: FunctionDef): FunctionMap
    fun removeFunc(name:String): FunctionMap
}
