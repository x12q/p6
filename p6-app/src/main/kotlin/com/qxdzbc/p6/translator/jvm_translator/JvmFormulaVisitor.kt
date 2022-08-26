package com.qxdzbc.p6.translator.jvm_translator

import androidx.compose.runtime.getValue
import com.github.michaelbull.result.Ok
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.toSt
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.address.CellAddresses
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddresses
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.di.FunctionMapMs
import com.qxdzbc.p6.di.FunctionMapSt
import com.qxdzbc.p6.di.state.app_state.DocumentContainerSt
import com.qxdzbc.p6.formula.translator.antlr.FormulaBaseVisitor
import com.qxdzbc.p6.formula.translator.antlr.FormulaParser
import com.qxdzbc.p6.translator.formula.FunctionMap
import com.qxdzbc.p6.translator.formula.function_def.P6FunctionDefinitions
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit.Companion.exUnit
import com.qxdzbc.p6.ui.app.state.DocumentContainer
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import org.antlr.v4.runtime.tree.ParseTree
import java.nio.file.Path

class JvmFormulaVisitor @AssistedInject constructor(
    @Assisted("1") private val wbKeySt: St<WorkbookKey>,
    @Assisted("2") private val wsNameSt: St<String>,
    @FunctionMapMs
    private val functionMapMs:Ms<FunctionMap> ,
    @DocumentContainerSt
    private val docContMs: St<@JvmSuppressWildcards DocumentContainer>
) : FormulaBaseVisitor<ExUnit>() {
    private val docCont: DocumentContainer by docContMs
    private val wbKey: WorkbookKey by wbKeySt
    private val wsName: String by wsNameSt

    private val wbKeyExUnit get() = wbKey.exUnit()
    private val wbKeyStExUnit = wbKeySt.exUnit()

    private val wsNameExUnit get() = wsName.exUnit()
    private val wsNameStExUnit = ExUnit.WsNameStUnit(wsNameSt)

    override fun visit(tree: ParseTree?): ExUnit? {
        return super.visit(tree)
    }

    override fun visitZFormula(ctx: FormulaParser.ZFormulaContext?): ExUnit? {
        val rt = this.visit(ctx?.expr())
        return rt
    }

    override fun visitFunCall(ctx: FormulaParser.FunCallContext?): ExUnit? {
        return this.visit(ctx?.functionCall())
    }

    override fun visitLiteral(ctx: FormulaParser.LiteralContext?): ExUnit? {
        return this.visitLit(ctx?.lit())
    }

    override fun visitUnSubExpr(ctx: FormulaParser.UnSubExprContext?): ExUnit? {
        val op = ctx?.op
        when (op?.type) {
            FormulaParser.SUB -> {
                val exUnit: ExUnit? = this.visit(ctx.expr())
                val rt: ExUnit? = exUnit?.let { ExUnit.UnarySubtract(it) }
                return rt
            }
            else -> return null
        }
    }

    override fun visitParenExpr(ctx: FormulaParser.ParenExprContext?): ExUnit? {
        return this.visit(ctx?.expr())
    }

    override fun visitPowExpr(ctx: FormulaParser.PowExprContext?): ExUnit? {
        val v = ctx?.let {
            val expr0 = this.visit(ctx.expr(0))
            val expr1 = this.visit(ctx.expr(1))
            if (expr0 != null && expr1 != null) {
                ExUnit.PowerBy(expr0, expr1)
            } else {
                null
            }
        }
        return v
    }

    override fun visitMulDivModExpr(ctx: FormulaParser.MulDivModExprContext?): ExUnit? {
        val rt = ctx?.let {
            val expr0 = this.visit(ctx.expr(0))
            val expr1 = this.visit(ctx.expr(1))
            if (expr0 != null && expr1 != null) {
                val op = ctx.op
                val r = when (op?.type) {
                    FormulaParser.MUL -> {
                        ExUnit.MultiplyOperator(expr0, expr1)
                    }
                    FormulaParser.DIV -> {
                        ExUnit.Div(expr0, expr1)
                    }
                    else -> null
                }
                r
            } else {
                null
            }
        }
        return rt
    }

    override fun visitAddSubExpr(ctx: FormulaParser.AddSubExprContext?): ExUnit? {
        val rt = ctx?.let {
            val expr0 = this.visit(ctx.expr(0))
            val expr1 = this.visit(ctx.expr(1))
            val v: ExUnit? = if (expr0 != null && expr1 != null) {
                val op = ctx.op
                when (op?.type) {
                    FormulaParser.ADD -> {
                        ExUnit.AddOperator(expr0, expr1)
                    }
                    FormulaParser.SUB -> {
                        ExUnit.MinusOperator(expr0, expr1)
                    }
                    else -> null
                }
            } else null
            v
        }
        return rt
    }

    override fun visitSheetPrefix(ctx: FormulaParser.SheetPrefixContext?): ExUnit.WsNameStUnit? {
        val e1 = this.visitSheetName(ctx?.sheetName())
        if (e1 != null) {
            return e1
        }
        val e2 = this.visitSheetNameWithSpace(ctx?.sheetNameWithSpace())
        return e2
    }

    override fun visitSheetNameWithSpace(ctx: FormulaParser.SheetNameWithSpaceContext?): ExUnit.WsNameStUnit? {
        val u= this.visitWithSpaceId(ctx?.withSpaceId())
        val rt = u?.run()?.component1()?.toSt()?.let {
            ExUnit.WsNameStUnit(it)
        }
        return rt
    }

    override fun visitSheetName(ctx: FormulaParser.SheetNameContext?): ExUnit.WsNameStUnit? {
        val exUnit = this.visitNoSpaceId(ctx?.noSpaceId())
        return exUnit?.run()?.component1()?.toSt()?.let { ExUnit.WsNameStUnit(it) }
    }

    override fun visitNoSpaceId(ctx: FormulaParser.NoSpaceIdContext?): ExUnit.StrUnit? {
        return ctx?.text?.exUnit()
    }

    override fun visitWithSpaceId(ctx: FormulaParser.WithSpaceIdContext?): ExUnit.StrUnit? {
        return ctx?.text?.let { extractFromSingleQuote(it) }?.exUnit()
    }

    override fun visitWbPrefix(ctx: FormulaParser.WbPrefixContext?): ExUnit.WbKeyStUnit? {
        val rt1 = visitWbPrefixNoPath(ctx?.wbPrefixNoPath())
        if (rt1 != null) {
            return rt1
        }
        val rt2 = visitWbPrefixWithPath(ctx?.wbPrefixWithPath())
        return rt2
    }

    override fun visitWbPrefixNoPath(ctx: FormulaParser.WbPrefixNoPathContext?): ExUnit.WbKeyStUnit? {
        val wbNameExUnitRs = this.visitWbName(ctx?.wbName())
        val wbName: String? = wbNameExUnitRs?.run()?.component1() as String?
        val wbk: WorkbookKey? = wbName?.let { WorkbookKey(it) }
        val rt = wbk?.toSt()?.exUnit()
        return rt
    }

    override fun visitWbPrefixWithPath(ctx: FormulaParser.WbPrefixWithPathContext?): ExUnit.WbKeyStUnit? {
        val wbNameExUnitRs: ExUnit? = this.visitWbName(ctx?.wbName())
        val wbName: String? = wbNameExUnitRs?.run()?.component1() as String?
        val wbk: WorkbookKey? = wbName?.let {
            val wbPathExUnit = this.visitWbPath(ctx?.wbPath())
            val wbPath: Path? = (wbPathExUnit?.run()?.component1() as String?)?.let { Path.of(it) }
            WorkbookKey(wbName, wbPath)
        }
        val rt = wbk?.toSt()?.exUnit()
        return rt
    }

    override fun visitWbPath(ctx: FormulaParser.WbPathContext?): ExUnit? {
        return ctx?.text?.let { extractFromSingleQuote(it).exUnit() }
    }

    override fun visitWbName(ctx: FormulaParser.WbNameContext?): ExUnit? {
        if (ctx?.wbNameNoSpace() != null) {
            return this.visitWbNameNoSpace(ctx.wbNameNoSpace())
        } else if (ctx?.wbNameWithSpace() != null) {
            return this.visitWbNameWithSpace(ctx.wbNameWithSpace())
        } else {
            return null
        }
    }

    override fun visitWbNameNoSpace(ctx: FormulaParser.WbNameNoSpaceContext?): ExUnit? {
        return ctx?.text?.exUnit()
    }

    override fun visitWbNameWithSpace(ctx: FormulaParser.WbNameWithSpaceContext?): ExUnit? {
        val text: String? = ctx?.text
        return text?.let { extractFromSingleQuote(it) }?.exUnit()
    }

    override fun visitFullRangeAddressExpr(ctx: FormulaParser.FullRangeAddressExprContext?): ExUnit? {
        val wbKeyStExUnit: ExUnit.WbKeyStUnit = this.visitWbPrefix(ctx?.wbPrefix())?.let { hypoWbKeyStUnit ->
            val actualWbKeySt: St<WorkbookKey>? = hypoWbKeyStUnit
                .run()
                .component1()
                ?.let { hypoWbKeySt ->
                    docCont.getWbKeySt(hypoWbKeySt.value)
                }
            val z = actualWbKeySt?.exUnit() ?: hypoWbKeyStUnit
            z
        } ?: wbKeyStExUnit

        val wbkSt: St<WorkbookKey> = wbKeyStExUnit.run().component1() ?: wbKeySt

        val wsNameStUnit: ExUnit.WsNameStUnit = this.visitSheetPrefix(ctx?.sheetPrefix())?.let {hypoWsNameStUnit->
            val k = hypoWsNameStUnit.run().component1()?.let { wsName ->
                val z = docCont.getWsNameSt(wbkSt, wsName.value)?.let {
                    ExUnit.WsNameStUnit(it)
                }
                z
            }
            k ?: hypoWsNameStUnit
        } ?: wsNameStExUnit
        val rangeAddressContext = ctx?.rangeAddress()
        val rangeAddress: String? = rangeAddressContext?.text
        if (rangeAddress != null) {
            val cellAddressRs: Rse<CellAddress> = CellAddresses.fromLabelRs(rangeAddress)
            if (cellAddressRs is Ok) {
                val raUnit = cellAddressRs.value.exUnit()
                return ExUnit.Func(
                    funcName = P6FunctionDefinitions.getCellRs,
                    args = listOf(wbKeyStExUnit, wsNameStUnit, raUnit),
                    functionMapSt = functionMapMs,
//                    isImplicit = true
                )
            }

            val rangeAddressRs: Rse<RangeAddress> = RangeAddresses.fromLabelRs(rangeAddress)
            if (rangeAddressRs is Ok) {
                val raUnit: ExUnit = rangeAddressRs.value.exUnit()
                return ExUnit.Func(
                    funcName = P6FunctionDefinitions.getRangeRs,
                    args = listOf(wbKeyStExUnit, wsNameStUnit, raUnit),
                    functionMapSt = functionMapMs,
//                    isImplicit = true
                )
            }
        }

        return null
    }

    override fun visitFunctionCall(ctx: FormulaParser.FunctionCallContext?): ExUnit? {
        val functionName = ctx?.functionName()?.text
        if (functionName != null) {
            val eLis = mutableListOf<ExUnit>()
            for (e in ctx.expr()) {
                val eRs = this.visit(e)
                if (eRs != null) {
                    eLis.add(eRs)
                }
            }
            return ExUnit.Func(
                funcName = functionName,
                args = eLis,
                functionMapSt = functionMapMs
            )
        } else {
            return null
        }
    }


    override fun visitFunctionName(ctx: FormulaParser.FunctionNameContext?): ExUnit? {
        return ctx?.text?.let {
            ExUnit.StrUnit(it)
        }
    }

    override fun visitRangeAsPairCellAddress(ctx: FormulaParser.RangeAsPairCellAddressContext?): ExUnit? {
        val cell0 = ctx?.cellAddress(0)?.text
        val cell1 = ctx?.cellAddress(1)?.text
        if (cell0 != null && cell1 != null) {
            val raUnit = RangeAddress(CellAddress(cell0), CellAddress(cell1)).exUnit()
            return ExUnit.Func(
                funcName = P6FunctionDefinitions.getRangeRs,
                args = listOf(wbKeyStExUnit, wsNameStExUnit, raUnit),
                functionMapSt = functionMapMs,
//                isImplicit = true
            )
        } else {
            return null
        }
    }

    override fun visitRangeAsOneCellAddress(ctx: FormulaParser.RangeAsOneCellAddressContext?): ExUnit? {
        val cell0 = ctx?.text
        if (cell0 != null) {
            val raUnit = CellAddress(cell0).exUnit()
            val rt = ExUnit.Func(
                funcName = P6FunctionDefinitions.getCellRs,
                args = listOf(wbKeyExUnit, wsNameExUnit, raUnit),
                functionMapSt = functionMapMs,
//                isImplicit = true
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
                args = listOf(wbKeyStExUnit, wsNameStExUnit, raUnit),
                functionMapSt = functionMapMs,
//                isImplicit = true
            )
        } else {
            return null
        }
    }

    override fun visitRangeAsRowAddress(ctx: FormulaParser.RangeAsRowAddressContext?): ExUnit? {
        val rowAddress = ctx?.text
        if (rowAddress != null) {
            val raUnit = RangeAddress(rowAddress).exUnit()
            return ExUnit.Func(
                funcName = P6FunctionDefinitions.getRangeRs,
                args = listOf(wbKeyStExUnit, wsNameStExUnit, raUnit),
                functionMapSt = functionMapMs,
//                isImplicit = true
            )
        } else {
            return null
        }
    }

    override fun visitRangeInparens(ctx: FormulaParser.RangeInparensContext?): ExUnit? {
        return this.visit(ctx?.rangeAddress())
    }

    override fun visitLit(ctx: FormulaParser.LitContext?): ExUnit? {
        val floatNode = ctx?.FLOAT_NUMBER()
        val intNode = ctx?.INT()
        val textNode = ctx?.STRING()
        val boolNode = ctx?.BOOLEAN()

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
