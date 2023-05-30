package com.qxdzbc.p6.translator.formula.function_def

import com.qxdzbc.p6.translator.formula.execution_unit.function.FunctionExecutor
import kotlin.reflect.KFunction

/**
 * A class containing the definition of a function, and an executor that can run that function.
 */
interface FunctionDef {
    val name: String
    val function: KFunction<Any>
    val functionExecutor: FunctionExecutor
}
