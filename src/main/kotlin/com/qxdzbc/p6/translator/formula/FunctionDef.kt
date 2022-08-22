package com.qxdzbc.p6.translator.formula

import com.qxdzbc.p6.translator.formula.execution_unit.ExecutionWay
import kotlin.reflect.KFunction

interface FunctionDef {
    val name: String
    val function: KFunction<Any>
    val executionWay:ExecutionWay?
}

abstract class AbstractFunctionDef : FunctionDef{
    override val executionWay: ExecutionWay? = null
}
