package com.qxdzbc.p6.translator.formula

import com.qxdzbc.p6.translator.formula.execution_unit.FunctionExecutor

abstract class AbstractFunctionDef : FunctionDef {
    override val functionExecutor: FunctionExecutor? = null
}
