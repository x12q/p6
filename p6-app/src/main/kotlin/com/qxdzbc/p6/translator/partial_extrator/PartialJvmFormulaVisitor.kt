package com.qxdzbc.p6.translator.partial_extrator

import com.qxdzbc.p6.formula.translator.antlr.FormulaBaseVisitor
import com.qxdzbc.p6.formula.translator.antlr.FormulaParser
import org.antlr.v4.runtime.tree.ParseTree
import java.nio.file.Path
import javax.inject.Inject
import com.qxdzbc.p6.translator.formula.execution_unit.StrUnit.Companion.toExUnit as toExUnit1

class PartialJvmFormulaVisitor @Inject constructor() : FormulaBaseVisitor<String?>() {
    override fun visitZFormula(ctx: FormulaParser.ZFormulaContext?): String? {
        val rt = this.visit(ctx?.expr())
        return rt
    }

    override fun visitAddSubExpr(ctx: FormulaParser.AddSubExprContext?): String? {
        val expr1 = this.visit(ctx?.expr(1))
        return expr1
    }

    override fun visitUnSubExpr(ctx: FormulaParser.UnSubExprContext?): String? {
        return this.visit(ctx?.expr())
    }

    override fun visitFullRangeAddressExpr(ctx: FormulaParser.FullRangeAddressExprContext?): String? {
        val rangeAddressContext = ctx?.rangeAddress()
        val rangeAddress: String? = rangeAddressContext?.text

        return rangeAddress
    }

    override fun visitMulDivModExpr(ctx: FormulaParser.MulDivModExprContext?): String? {
        val expr1 = this.visit(ctx?.expr(1))
        return expr1
    }

    override fun visitAndOrExpr(ctx: FormulaParser.AndOrExprContext?): String? {
        return null
    }

    override fun visitPowExpr(ctx: FormulaParser.PowExprContext?): String? {
        val expr1 = this.visit(ctx?.expr(1))
        return expr1
    }

    override fun visitFunCall(ctx: FormulaParser.FunCallContext?): String? {
        return this.visit(ctx?.functionCall())
    }

    override fun visitParenExpr(ctx: FormulaParser.ParenExprContext?): String? {
        return this.visit(ctx?.expr())
    }

    override fun visitLiteral(ctx: FormulaParser.LiteralContext?): String? {
        return this.visitLit(ctx?.lit())
    }

    override fun visitFunctionCall(ctx: FormulaParser.FunctionCallContext?): String? {
        val functionName = ctx?.functionName()?.text
        if (functionName != null) {
            return ctx.expr().lastOrNull()?.let{
             this.visit(it)
            }

        } else {
            return null
        }
    }

    override fun visitRangeAsPairCellAddress(ctx: FormulaParser.RangeAsPairCellAddressContext?): String? {
        return ctx?.text
    }

    override fun visitRangeAsOneCellAddress(ctx: FormulaParser.RangeAsOneCellAddressContext?): String? {
        return ctx?.text
    }

    override fun visitRangeAsColAddress(ctx: FormulaParser.RangeAsColAddressContext?): String? {
        return ctx?.text
    }

    override fun visitRangeAsRowAddress(ctx: FormulaParser.RangeAsRowAddressContext?): String? {
        return ctx?.text
    }

    override fun visitRangeInparens(ctx: FormulaParser.RangeInparensContext?): String? {
        return this.visit(ctx?.rangeAddress())
    }

    override fun visitCellAddress(ctx: FormulaParser.CellAddressContext?): String? {
        return ctx?.text
    }

    override fun visitLit(ctx: FormulaParser.LitContext?): String? {
        return null
    }

    override fun visitSheetPrefix(ctx: FormulaParser.SheetPrefixContext?): String? {
        return null
    }

    override fun visitSheetNameWithSpace(ctx: FormulaParser.SheetNameWithSpaceContext?): String? {
        return null
    }

    override fun visitSheetName(ctx: FormulaParser.SheetNameContext?): String? {
        return null
    }

    override fun visitWbPrefix(ctx: FormulaParser.WbPrefixContext?): String? {
        return null
    }

    override fun visitWbPrefixNoPath(ctx: FormulaParser.WbPrefixNoPathContext?): String? {
        return null
    }

    override fun visitWbPrefixWithPath(ctx: FormulaParser.WbPrefixWithPathContext?): String? {
        return null
    }

    override fun visitWbName(ctx: FormulaParser.WbNameContext?): String? {
        return null
    }

    override fun visitWbNameNoSpace(ctx: FormulaParser.WbNameNoSpaceContext?): String? {
        return null
    }

    override fun visitWbNameWithSpace(ctx: FormulaParser.WbNameWithSpaceContext?): String? {
        return null
    }

    override fun visitWbPath(ctx: FormulaParser.WbPathContext?): String? {
        return null
    }

    override fun visitNoSpaceId(ctx: FormulaParser.NoSpaceIdContext?): String? {
        return null
    }

    override fun visitWithSpaceId(ctx: FormulaParser.WithSpaceIdContext?): String? {
        return null
    }

    override fun visitFunctionName(ctx: FormulaParser.FunctionNameContext?): String? {
        return null
    }
}
