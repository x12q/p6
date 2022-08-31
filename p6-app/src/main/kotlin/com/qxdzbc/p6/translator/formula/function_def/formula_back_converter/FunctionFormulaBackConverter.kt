package com.qxdzbc.p6.translator.formula.function_def.formula_back_converter

import androidx.compose.ui.text.AnnotatedString
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.ui.common.color_generator.ColorProvider
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit

/**
 * Detail how to convert a function ExUnit back to a formula
 */
interface FunctionFormulaBackConverter {
    fun toFormula(u: ExUnit.Func): String?
    fun toFormulaSelective(u: ExUnit.Func, wbKey: WorkbookKey? = null, wsName: String? = null): String?
    fun toColorFormula(u: ExUnit.Func, colorProvider: ColorProvider, wbKey: WorkbookKey? = null, wsName: String? = null): AnnotatedString?
}
