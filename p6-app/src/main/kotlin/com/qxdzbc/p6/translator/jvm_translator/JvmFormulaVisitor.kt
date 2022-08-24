package com.qxdzbc.p6.translator.jvm_translator

import androidx.compose.runtime.getValue
import com.qxdzbc.common.path.PPaths
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.address.CellAddresses
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddresses
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.translator.formula.FunctionMap
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.qxdzbc.p6.formula.translator.antlr.FormulaBaseVisitor
import com.qxdzbc.p6.formula.translator.antlr.FormulaParser
import com.qxdzbc.p6.translator.formula.P6FunctionDefinitions
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit.Companion.exUnit
import com.qxdzbc.common.compose.St
import com.github.michaelbull.result.Ok
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import org.antlr.v4.runtime.tree.ParseTree
import java.nio.file.Path

class JvmFormulaVisitor @AssistedInject constructor(
    @Assisted("1") private val wbKeySt: St<WorkbookKey>,
    @Assisted("2") private val wsNameSt: St<String>,
    private val functionMap: FunctionMap
) : FormulaBaseVisitor<ExUnit>() {

    private val wbKey: WorkbookKey by wbKeySt
    private val wsName: String by wsNameSt

    private val wbKeyExUnit = wbKey.exUnit()
    private val wsNameExUnit = wsName.exUnit()

    override fun visit(tree: ParseTree?): ExUnit? {
        return super.visit(tree)
    }

    override fun visitZFormula(ctx: FormulaParser.ZFormulaContext?): ExUnit? {
        val rt = ctx?.expr()?.let { this.visit(it) }
        return rt
    }

    override fun visitFunCall(ctx: FormulaParser.FunCallContext?): ExUnit? {
        return ctx?.functionCall()?.let {
            this.visit(it)
        }
    }

    override fun visitLiteral(ctx: FormulaParser.LiteralContext?): ExUnit? {
        return ctx?.lit()?.let {
            this.visitLit(it)
        }
    }

    override fun visitUnSubExpr(ctx: FormulaParser.UnSubExprContext?): ExUnit? {
        val op = ctx?.op
        when (op?.type) {
            FormulaParser.SUB -> {
                val exUnit = ctx.expr()?.let { this.visit(it) }
                if (exUnit != null) {
                    val rt = ExUnit.UnarySubtract(exUnit)
                    return rt
                } else {
                    return null
                }
            }
        }
        return null
    }

    override fun visitParenExpr(ctx: FormulaParser.ParenExprContext?): ExUnit? {
        return this.visit(ctx?.expr())
    }

    override fun visitPowExpr(ctx: FormulaParser.PowExprContext): ExUnit? {
        val expr0 = this.visit(ctx.expr(0))
        val expr1 = this.visit(ctx.expr(1))
        if (expr0 != null && expr1 != null) {
            return ExUnit.PowerBy(expr0, expr1)
        } else {
            return null
        }
    }

    override fun visitMulDivModExpr(ctx: FormulaParser.MulDivModExprContext): ExUnit? {
        val expr0 = this.visit(ctx.expr(0))
        val expr1 = this.visit(ctx.expr(1))
        val op = ctx.op
        if (expr0 != null && expr1 != null) {
            when (op?.type) {
                FormulaParser.MUL -> {
                    return ExUnit.MultiplyOperator(expr0, expr1)
                }
                FormulaParser.DIV -> {
                    return ExUnit.Div(expr0, expr1)
                }
                else -> return null
            }
        } else {
            return null
        }
    }

    override fun visitAddSubExpr(ctx: FormulaParser.AddSubExprContext): ExUnit? {
        val expr0 = this.visit(ctx.expr(0))
        val expr1 = this.visit(ctx.expr(1))
        val op = ctx.op
        if (expr0 != null && expr1 != null) {
            when (op?.type) {
                FormulaParser.ADD -> {
                    return ExUnit.AddOperator(expr0, expr1)
                }
                FormulaParser.SUB -> {
                    return ExUnit.MinusOperator(expr0, expr1)
                }
                else -> return null
            }
        } else {
            return null
        }
    }
    //==

    override fun visitSheetPrefix(ctx: FormulaParser.SheetPrefixContext?): ExUnit? {
        val e1 = ctx?.sheetName()?.let { this.visitSheetName(it) }
        if(e1!=null){
            return e1
        }
        val e2 = ctx?.sheetNameWithSpace()?.let { this.visitSheetNameWithSpace(it) }
        return e2
    }

    override fun visitSheetNameWithSpace(ctx: FormulaParser.SheetNameWithSpaceContext?): ExUnit? {
        return ctx?.withSpaceId()?.let { this.visitWithSpaceId(it) }
    }

    override fun visitSheetName(ctx: FormulaParser.SheetNameContext?): ExUnit? {
        val exUnit = ctx?.noSpaceId()?.let { this.visitNoSpaceId(it) }
        return exUnit
    }

    override fun visitNoSpaceId(ctx: FormulaParser.NoSpaceIdContext?): ExUnit? {
        return ctx?.text?.exUnit()
    }

    override fun visitWithSpaceId(ctx: FormulaParser.WithSpaceIdContext?): ExUnit? {
        return ctx?.text?.let { extractFromSingleQuote(it) }?.exUnit()
    }

    //==
    override fun visitWbPrefix(ctx: FormulaParser.WbPrefixContext?): ExUnit? {
       val rt1=ctx?.wbPrefixNoPath()?.let { visitWbPrefixNoPath(it) }
        if (rt1 != null) {
            return rt1
        }
        val rt2 = ctx?.wbPrefixWithPath()?.let { visitWbPrefixWithPath(it) }
        return rt2
    }

    override fun visitWbPrefixNoPath(ctx: FormulaParser.WbPrefixNoPathContext?): ExUnit? {
        val wbNameExUnit = ctx?.wbName()?.let { this.visitWbName(it) }
        val wbName: String? = wbNameExUnit?.run()?.component1() as String?
        val wbk: WorkbookKey? = wbName?.let { WorkbookKey(it) }
        val rt = wbk?.exUnit()
        return rt
    }

    override fun visitWbPrefixWithPath(ctx: FormulaParser.WbPrefixWithPathContext?): ExUnit? {
        val wbNameExUnit = ctx?.wbName()?.let { this.visitWbName(it) }
        val wbName: String? = wbNameExUnit?.run()?.component1() as String?
        val wbk: WorkbookKey? = wbName?.let {
            val wbPathExUnit = ctx?.wbPath()?.let { this.visitWbPath(it) }
            val wbPath: Path? = (wbPathExUnit?.run()?.component1() as String?)?.let { Path.of(it) }
            WorkbookKey(wbName, wbPath)
        }
        val rt = wbk?.exUnit()
        return rt
    }
    //==

    override fun visitWbPath(ctx: FormulaParser.WbPathContext): ExUnit? {
        return extractFromSingleQuote(ctx.text).exUnit()
    }

    override fun visitWbName(ctx: FormulaParser.WbNameContext): ExUnit? {
        if (ctx.wbNameNoSpace() != null) {
            return this.visitWbNameNoSpace(ctx.wbNameNoSpace())
        } else {
            return this.visitWbNameWithSpace(ctx.wbNameWithSpace())
        }
    }

    override fun visitWbNameNoSpace(ctx: FormulaParser.WbNameNoSpaceContext): ExUnit? {
        return ctx.text.exUnit()
    }

    override fun visitWbNameWithSpace(ctx: FormulaParser.WbNameWithSpaceContext): ExUnit? {
        val text = ctx.text
        return extractFromSingleQuote(text).exUnit()
    }

    override fun visitFullRangeAddressExpr(ctx: FormulaParser.FullRangeAddressExprContext): ExUnit? {
        val wbExUnit = ctx.wbPrefix()?.let { this.visitWbPrefix(it) } ?: wbKeyExUnit
        val wsExUnit = ctx.sheetPrefix()?.let { this.visitSheetPrefix(it) }
        val rangeAddressContext = ctx.rangeAddress()
        val ra:String? = rangeAddressContext.text
        if(ra!=null && wsExUnit!=null){
            val cellAddressRs = CellAddresses.fromLabelRs(ra)
            if (cellAddressRs is Ok) {
                val raUnit = cellAddressRs.value.exUnit()
                return ExUnit.Func(
                    P6FunctionDefinitions.getCellRs,
                    args = listOf(wbExUnit, wsExUnit, raUnit),
                    functionMap
                )
            }

            val rangeAddressRs = RangeAddresses.fromLabelRs(ra)
            if (rangeAddressRs is Ok) {
                val raUnit = rangeAddressRs.value.exUnit()
                return ExUnit.Func(
                    P6FunctionDefinitions.getRangeRs,
                    args = listOf(wbExUnit, wsExUnit, raUnit),
                    functionMap
                )
            }
        }
        return null
    }

    override fun visitFunctionCall(ctx: FormulaParser.FunctionCallContext): ExUnit? {
        val functionName = ctx.functionName().text
        if (functionName != null) {
            val args = ctx.expr()?.map {
                this.visit(it)
            }?.filterNotNull() ?: emptyList()
            return ExUnit.Func(
                funcName = functionName,
                args = args,
                functionMap = functionMap
            )
        } else {
            return null
        }
    }

    override fun visitFunctionName(ctx: FormulaParser.FunctionNameContext): ExUnit? {
        return ctx.text?.let {
            ExUnit.StrUnit(it)
        }
    }

    override fun visitRangeAsPairCellAddress(ctx: FormulaParser.RangeAsPairCellAddressContext): ExUnit? {
        val cell0 = ctx.cellAddress(0).text
        val cell1 = ctx.cellAddress(1).text
        if (cell0 != null && cell1 != null) {
            val raUnit = RangeAddress(CellAddress(cell0), CellAddress(cell1)).exUnit()
            return ExUnit.Func(
                funcName = P6FunctionDefinitions.getRangeRs,
                args = listOf(wbKeyExUnit, wsNameExUnit, raUnit),
                functionMap = functionMap
            )
        } else {
            return null
        }
    }

    override fun visitRangeAsOneCellAddress(ctx: FormulaParser.RangeAsOneCellAddressContext): ExUnit.Func? {
        val cell0 = ctx.text
        if (cell0 != null) {
            val raUnit = CellAddress(cell0).exUnit()
            val rt = ExUnit.Func(
                funcName = P6FunctionDefinitions.getCellRs,
                args = listOf(wbKeyExUnit, wsNameExUnit, raUnit),
                functionMap = functionMap
            )
            return rt
        } else {
            return null
        }
    }

    override fun visitRangeAsColAddress(ctx: FormulaParser.RangeAsColAddressContext?): ExUnit? {
        val colAddress = ctx?.text
        if (colAddress != null) {
            val raUnit = RangeAddress(colAddress).exUnit()
            return ExUnit.Func(
                funcName = P6FunctionDefinitions.getRangeRs,
                args = listOf(wbKeyExUnit, wsNameExUnit, raUnit),
                functionMap = functionMap
            )
        } else {
            return null
        }
    }

    override fun visitRangeAsRowAddress(ctx: FormulaParser.RangeAsRowAddressContext?): ExUnit.Func? {
        val rowAddress = ctx?.text
        if (rowAddress != null) {
            val raUnit = RangeAddress(rowAddress).exUnit()
            return ExUnit.Func(
                funcName = P6FunctionDefinitions.getRangeRs,
                args = listOf(wbKeyExUnit, wsNameExUnit, raUnit),
                functionMap = functionMap
            )
        } else {
            return null
        }
    }

    override fun visitRangeInparens(ctx: FormulaParser.RangeInparensContext?): ExUnit? {
        return this.visit(ctx?.rangeAddress())
    }

    override fun visitLit(ctx: FormulaParser.LitContext): ExUnit? {
        val floatNode = ctx.FLOAT_NUMBER()
        val intNode = ctx.INT()
        val textNode = ctx.STRING()
        val boolNode = ctx.BOOLEAN()

        if (boolNode != null) {
            when (boolNode.text) {
                "TRUE" -> return ExUnit.TRUE
                "FALSE" -> return ExUnit.FALSE
            }
        }

        if (floatNode != null) {
            val doubleNum = floatNode.text.toDoubleOrNull()
            if (doubleNum != null) {
                return ExUnit.DoubleNum(doubleNum)
            }
        }
        if (intNode != null) {
            val i = intNode.text.toIntOrNull()
            if (i != null) {
                return ExUnit.IntNum(i)
            } else {
                val i2 = intNode.text.toDoubleOrNull()
                if (i2 != null) {
                    return ExUnit.DoubleNum(i2)
                }
            }
        }

        if (textNode != null) {
            val ot = textNode.text
            val t = ot?.substring(1, ot.length - 1)
            if (t != null) {
                return ExUnit.StrUnit(t)
            }
        }
        return null
    }

    /**
     * Extract sheet name from this format:
     * !'SheetName' -> SheetName
     * !SheetName -> SheetName
     */
    private fun extractSheetName(rawSheetName: String?): String {
        if (rawSheetName == null) {
            return ""
        }
        if (rawSheetName.isBlank() || rawSheetName.isEmpty()) {
            return ""
        } else {
            val rt = if (rawSheetName.startsWith('\'')) {
                rawSheetName.substring(1, rawSheetName.length - 2)
            } else {
                rawSheetName.substring(0, rawSheetName.length - 1)
            }
            return rt
        }
    }

    private fun extractFromSingleQuote(str: String): String {
        return str.substring(1, str.length - 1)
    }
}
