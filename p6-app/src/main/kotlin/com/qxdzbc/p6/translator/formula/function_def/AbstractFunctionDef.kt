package com.qxdzbc.p6.translator.formula.function_def

import com.qxdzbc.p6.translator.formula.execution_unit.FunctionExecutor

abstract class AbstractFunctionDef : FunctionDef {
    override val functionExecutor: FunctionExecutor = FunctionExecutor.ArgsAsArray
}
