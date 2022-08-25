package com.qxdzbc.p6.translator.formula.function_def.formula_back_converter

import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit

/**
 * Detail how to convert a function ExUnit back to a formula
 */
interface FunctionFormulaConverter{
    fun toFormula(u: ExUnit.Func):String?
}
