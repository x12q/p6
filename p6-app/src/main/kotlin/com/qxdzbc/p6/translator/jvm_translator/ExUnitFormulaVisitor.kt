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
import com.qxdzbc.p6.formula.translator.antlr.FormulaBaseVisitor
import com.qxdzbc.p6.formula.translator.antlr.FormulaParser
import com.qxdzbc.p6.translator.formula.FunctionMap
import com.qxdzbc.p6.translator.formula.execution_unit.*
import com.qxdzbc.p6.translator.formula.execution_unit.function.FunctionExUnit
import com.qxdzbc.p6.translator.formula.execution_unit.obj_type_in_app.CellAddressUnit.Companion.toExUnit
import com.qxdzbc.p6.translator.formula.execution_unit.obj_type_in_app.RangeAddressUnit.Companion.toExUnit
import com.qxdzbc.p6.translator.formula.execution_unit.function.GetCellUnit
import com.qxdzbc.p6.translator.formula.execution_unit.function.GetRange
import com.qxdzbc.p6.translator.formula.execution_unit.obj_type_in_app.WbKeyStUnit
import com.qxdzbc.p6.translator.formula.execution_unit.primitive.StrUnit.Companion.toExUnit
import com.qxdzbc.p6.translator.formula.execution_unit.obj_type_in_app.WbKeyStUnit.Companion.toExUnit
import com.qxdzbc.p6.translator.formula.execution_unit.obj_type_in_app.WsNameStUnit
import com.qxdzbc.p6.translator.formula.execution_unit.operator.*
import com.qxdzbc.p6.translator.formula.execution_unit.primitive.BoolUnit
import com.qxdzbc.p6.translator.formula.execution_unit.primitive.DoubleUnit
import com.qxdzbc.p6.translator.formula.execution_unit.primitive.IntUnit
import com.qxdzbc.p6.translator.formula.execution_unit.primitive.StrUnit
import com.qxdzbc.p6.translator.formula.function_def.P6FunctionDefinitions
import com.qxdzbc.p6.ui.app.state.DocumentContainer
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import org.antlr.v4.runtime.tree.ParseTree
import java.nio.file.Path

class ExUnitFormulaVisitor @AssistedInject constructor(
    @Assisted("1") private val wbKeySt: St<WorkbookKey>,
    @Assisted("2") private val wsNameSt: St<String>,
    private val functionMapMs: Ms<FunctionMap>,
    private val docContMs: St<@JvmSuppressWildcards DocumentContainer>,
//    private val errX:ExUnitErrors.IncompatibleType = ExUnitErrors.IncompatibleType.instance,
) : FormulaBaseVisitor<ExUnit>() {
    private val docCont: DocumentContainer by docContMs
    private val wbKey: WorkbookKey by wbKeySt
    private val wsName: String by wsNameSt

    private val wbKeyStExUnit = wbKeySt.toExUnit()

    private val wsNameExUnit get() = wsName.toExUnit()
    private val wsNameStExUnit = WsNameStUnit(wsNameSt)


    override fun visit(tree: ParseTree?): ExUnit? {
        return super.visit(tree)
    }

    override fun visitZFormula(ctx: FormulaParser.ZFormulaContext?): ExUnit? {
        val rt = this.visit(ctx?.expr())

        return rt
    }

    override fun visitInvokeExpr(ctx: FormulaParser.InvokeExprContext?): ExUnit? {
        return this.visitInvokation(ctx?.invokation())
    }

    override fun visitInvokation(ctx: FormulaParser.InvokationContext?): ExUnit? {
        val rt = this.visitFullRangeAddress(ctx?.fullRangeAddress())?:this.visitFunctionCall(ctx?.functionCall())
        return rt
    }

    override fun visitLiteral(ctx: FormulaParser.LiteralContext?): ExUnit? {
        return this.visitLit(ctx?.lit())
    }

    override fun visitUnSubExpr(ctx: FormulaParser.UnSubExprContext?): ExUnit? {
        val op = ctx?.op
        when (op?.type) {
            FormulaParser.SUB -> {
                val exUnit: ExUnit? = this.visit(ctx.expr())
                val rt: ExUnit? = exUnit?.let { UnarySubtract(it) }
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
                PowerByUnit(expr0, expr1)
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
                        MultiplyOperator(expr0, expr1)
                    }
                    FormulaParser.DIV -> {
                        DivOperator(expr0, expr1)
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
                        AddOperator(expr0, expr1,
//                            errX
                        )
                    }
                    FormulaParser.SUB -> {
                        MinusOperator(expr0, expr1)
                    }
                    else -> null
                }
            } else null
            v
        }
        return rt
    }

    override fun visitSheetPrefix(ctx: FormulaParser.SheetPrefixContext?): WsNameStUnit? {
        val e1 = this.visitSheetName(ctx?.sheetName())
        if (e1 != null) {
            return e1
        }
        val e2 = this.visitSheetNameWithSpace(ctx?.sheetNameWithSpace())
        return e2
    }

    override fun visitSheetNameWithSpace(ctx: FormulaParser.SheetNameWithSpaceContext?): WsNameStUnit? {
        val rt= ctx?.WITH_SPACE_ID()?.text?.let{
            WsNameStUnit(extractFromSingleQuote(it).toSt())
        }
        return rt
    }

    override fun visitSheetName(ctx: FormulaParser.SheetNameContext?): WsNameStUnit? {
        val rt= ctx?.noSpaceId()?.text?.let {
            WsNameStUnit(it.toSt())
        }
        return rt
    }


    override fun visitWbPrefix(ctx: FormulaParser.WbPrefixContext?): WbKeyStUnit? {
        val rt1 = visitWbPrefixNoPath(ctx?.wbPrefixNoPath())
        if (rt1 != null) {
            return rt1
        }
        val rt2 = visitWbPrefixWithPath(ctx?.wbPrefixWithPath())
        return rt2
    }

    override fun visitWbPrefixNoPath(ctx: FormulaParser.WbPrefixNoPathContext?): WbKeyStUnit? {
        val wbNameExUnitRs = this.visitWbName(ctx?.wbName())
        val wbName: String? = wbNameExUnitRs?.runRs()?.component1() as String?
        val wbk: WorkbookKey? = wbName?.let { WorkbookKey(it) }
        val rt = wbk?.toSt()?.toExUnit()
        return rt
    }

    override fun visitWbPrefixWithPath(ctx: FormulaParser.WbPrefixWithPathContext?): WbKeyStUnit? {
        val wbNameExUnitRs: ExUnit? = this.visitWbName(ctx?.wbName())
        val wbName: String? = wbNameExUnitRs?.runRs()?.component1() as String?
        val wbk: WorkbookKey? = wbName?.let {
            val wbPathExUnit = this.visitWbPath(ctx?.wbPath())
            val wbPath: Path? = (wbPathExUnit?.runRs()?.component1() as String?)?.let { Path.of(it) }
            WorkbookKey(wbName, wbPath)
        }
        val rt = wbk?.toSt()?.toExUnit()
        return rt
    }

    override fun visitWbPath(ctx: FormulaParser.WbPathContext?): ExUnit? {
        return ctx?.text?.let { extractFromSingleQuote(it).toExUnit() }
    }

    override fun visitWbName(ctx: FormulaParser.WbNameContext?): ExUnit? {
        val rt = ctx?.noSpaceId()?.text?.toExUnit() ?: ctx?.WITH_SPACE_ID()?.text?.let {
            extractFromSingleQuote(it).toExUnit()
        }
        return rt
    }

    override fun visitFullRangeAddress(ctx: FormulaParser.FullRangeAddressContext?): ExUnit? {
        val wbKeyStExUnit: WbKeyStUnit = this.visitWbPrefix(ctx?.wbPrefix())?.let { hypoWbKeyStUnit ->
            val actualWbKeySt: St<WorkbookKey>? = hypoWbKeyStUnit
                .runRs()
                .component1()
                ?.let { hypoWbKeySt ->
                    docCont.getWbKeySt(hypoWbKeySt.value)
                }
            val z = actualWbKeySt?.toExUnit() ?: hypoWbKeyStUnit
            z
        } ?: wbKeyStExUnit

        val wbkSt: St<WorkbookKey> = wbKeyStExUnit.runRs().component1() ?: wbKeySt

        val wsNameStUnit: WsNameStUnit = this.visitSheetPrefix(ctx?.sheetPrefix())?.let { hypoWsNameStUnit ->
            val k = hypoWsNameStUnit.runRs().component1()?.let { wsName ->
                val z = docCont.getWsNameSt(wbkSt, wsName.value)?.let {
                    WsNameStUnit(it)
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
                val raUnit = cellAddressRs.value.toExUnit()
                return GetCellUnit(
                    funcName = P6FunctionDefinitions.getCellRs,
                    wbKeyUnit = wbKeyStExUnit,
                    wsNameUnit = wsNameStUnit,
                    cellAddressUnit = raUnit,
                    functionMapSt = functionMapMs,
                )
            }

            val rangeAddressRs: Rse<RangeAddress> = RangeAddresses.fromLabelRs(rangeAddress)
            if (rangeAddressRs is Ok) {
                val raUnit = rangeAddressRs.value.toExUnit()
                return GetRange(
                    funcName = P6FunctionDefinitions.getRangeRs,
                    wbKeyUnit = wbKeyStExUnit, wsNameUnit = wsNameStUnit, rangeAddressUnit = raUnit,
                    functionMapSt = functionMapMs,
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
            return FunctionExUnit(
                funcName = functionName,
                args = eLis,
                functionMapSt = functionMapMs
            )
        } else {
            return null
        }
    }

//    override fun visitNoSpaceId(ctx: FormulaParser.NoSpaceIdContext?): ExUnit? {
//        val rt= ctx?.text?.let{it.toExUnit()}
//        return rt
//    }

    override fun visitFunctionName(ctx: FormulaParser.FunctionNameContext?): ExUnit? {
        return ctx?.text?.let {
            StrUnit(it)
        }
    }

    override fun visitRangeAsPairCellAddress(ctx: FormulaParser.RangeAsPairCellAddressContext?): ExUnit? {
        val cell0 = ctx?.cellAddress(0)?.text
        val cell1 = ctx?.cellAddress(1)?.text
        if (cell0 != null && cell1 != null) {
            val raUnit = RangeAddress(CellAddress(cell0), CellAddress(cell1)).toExUnit()
            return GetRange(
                funcName = P6FunctionDefinitions.getRangeRs,
                wbKeyUnit = wbKeyStExUnit, wsNameUnit = wsNameStExUnit, rangeAddressUnit = raUnit,
                functionMapSt = functionMapMs,
            )
        } else {
            return null
        }
    }

    override fun visitRangeAsOneCellAddress(ctx: FormulaParser.RangeAsOneCellAddressContext?): ExUnit? {
        val cell0 = ctx?.text
        if (cell0 != null) {
            val raUnit = CellAddress(cell0).toExUnit()
            val rt = GetCellUnit(
                funcName = P6FunctionDefinitions.getCellRs,
                wbKeyUnit = wbKeyStExUnit,
                wsNameUnit = wsNameStExUnit,
                cellAddressUnit = raUnit,
                functionMapSt = functionMapMs,
            )
            return rt
        } else {
            return null
        }
    }

    override fun visitRangeAsColAddress(ctx: FormulaParser.RangeAsColAddressContext?): ExUnit? {
        val colAddress = ctx?.text
        if (colAddress != null) {
            val raUnit = RangeAddress(colAddress).toExUnit()
            return GetRange(
                funcName = P6FunctionDefinitions.getRangeRs,
                wbKeyUnit = wbKeyStExUnit, wsNameUnit = wsNameStExUnit, rangeAddressUnit = raUnit,
                functionMapSt = functionMapMs,
            )
        } else {
            return null
        }
    }

    override fun visitRangeAsRowAddress(ctx: FormulaParser.RangeAsRowAddressContext?): ExUnit? {
        val rowAddress = ctx?.text
        if (rowAddress != null) {
            val raUnit = RangeAddress(rowAddress).toExUnit()
            return GetRange(
                funcName = P6FunctionDefinitions.getRangeRs,
                wbKeyUnit = wbKeyStExUnit, wsNameUnit = wsNameStExUnit, rangeAddressUnit = raUnit,
                functionMapSt = functionMapMs,
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
                "TRUE" -> return BoolUnit.TRUE
                "FALSE" -> return BoolUnit.FALSE
            }
        }

        if (floatNode != null) {
            val doubleNum = floatNode.text.toDoubleOrNull()
            if (doubleNum != null) {
                return DoubleUnit(doubleNum)
            }
        }
        if (intNode != null) {
            val i = intNode.text.toIntOrNull()
            if (i != null) {
                return IntUnit(i)
            } else {
                val i2 = intNode.text.toDoubleOrNull()
                if (i2 != null) {
                    return DoubleUnit(i2)
                }
            }
        }

        if (textNode != null) {
            val ot = textNode.text
            val t = ot?.substring(1, ot.length - 1)
            if (t != null) {
                return StrUnit(t)
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
