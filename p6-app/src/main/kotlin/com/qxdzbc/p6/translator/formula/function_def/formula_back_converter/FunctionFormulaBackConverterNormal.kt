package com.qxdzbc.p6.translator.formula.function_def.formula_back_converter

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.ui.common.color_generator.ColorProvider
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import javax.inject.Inject

class FunctionFormulaBackConverterNormal @Inject constructor() : FunctionFormulaBackConverter {
    override fun toFormula(u: ExUnit.Func): String {
        val argsStr = u.args.map { it.toFormula() }.filterNotNull().joinToString(", ")
        return "${u.funcName}(${argsStr})"
    }

    override fun toFormulaSelective(u: ExUnit.Func, wbKey: WorkbookKey?, wsName: String?): String? {
        val argsStr = u.args.map { it.toFormulaSelective(wbKey, wsName) }.filterNotNull().joinToString(", ")
        return "${u.funcName}(${argsStr})"
    }

    override fun toColorFormula(
        u: ExUnit.Func,
        colorProvider: ColorProvider,
        wbKey: WorkbookKey?,
        wsName: String?
    ): AnnotatedString {
        val argsStr = u.args.map { it.toColorFormula(colorProvider,wbKey, wsName) }.filterNotNull()
        val rt= buildAnnotatedString {
            append(u.funcName)
            append("(")
            argsStr.forEach {
                append(it)
                append(",")
            }
            append(")")
        }
        return rt
    }
}
