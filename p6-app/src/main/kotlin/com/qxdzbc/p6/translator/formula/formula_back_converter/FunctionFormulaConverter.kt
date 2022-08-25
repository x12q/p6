package com.qxdzbc.p6.translator.formula.formula_back_converter

import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit

/**
 * Detail how to convert a function ExUnit to formula
 */
interface FunctionFormulaConverter{
    fun toFormula(u: ExUnit.Func):String?
}
