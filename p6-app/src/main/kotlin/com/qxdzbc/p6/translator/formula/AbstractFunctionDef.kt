package com.qxdzbc.p6.translator.formula

import com.qxdzbc.p6.translator.formula.execution_unit.ExecutionWay

abstract class AbstractFunctionDef : FunctionDef {
    override val executionWay: ExecutionWay? = null
}
