package com.qxdzbc.p6.translator.partial_text_element_extractor

import com.qxdzbc.p6.formula.translator.antlr.FormulaBaseVisitor
import com.qxdzbc.p6.formula.translator.antlr.FormulaParser
import com.qxdzbc.p6.translator.partial_text_element_extractor.text_element.BasicTextElement
import com.qxdzbc.p6.translator.partial_text_element_extractor.text_element.CellRangeElement
import com.qxdzbc.p6.translator.partial_text_element_extractor.text_element.WbElement
import com.qxdzbc.p6.translator.partial_text_element_extractor.text_element.WsNameElement
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.tree.ErrorNode
import org.antlr.v4.runtime.tree.ParseTree
import javax.inject.Inject

/**
 * A visitor that extract element in a parse tree (created by parsing a text formula) into [TextElementResult]
 */
class TextElementVisitor @Inject constructor() : FormulaBaseVisitor<TextElementResult>() {

    private fun handleErrorChildren(children: List<ParseTree>?): TextElementResult {
        val rt = children
            ?.filter { it is ErrorNode }
            ?.map {
                visitErrorNode(it as ErrorNode)
            }
            ?.fold(TextElementResult.empty) { acc, e -> acc + e } ?: TextElementResult.empty
        return rt
    }

    override fun visitZFormula(ctx: FormulaParser.ZFormulaContext?): TextElementResult {
        val equalSign = this.visitStartFormulaSymbol(ctx?.startFormulaSymbol())
        val errorResult = handleErrorChildren(ctx?.children)
        val rt = equalSign + (this.visit(ctx?.expr()) ?: TextElementResult.empty) + errorResult
        return rt
    }

    override fun visitNotExpr(ctx: FormulaParser.NotExprContext?): TextElementResult {
        val rt = ctx?.let {
            val opRs = ctx.op?.let {
                TextElementResult.from(BasicTextElement.from(it))
            } ?: TextElementResult.empty
            val exprRs = this.visit(ctx.expr()) ?: TextElementResult.empty
            opRs + exprRs
        } ?: TextElementResult.empty
        val errorResult = handleErrorChildren(ctx?.children)
        return rt + errorResult
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
                TextElementResult.from(BasicTextElement.from(ctx.op))
            }
        } ?: TextElementResult.empty
        val expr0 = this.visit(ctx?.expr(0)) ?: TextElementResult.empty
        val expr1 = this.visit(ctx?.expr(1)) ?: TextElementResult.empty
        val errorResult = handleErrorChildren(ctx?.children)
        return expr0 + operator + expr1 + errorResult
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
                TextElementResult.from(
                    BasicTextElement.from(ctx.op)
                )
            }
        } ?: TextElementResult.empty
        val expr0 = this.visit(ctx?.expr(0)) ?: TextElementResult.empty
        val expr1 = this.visit(ctx?.expr(1)) ?: TextElementResult.empty
        val errorResult = handleErrorChildren(ctx?.children)
        return expr0 + operator + expr1 + errorResult
    }

    override fun visitUnSubExpr(ctx: FormulaParser.UnSubExprContext?): TextElementResult {
        val rt = ctx?.let {
            val opRs = ctx.op?.let {
                TextElementResult.from(BasicTextElement.from(it))
            } ?: TextElementResult.empty
            val exprRs = this.visit(ctx.expr()) ?: TextElementResult.empty
            opRs + exprRs
        } ?: TextElementResult.empty
        val errorResult = handleErrorChildren(ctx?.children)
        return rt + errorResult
    }

    private fun extractFromSingleQuote(str: String): String {
        return str.substring(1, str.length - 1)
    }

    override fun visitSheetNameWithSpace(ctx: FormulaParser.SheetNameWithSpaceContext?): TextElementResult {
        val rt = ctx?.let {
            WsNameElement(
                label = ctx.text,
                wsName = extractFromSingleQuote(ctx.text),
                ctx.start.startIndex..ctx.stop.stopIndex
            )
        }?.let {
            TextElementResult.ferry(it)
        } ?: TextElementResult.empty
        return rt
    }

    override fun visitSheetName(ctx: FormulaParser.SheetNameContext?): TextElementResult {
        val rt = ctx?.let {
            WsNameElement(label = ctx.text, wsName = ctx.text, ctx.start.startIndex..ctx.stop.stopIndex)
        }?.let {
            TextElementResult.ferry(it)
        } ?: TextElementResult.empty
        return rt
    }

    override fun visitSheetPrefix(ctx: FormulaParser.SheetPrefixContext?): TextElementResult {
        val r0 = ctx?.let {
            val sheetNameResul = ctx.sheetName()?.let {
                visitSheetName(it)
            } ?: ctx.sheetNameWithSpace()?.let {
                visitSheetNameWithSpace(it)
            }
            sheetNameResul?.ferryWsNameElement?.let {
                WsNameElement(label = ctx.text, wsName = it.text, ctx.start.startIndex..ctx.stop.stopIndex)
            }
        }?.let {
            TextElementResult.ferry(it)
        } ?: TextElementResult.empty
        val errorResult = handleErrorChildren(ctx?.children)
        val rt = r0 + errorResult
        return rt
    }

    override fun visitWbName(ctx: FormulaParser.WbNameContext?): TextElementResult {
        val rt = ctx?.let {
            ctx.noSpaceId()?.let {
                BasicTextElement(it.text, ctx.start.startIndex, ctx.stop.stopIndex)
            } ?: ctx.WITH_SPACE_ID()?.let {
                BasicTextElement(extractFromSingleQuote(it.text), ctx.start.startIndex, ctx.stop.stopIndex)
            }
        }?.let {
            TextElementResult.ferry(it)
        } ?: TextElementResult.empty
        return rt
    }

    override fun visitWbPath(ctx: FormulaParser.WbPathContext?): TextElementResult {
        val rt = ctx?.let {
            ctx.WITH_SPACE_ID()?.let {
                BasicTextElement(extractFromSingleQuote(it.text), ctx.start.startIndex, ctx.stop.stopIndex)
            }
        }?.let {
            TextElementResult.ferry(it)
        } ?: TextElementResult.empty
        return rt
    }

    override fun visitWbPrefixNoPath(ctx: FormulaParser.WbPrefixNoPathContext?): TextElementResult {
        return ctx?.let {
            val visiWbRs = visitWbName(ctx.wbName())
            visiWbRs.ferryBasicTextElement?.let {
                WbElement(
                    label = ctx.text,
                    wbName = it.text,
                    wbPath = null,
                    range = ctx.start.startIndex..ctx.stop.stopIndex
                )
            }
        }?.let {
            TextElementResult.ferry(it)
        } ?: TextElementResult.empty
    }

    override fun visitWbPrefixWithPath(ctx: FormulaParser.WbPrefixWithPathContext?): TextElementResult {
        val rt = ctx?.let {
            val wbn = visitWbName(ctx.wbName()).ferryBasicTextElement
            wbn?.let {
                val path = visitWbPath(ctx.wbPath()).ferryBasicTextElement
                WbElement(
                    label = ctx.text,
                    wbName = wbn.text,
                    wbPath = path?.text,
                    range = ctx.start.startIndex..ctx.stop.stopIndex
                )
            }
        }?.let {
            TextElementResult.ferry(it)
        } ?: TextElementResult.empty
        return rt
    }

    override fun visitWbPrefix(ctx: FormulaParser.WbPrefixContext?): TextElementResult {
        val r0 = ctx?.let {
            BasicTextElement(ctx.text, ctx.start.startIndex, ctx.stop.stopIndex)
            val q = ctx.wbPrefixNoPath()?.let {
                visitWbPrefixNoPath(it)
            } ?: ctx.wbPrefixWithPath()?.let {
                visitWbPrefixWithPath(it)
            }
            val wbEle = q?.ferryWbElement
            wbEle
        }?.let {
            TextElementResult.ferry(it)
        } ?: TextElementResult.empty
        val errorResult = handleErrorChildren(ctx?.children)
        val rt = r0 + errorResult
        return rt
    }

    override fun visitFullRangeAddress(ctx: FormulaParser.FullRangeAddressContext?): TextElementResult {
        val l = ctx?.let { fullRangeContext ->
            val rangeAddressContext = fullRangeContext.rangeAddress()
            val rangeAddressResult: TextElementResult? = this.visit(rangeAddressContext)
            rangeAddressResult?.let {
                it.ferryBasicTextElement?.let { r ->
                    val wsSuffixResult: TextElementResult? =
                        fullRangeContext.sheetPrefix()?.let { this.visitSheetPrefix(it) }
                    val wbSuffixResult: TextElementResult? = fullRangeContext.wbPrefix()?.let { this.visitWbPrefix(it) }
                    CellRangeElement(
                        rangeAddress = r,
                        wsSuffix = wsSuffixResult?.ferryWsNameElement,
                        wbSuffix = wbSuffixResult?.ferryWbElement
                    )
                }
            }
        }?.let {
            TextElementResult.from(it)
        } ?: TextElementResult.empty
        val errorResult = handleErrorChildren(ctx?.children)
        val rt = l + errorResult
        return rt
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
                TextElementResult.from(BasicTextElement.from(ctx.op))
            }
        } ?: TextElementResult.empty
        val expr0 = this.visit(ctx?.expr(0)) ?: TextElementResult.empty
        val expr1 = this.visit(ctx?.expr(1)) ?: TextElementResult.empty
        val errorResult = handleErrorChildren(ctx?.children)
        return expr0 + operator + expr1 + errorResult
    }

    override fun visitErrorNode(node: ErrorNode?): TextElementResult {
        val rt = node?.text?.let {
            BasicTextElement(
                text = it,
                range = node.symbol.startIndex..node.symbol.stopIndex
            ).toResult()
        } ?: TextElementResult.empty
        return rt
    }

    override fun visitLiteral(ctx: FormulaParser.LiteralContext?): TextElementResult {
        val rt = ctx?.let {
            BasicTextElement.from(it)
        } ?: BasicTextElement.empty
        return TextElementResult.from(rt)
    }

    override fun visitParenExpr(ctx: FormulaParser.ParenExprContext?): TextElementResult {
        val cRs = (this.visit(ctx?.openParen()) ?: TextElementResult.empty) +
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
                    TextElementResult.from(BasicTextElement.from(ctx.op))
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
        val errorResult = handleErrorChildren(ctx?.children)
        return v + errorResult
    }

    override fun visitOpenParen(ctx: FormulaParser.OpenParenContext?): TextElementResult {
        val rt = ctx?.let {
            TextElementResult.from(BasicTextElement.from(ctx))
        } ?: TextElementResult.empty
        return rt
    }

    override fun visitCloseParen(ctx: FormulaParser.CloseParenContext?): TextElementResult {
        val rt = ctx?.let {
            TextElementResult.from(BasicTextElement.from(ctx))
        } ?: TextElementResult.empty
        return rt
    }

    override fun visitStartFormulaSymbol(ctx: FormulaParser.StartFormulaSymbolContext?): TextElementResult {
        val rt = ctx?.let {
            TextElementResult.from(BasicTextElement.from(ctx))
        } ?: TextElementResult.empty
        return rt
    }

    override fun visitComma(ctx: FormulaParser.CommaContext?): TextElementResult {
        val rt = ctx?.let {
            TextElementResult.from(BasicTextElement.from(ctx))
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
                TextElementResult.from(BasicTextElement.from(ctx.op))
            }
        } ?: TextElementResult.empty
        val expr0 = this.visit(ctx?.expr(0)) ?: TextElementResult.empty
        val expr1 = this.visit(ctx?.expr(1)) ?: TextElementResult.empty
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
            TextElementResult.from(BasicTextElement.from(it))
        } ?: TextElementResult.empty
    }

    private fun handleCellAddressNode(ctx: FormulaParser.CellAddressContext?):String{
        val terminalNode = ctx?.CELL_LIKE_ADDRESS()
        when(terminalNode){
            is ErrorNode, null -> {
                return ""
            }
            else -> {
                return terminalNode.text
            }
        }
    }
    override fun visitRangeAsPairCellAddress(ctx: FormulaParser.RangeAsPairCellAddressContext?): TextElementResult {
        val rt = ctx?.let {
//            val c1 = ctx.cellAddress(0)?.text ?: ""
            val c1 = handleCellAddressNode(ctx.cellAddress(0))
            val op = ctx.getChild(1)?.text ?: ""
//            val c2 = ctx.cellAddress(1)?.text ?: ""
            val c2 = handleCellAddressNode(ctx.cellAddress(1))
            val label = "${c1}${op}${c2}"
            TextElementResult.ferry(BasicTextElement(label, ctx.start.startIndex..ctx.stop.stopIndex))
        } ?: TextElementResult.empty
        val errorResult = handleErrorChildren(ctx?.children)
        return rt + errorResult
    }

    override fun visitRangeAsOneCellAddress(ctx: FormulaParser.RangeAsOneCellAddressContext?): TextElementResult {
        val rt = ctx?.let {
            val c1 = ctx.cellAddress()?.text ?: ""
            TextElementResult.ferry(BasicTextElement(c1, ctx.start.startIndex..ctx.stop.stopIndex))
        } ?: TextElementResult.empty
        val errorResult = handleErrorChildren(ctx?.children)
        return rt + errorResult
    }

    override fun visitRangeAsColAddress(ctx: FormulaParser.RangeAsColAddressContext?): TextElementResult {
        val rt = ctx?.let {
            val c1 = ctx.ID_LETTERS(0)?.text ?: ""
            val op = ctx.getChild(1)?.text ?: ""
            val c2 = ctx.ID_LETTERS(1)?.text ?: ""
            val label = "${c1}${op}${c2}"
            TextElementResult.ferry(BasicTextElement(label, ctx.start.startIndex..ctx.stop.stopIndex))
        } ?: TextElementResult.empty
        val errorResult = handleErrorChildren(ctx?.children)
        return rt + errorResult
    }

    override fun visitRangeAsRowAddress(ctx: FormulaParser.RangeAsRowAddressContext?): TextElementResult {
        val rt = ctx?.let {
            val c1 = ctx.INT(0)?.text ?: ""
            val op = ctx.getChild(1)?.text ?: ""
            val c2 = ctx.INT(1)?.text ?: ""
            val label = "${c1}${op}${c2}"
            TextElementResult.ferry(BasicTextElement(label, ctx.start.startIndex..ctx.stop.stopIndex))
        } ?: TextElementResult.empty
        val errorResult = handleErrorChildren(ctx?.children)
        return rt + errorResult
    }

    override fun visitRangeInparens(ctx: FormulaParser.RangeInparensContext?): TextElementResult {
        var rt = (this.visit(ctx?.openParen()) ?: TextElementResult.empty) +
                (this.visit(ctx?.closeParen()) ?: TextElementResult.empty)
        val raRs = this.visit(ctx?.rangeAddress()) ?: TextElementResult.empty
        rt += raRs
        return rt
    }

    override fun visitLit(ctx: FormulaParser.LitContext?): TextElementResult {
        return ctx?.let {
            TextElementResult.from(BasicTextElement.from(it))
        } ?: TextElementResult.empty
    }
}
