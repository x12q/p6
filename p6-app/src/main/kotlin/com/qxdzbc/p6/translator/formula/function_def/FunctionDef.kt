package com.qxdzbc.p6.translator.formula.function_def

import com.qxdzbc.p6.translator.formula.execution_unit.FunctionExecutor
import com.qxdzbc.p6.translator.formula.function_def.formula_back_converter.FunctionFormulaBackConverter
import kotlin.reflect.KFunction

/**
 * A function definition
 */
interface FunctionDef {
    val name: String
    val function: KFunction<Any>
    val functionExecutor:FunctionExecutor?
    val functionFormulaConverter: FunctionFormulaBackConverter
}
