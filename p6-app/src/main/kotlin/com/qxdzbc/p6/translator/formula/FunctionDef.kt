package com.qxdzbc.p6.translator.formula

import com.qxdzbc.p6.translator.formula.execution_unit.ExecutionWay
import kotlin.reflect.KFunction

/**
 * A function definition
 */
interface FunctionDef {
    val name: String
    val function: KFunction<Any>
    val executionWay:ExecutionWay?
}
