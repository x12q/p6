package com.qxdzbc.p6.translator.formula.formula_back_converter

import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit

class FunctionFormulaConverterForNormalFunction : FunctionFormulaConverter {
    override fun toFormula(u: ExUnit.Func): String {
        val argsStr = u.args.map { it.toFormula() }.filterNotNull().joinToString(", ")
        return "${u.funcName}(${argsStr})"
    }
}
