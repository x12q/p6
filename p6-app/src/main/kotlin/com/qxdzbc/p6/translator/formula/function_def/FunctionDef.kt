package com.qxdzbc.p6.translator.formula.function_def

import com.qxdzbc.p6.translator.formula.execution_unit.FunctionExecutor
import kotlin.reflect.KFunction

/**
 * A formula function definition
 */
interface FunctionDef {
    val name: String
    val function: KFunction<Any>
    val functionExecutor:FunctionExecutor
}
