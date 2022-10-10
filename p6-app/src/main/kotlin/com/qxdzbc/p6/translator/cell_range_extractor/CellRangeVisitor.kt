package com.qxdzbc.p6.translator.cell_range_extractor

import com.qxdzbc.p6.formula.translator.antlr.FormulaBaseVisitor
import com.qxdzbc.p6.formula.translator.antlr.FormulaParser
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.qxdzbc.p6.translator.formula.execution_unit.FuncUnit
import javax.inject.Inject

/**
 * A visitor that extract cell,range into a list
 */
class CellRangeVisitor @Inject constructor() : FormulaBaseVisitor<List<CellRangePosition>>() {
    override fun visitZFormula(ctx: FormulaParser.ZFormulaContext?): List<CellRangePosition> {
        val rt = this.visit(ctx?.expr())
        return rt
    }

    override fun visitAddSubExpr(ctx: FormulaParser.AddSubExprContext?): List<CellRangePosition> {
        val expr0 = this.visit(ctx?.expr(0))
        val expr1 = this.visit(ctx?.expr(1))
        return expr0+expr1
    }

    override fun visitUnSubExpr(ctx: FormulaParser.UnSubExprContext?): List<CellRangePosition> {
        return this.visit(ctx?.expr())
    }

    override fun visitFullRangeAddressExpr(ctx: FormulaParser.FullRangeAddressExprContext?): List<CellRangePosition> {
        val l = ctx?.let{
            val labelLoc = ((it.sheetPrefix()?.text ?: "")+(it.wbPrefix()?.text ?:"")).ifEmpty { null }
            listOf(
            CellRangePosition(
                cellRangeLabel = it.rangeAddress()?.text ?:"",
                labelLoc = labelLoc,
                start = TokenPosition(it.start.startIndex),
                stop = TokenPosition(it.stop.stopIndex)
            )
        )
        }?:emptyList()
        return l
    }

    override fun visitMulDivModExpr(ctx: FormulaParser.MulDivModExprContext?): List<CellRangePosition> {
        val expr0 = this.visit(ctx?.expr(0))
        val expr1 = this.visit(ctx?.expr(1))
        return expr0+expr1
    }
    override fun visitLiteral(ctx: FormulaParser.LiteralContext?): List<CellRangePosition> {
        return emptyList()
    }


    override fun visitParenExpr(ctx: FormulaParser.ParenExprContext?): List<CellRangePosition> {
        return this.visit(ctx?.expr())
    }

    override fun visitPowExpr(ctx: FormulaParser.PowExprContext?): List<CellRangePosition> {
        val v = ctx?.let {
            val expr0 = this.visit(ctx.expr(0))
            val expr1 = this.visit(ctx.expr(1))
            if (expr0 != null && expr1 != null) {
                expr0 + expr1
            } else {
                emptyList()
            }
        }?: emptyList()
        return v
    }


    override fun visitSheetPrefix(ctx: FormulaParser.SheetPrefixContext?): List<CellRangePosition> {
        return emptyList()
    }

    override fun visitSheetNameWithSpace(ctx: FormulaParser.SheetNameWithSpaceContext?): List<CellRangePosition> {
        return emptyList()
    }

    override fun visitSheetName(ctx: FormulaParser.SheetNameContext?): List<CellRangePosition> {
        return emptyList()
    }

    override fun visitNoSpaceId(ctx: FormulaParser.NoSpaceIdContext?): List<CellRangePosition> {
        return emptyList()
    }

    override fun visitWithSpaceId(ctx: FormulaParser.WithSpaceIdContext?): List<CellRangePosition> {
        return emptyList()
    }

    override fun visitWbPrefix(ctx: FormulaParser.WbPrefixContext?): List<CellRangePosition> {
        return emptyList()
    }

    override fun visitWbPrefixNoPath(ctx: FormulaParser.WbPrefixNoPathContext?): List<CellRangePosition> {
        return emptyList()
    }

    override fun visitWbPrefixWithPath(ctx: FormulaParser.WbPrefixWithPathContext?): List<CellRangePosition> {
        return emptyList()
    }

    override fun visitWbPath(ctx: FormulaParser.WbPathContext?): List<CellRangePosition> {
        return emptyList()
    }

    override fun visitWbName(ctx: FormulaParser.WbNameContext?): List<CellRangePosition> {
        return emptyList()
    }

    override fun visitWbNameNoSpace(ctx: FormulaParser.WbNameNoSpaceContext?): List<CellRangePosition> {
        return emptyList()
    }

    override fun visitWbNameWithSpace(ctx: FormulaParser.WbNameWithSpaceContext?): List<CellRangePosition> {
        return emptyList()
    }


    override fun visitFunctionCall(ctx: FormulaParser.FunctionCallContext?): List<CellRangePosition> {
        val functionName = ctx?.functionName()?.text
        if (functionName != null) {
            val eLis = mutableListOf<CellRangePosition>()
            for (e in ctx.expr()) {
                val eRs = this.visit(e)
                if (eRs != null) {
                    eLis.addAll(eRs)
                }
            }
            return eLis
        } else {
            return emptyList()
        }
    }


    override fun visitFunctionName(ctx: FormulaParser.FunctionNameContext?): List<CellRangePosition> {
        return emptyList()
    }

    override fun visitRangeAsPairCellAddress(ctx: FormulaParser.RangeAsPairCellAddressContext?): List<CellRangePosition> {
        val rt = ctx?.let{
            listOf(CellRangePosition.from(it))
        }?: emptyList()
        return rt
    }

    override fun visitRangeAsOneCellAddress(ctx: FormulaParser.RangeAsOneCellAddressContext?): List<CellRangePosition> {
        val rt = ctx?.let{
            listOf(CellRangePosition.from(it))
        }?: emptyList()
        return rt
    }

    override fun visitRangeAsColAddress(ctx: FormulaParser.RangeAsColAddressContext?): List<CellRangePosition> {
        val rt = ctx?.let{
            listOf(CellRangePosition.from(it))
        }?: emptyList()
        return rt
    }

    override fun visitRangeAsRowAddress(ctx: FormulaParser.RangeAsRowAddressContext?): List<CellRangePosition> {
    val rt = ctx?.let{
        listOf(CellRangePosition.from(it))
    }?: emptyList()
    return rt
    }

    override fun visitRangeInparens(ctx: FormulaParser.RangeInparensContext?): List<CellRangePosition> {
        return this.visit(ctx?.rangeAddress())
    }

    override fun visitLit(ctx: FormulaParser.LitContext?): List<CellRangePosition> {
        return emptyList()
    }
}
