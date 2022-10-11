package com.qxdzbc.p6.translator.partial_text_element_extractor

import com.qxdzbc.p6.formula.translator.antlr.FormulaBaseVisitor
import com.qxdzbc.p6.formula.translator.antlr.FormulaParser
import com.qxdzbc.p6.translator.partial_text_element_extractor.text_element.CellRangeElement
import com.qxdzbc.p6.translator.partial_text_element_extractor.text_element.OtherElement
import com.qxdzbc.p6.translator.partial_text_element_extractor.text_element.TokenPosition
import org.antlr.v4.runtime.tree.ErrorNode
import javax.inject.Inject

/**
 * A visitor that extract cell,range into a list
 */
class TextElementVisitor @Inject constructor() : FormulaBaseVisitor<TextElementResult>() {
    override fun visitZFormula(ctx: FormulaParser.ZFormulaContext?): TextElementResult {
        val equalSign = this.visitStartFormulaSymbol(ctx?.startFormulaSymbol())
        val rt = equalSign+(this.visit(ctx?.expr())?: TextElementResult.empty)
        return rt
    }

    override fun visitNotExpr(ctx: FormulaParser.NotExprContext?): TextElementResult {
        val rt= ctx?.let {
            val opRs = ctx.op?.let {
                TextElementResult.from(OtherElement.from(it))
            } ?: TextElementResult.empty
            val exprRs = this.visit(ctx.expr()) ?: TextElementResult.empty
            opRs+exprRs
        }?: TextElementResult.empty
        return rt
    }

    override fun visitAndOrExpr(ctx: FormulaParser.AndOrExprContext?): TextElementResult {
        val operator = ctx?.op?.let { op ->
            when (op.type) {
                FormulaParser.AND -> {
                    "&&"
                }
                FormulaParser.OR -> {
                    "||"
                }
                else -> null
            }?.let { opText ->
                TextElementResult.from(OtherElement(text = opText, range = ctx.op.startIndex..ctx.op.stopIndex))
            }
        } ?: TextElementResult.empty
        val expr0 = this.visit(ctx?.expr(0)) ?: TextElementResult.empty
        val expr1 = this.visit(ctx?.expr(1)) ?: TextElementResult.empty
        return expr0 + operator + expr1
    }

    override fun visitAddSubExpr(ctx: FormulaParser.AddSubExprContext?): TextElementResult {
        val operator = ctx?.op?.let { op ->
            when (op.type) {
                FormulaParser.ADD -> {
                    "+"
                }
                FormulaParser.SUB -> {
                    "-"
                }
                else -> null
            }?.let { opText ->
                TextElementResult.from(OtherElement(text = opText, range = ctx.op.startIndex..ctx.op.stopIndex))
            }
        } ?: TextElementResult.empty
        val expr0 = this.visit(ctx?.expr(0)) ?: TextElementResult.empty
        val expr1 = this.visit(ctx?.expr(1)) ?: TextElementResult.empty
        return expr0 + operator + expr1
    }

    override fun visitUnSubExpr(ctx: FormulaParser.UnSubExprContext?): TextElementResult {
        val rt= ctx?.let {
            val opRs = ctx.op?.let {
                TextElementResult.from(OtherElement.from(it))
            } ?: TextElementResult.empty
            val exprRs = this.visit(ctx.expr()) ?: TextElementResult.empty
            opRs+exprRs
        }?: TextElementResult.empty
        return rt
    }

    override fun visitFullRangeAddressExpr(ctx: FormulaParser.FullRangeAddressExprContext?): TextElementResult {
        val l = ctx?.let {
            val labelLoc = ((it.sheetPrefix()?.text ?: "") + (it.wbPrefix()?.text ?: "")).ifEmpty { null }
            CellRangeElement(
                cellRangeLabel = it.rangeAddress()?.text ?: "",
                labelLoc = labelLoc,
                startTP = TokenPosition(it.start.startIndex),
                stopTP = TokenPosition(it.stop.stopIndex)
            )
        }?.let {
            TextElementResult.from(it)
        } ?: TextElementResult.empty
        return l
    }

    override fun visitMulDivModExpr(ctx: FormulaParser.MulDivModExprContext?): TextElementResult {
        val operator = ctx?.op?.let { op ->
            when (op.type) {
                FormulaParser.MUL -> {
                    "*"
                }
                FormulaParser.DIV -> {
                    "/"
                }
                FormulaParser.MOD -> {
                    "%"
                }
                else -> null
            }?.let { opText ->
                TextElementResult.from(OtherElement(text = opText, range = ctx.op.startIndex..ctx.op.stopIndex))
            }
        } ?: TextElementResult.empty
        val expr0 = this.visit(ctx?.expr(0)) ?: TextElementResult.empty
        val expr1 = this.visit(ctx?.expr(1)) ?: TextElementResult.empty
        return expr0 + operator + expr1
    }

    override fun visitErrorNode(node: ErrorNode?): TextElementResult {
        return node?.text?.let {
            OtherElement(
                text=it,
                range=node.symbol.startIndex .. node.symbol.stopIndex
            ).toResult()
        }?:TextElementResult.empty
    }

    override fun visitLiteral(ctx: FormulaParser.LiteralContext?): TextElementResult {
        val rt = ctx?.let {
            OtherElement.from(it)
        } ?: OtherElement.empty
        return TextElementResult.from(rt)
    }

    override fun visitParenExpr(ctx: FormulaParser.ParenExprContext?): TextElementResult {
        val cRs = (this.visit(ctx?.openParen())  ?: TextElementResult.empty)+
                (this.visit(ctx?.expr()) ?: TextElementResult.empty) +
                (this.visit(ctx?.closeParen()) ?: TextElementResult.empty)
        return cRs
    }

    override fun visitPowExpr(ctx: FormulaParser.PowExprContext?): TextElementResult {
        val v = ctx?.let {
            val operator = ctx.op?.let { op ->
                when (op.type) {
                    FormulaParser.EXP -> {
                        "^"
                    }
                    else -> null
                }?.let { opText ->
                    TextElementResult.from(OtherElement(text = opText, range = ctx.op.startIndex..ctx.op.stopIndex))
                }
            } ?: TextElementResult.empty

            val expr0 = this.visit(ctx.expr(0))
            val expr1 = this.visit(ctx.expr(1))
            if (expr0 != null && expr1 != null) {
                expr0 + operator + expr1
            } else {
                TextElementResult.empty
            }
        } ?: TextElementResult.empty
        return v
    }

    override fun visitOpenParen(ctx: FormulaParser.OpenParenContext?): TextElementResult {
        val rt = ctx?.let {
            TextElementResult.from(OtherElement.from(ctx))
        } ?: TextElementResult.empty
        return rt
    }

    override fun visitCloseParen(ctx: FormulaParser.CloseParenContext?): TextElementResult {
        val rt = ctx?.let {
            TextElementResult.from(OtherElement.from(ctx))
        } ?: TextElementResult.empty
        return rt
    }

    override fun visitStartFormulaSymbol(ctx: FormulaParser.StartFormulaSymbolContext?): TextElementResult {
        val rt = ctx?.let {
            TextElementResult.from(OtherElement.from(ctx))
        } ?: TextElementResult.empty
        return rt
    }

    override fun visitComma(ctx: FormulaParser.CommaContext?): TextElementResult {
        val rt = ctx?.let {
            TextElementResult.from(OtherElement.from(ctx))
        } ?: TextElementResult.empty
        return rt
    }

    override fun visitBoolOperation(ctx: FormulaParser.BoolOperationContext?): TextElementResult {
        val operator = ctx?.op?.let { op ->
            when (op.type) {
                FormulaParser.EQUAL -> {
                    "=="
                }
                FormulaParser.NOT_EQUAL -> {
                    "!="
                }
                FormulaParser.LARGER -> {
                    ">"
                }

                FormulaParser.LARGER_OR_EQUAL -> {
                    ">="
                }
                FormulaParser.SMALLER -> {
                    "<"
                }
                FormulaParser.SMALLER_OR_EQUAL -> {
                    "<="
                }
                else -> null
            }?.let { opText ->
                TextElementResult.from(OtherElement(text = opText, range = ctx.op.startIndex..ctx.op.stopIndex))
            }
        } ?: TextElementResult.empty
        val expr0 = this.visit(ctx?.expr(0))  ?: TextElementResult.empty
        val expr1 = this.visit(ctx?.expr(1))  ?: TextElementResult.empty
        return expr0 + operator + expr1
    }

    override fun visitFunctionCall(ctx: FormulaParser.FunctionCallContext?): TextElementResult {
        var rt = TextElementResult.empty
        if (ctx != null) {
            for (child in ctx.children) {
                val q = this.visit(child)
                if (q != null) {
                    rt += q
                }
            }
            return rt
        } else {
            return TextElementResult.empty
        }
    }


    override fun visitFunctionName(ctx: FormulaParser.FunctionNameContext?): TextElementResult {
        return ctx?.let {
            TextElementResult.from(OtherElement.from(it))
        } ?: TextElementResult.empty
    }

    override fun visitRangeAsPairCellAddress(ctx: FormulaParser.RangeAsPairCellAddressContext?): TextElementResult {
        val rt = ctx?.let {
            TextElementResult.from(CellRangeElement.from(it))
        } ?: TextElementResult.empty
        return rt
    }

    override fun visitRangeAsOneCellAddress(ctx: FormulaParser.RangeAsOneCellAddressContext?): TextElementResult {
        val rt = ctx?.let {
            TextElementResult.from(CellRangeElement.from(it))
        } ?: TextElementResult.empty
        return rt
    }

    override fun visitRangeAsColAddress(ctx: FormulaParser.RangeAsColAddressContext?): TextElementResult {
        val rt = ctx?.let {
            TextElementResult.from(CellRangeElement.from(it))
        } ?: TextElementResult.empty
        return rt
    }

    override fun visitRangeAsRowAddress(ctx: FormulaParser.RangeAsRowAddressContext?): TextElementResult {
        val rt = ctx?.let {
            TextElementResult.from(CellRangeElement.from(it))
        } ?: TextElementResult.empty
        return rt
    }

    override fun visitRangeInparens(ctx: FormulaParser.RangeInparensContext?): TextElementResult {
        var rt = (this.visit(ctx?.openParen()) ?: TextElementResult.empty) +
                (this.visit(ctx?.closeParen()) ?: TextElementResult.empty)
        val raRs = this.visit(ctx?.rangeAddress())  ?: TextElementResult.empty
        rt += raRs
        return rt
    }

    override fun visitLit(ctx: FormulaParser.LitContext?): TextElementResult {
        return ctx?.let {
            TextElementResult.from(OtherElement.from(it))
        } ?: TextElementResult.empty
    }
}
