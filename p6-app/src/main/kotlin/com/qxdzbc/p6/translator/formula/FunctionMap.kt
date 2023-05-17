package com.qxdzbc.p6.translator.formula

import com.qxdzbc.common.Rse
import com.qxdzbc.p6.translator.formula.function_def.FunctionDef

/**
 * A mapping of formula function names to function definitions
 */
interface FunctionMap : Map<String, FunctionDef> {
    fun getFunc(name: String): FunctionDef?
    fun getFuncRs(name: String): Rse<FunctionDef>
    fun addFunc(name: String, func: FunctionDef): FunctionMap
    fun removeFunc(name: String): FunctionMap
}
