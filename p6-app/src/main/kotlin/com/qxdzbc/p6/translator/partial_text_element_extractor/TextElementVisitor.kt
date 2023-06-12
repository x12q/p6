package com.qxdzbc.p6.translator.partial_text_element_extractor

import com.qxdzbc.p6.formula.translator.antlr.FormulaBaseVisitor
import com.qxdzbc.p6.formula.translator.antlr.FormulaParser
import com.qxdzbc.p6.translator.partial_text_element_extractor.text_element.*
import org.antlr.v4.runtime.tree.ErrorNode
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.TerminalNode
import javax.inject.Inject

/**
 * A visitor that extract elements in a parse tree (created by parsing a text formula) into [TextElementResult]
 */
class TextElementVisitor @Inject constructor() : FormulaBaseVisitor<TextElementResult>() {

    /**
     * For dealing with error children in a context obj, can be used for any context.
     */
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
        val textElementResult = ctx?.let {
            val opRs = ctx.op?.let {
                TextElementResult.from(BasicTextElement.from(it))
            } ?: TextElementResult.empty
            val exprRs = this.visit(ctx.expr()) ?: TextElementResult.empty
            opRs + exprRs
        } ?: TextElementResult.empty
        val errorResult = handleErrorChildren(ctx?.children)
        val rt = textElementResult + errorResult
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
                TextElementResult.from(BasicTextElement.from(ctx.op))
            }
        } ?: TextElementResult.empty
        val expr0 = this.visit(ctx?.expr(0)) ?: TextElementResult.empty
        val expr1 = this.visit(ctx?.expr(1)) ?: TextElementResult.empty
        val errorResult = handleErrorChildren(ctx?.children)
        val rt = expr0 + operator + expr1 + errorResult
        return rt
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
        val rt = expr0 + operator + expr1 + errorResult
        return rt
    }

    override fun visitUnSubExpr(ctx: FormulaParser.UnSubExprContext?): TextElementResult {
        val textElementResult = ctx?.let {
            val opRs = ctx.op?.let {
                TextElementResult.from(BasicTextElement.from(it))
            } ?: TextElementResult.empty
            val exprRs = this.visit(ctx.expr()) ?: TextElementResult.empty
            opRs + exprRs
        } ?: TextElementResult.empty
        val errorResult = handleErrorChildren(ctx?.children)
        val rt = textElementResult + errorResult
        return rt
    }

    private fun extractFromSingleQuote(str: String): String {
        return str.substring(1, str.length - 1)
    }

    override fun visitSheetNameWithSpace(ctx: FormulaParser.SheetNameWithSpaceContext?): TextElementResult {
        val textElementResult = ctx?.let {
            WsNameElement(
                label = ctx.text,
                wsName = extractFromSingleQuote(ctx.text),
                ctx.start.startIndex..ctx.stop.stopIndex
            )
        }?.let {
            TextElementResult.ferry(it)
        } ?: TextElementResult.empty
        val errorResult = handleErrorChildren(ctx?.children)
        val rt = textElementResult + errorResult
        return rt
    }

    override fun visitSheetName(ctx: FormulaParser.SheetNameContext?): TextElementResult {
        val textElementResult = ctx?.let {
            WsNameElement(label = ctx.text, wsName = ctx.text, ctx.start.startIndex..ctx.stop.stopIndex)
        }?.let {
            TextElementResult.ferry(it)
        } ?: TextElementResult.empty
        val errorResult = handleErrorChildren(ctx?.children)
        val rt = textElementResult + errorResult
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
        val textElementResult = ctx?.let {
            ctx.noSpaceId()?.let {
                BasicTextElement(it.text, ctx.start.startIndex, ctx.stop.stopIndex)
            } ?: ctx.WITH_SPACE_ID()?.let {
                BasicTextElement(extractFromSingleQuote(it.text), ctx.start.startIndex, ctx.stop.stopIndex)
            }
        }?.let {
            TextElementResult.ferry(it)
        } ?: TextElementResult.empty
        val errorResult = handleErrorChildren(ctx?.children)
        val rt = textElementResult + errorResult
        return rt
    }

    override fun visitWbPath(ctx: FormulaParser.WbPathContext?): TextElementResult {
        val textElementResult = ctx?.let {
            ctx.WITH_SPACE_ID()?.let {
                BasicTextElement(extractFromSingleQuote(it.text), ctx.start.startIndex, ctx.stop.stopIndex)
            }
        }?.let {
            TextElementResult.ferry(it)
        } ?: TextElementResult.empty
        val errorResult = handleErrorChildren(ctx?.children)
        val rt = textElementResult + errorResult
        return rt
    }

    override fun visitWbPrefixNoPath(ctx: FormulaParser.WbPrefixNoPathContext?): TextElementResult {
        val textElementResult = ctx?.let {
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
        val errorResult = handleErrorChildren(ctx?.children)
        val rt = textElementResult + errorResult
        return rt
    }

    override fun visitWbPrefixWithPath(ctx: FormulaParser.WbPrefixWithPathContext?): TextElementResult {
        val textElementResult = ctx?.let {
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
        val errResult = handleErrorChildren(ctx?.children)
        val rt = textElementResult + errResult
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
        val rt = expr0 + operator + expr1 + errorResult
        return rt
    }

    /**
     * parse err node: ErrorNode is parse into [ErrTextElement] and stored separately from other text elements.
     */
    override fun visitErrorNode(node: ErrorNode?): TextElementResult {
        val rt = node?.text?.let {
            ErrTextElement(
                text = it,
                range = node.symbol.startIndex..node.symbol.stopIndex
            ).toResult()
        } ?: TextElementResult.empty
        return rt
    }

    override fun visitLiteral(ctx: FormulaParser.LiteralContext?): TextElementResult {
        val textElementResult = ctx?.let {
            BasicTextElement.from(it).toResult()
        } ?: TextElementResult.empty
        val errorResult = handleErrorChildren(ctx?.children)
        val rt = textElementResult + errorResult
        return rt
    }

    override fun visitParenExpr(ctx: FormulaParser.ParenExprContext?): TextElementResult {
        val textElementResult = (this.visit(ctx?.openParen()) ?: TextElementResult.empty) +
                (this.visit(ctx?.expr()) ?: TextElementResult.empty) +
                (this.visit(ctx?.closeParen()) ?: TextElementResult.empty)
        val errorResult = handleErrorChildren(ctx?.children)
        val rt = textElementResult + errorResult
        return rt
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
        val rt = v + errorResult
        return rt
    }

    override fun visitOpenParen(ctx: FormulaParser.OpenParenContext?): TextElementResult {
//        val errorResult = handleErrorChildren(ctx?.children)
//        if(errorResult.errs.isNotEmpty()){
//            return errorResult
//        }else{
//            val textElementResult = ctx?.let {
//                TextElementResult.from(BasicTextElement.from(ctx))
//            } ?: TextElementResult.empty
//            return textElementResult
//        }
        var rt = TextElementResult.empty
        ctx?.children?.forEach {
            this.visit(it)?.also{
                rt+=it
            }
        }
        return rt
    }

    override fun visitTerminal(node: TerminalNode?): TextElementResult {
        val rt = node?.symbol?.let{
            BasicTextElement.from(it).toResult()
        }?: TextElementResult.empty
        return rt
    }

    override fun visitCloseParen(ctx: FormulaParser.CloseParenContext?): TextElementResult {
        val textElementResult = ctx?.let {
            TextElementResult.from(BasicTextElement.from(ctx))
        } ?: TextElementResult.empty
        val errorResult = handleErrorChildren(ctx?.children)
        val rt = textElementResult + errorResult
        return rt
    }



    override fun visitStartFormulaSymbol(ctx: FormulaParser.StartFormulaSymbolContext?): TextElementResult {
        val textElementResult = ctx?.let {
            TextElementResult.from(BasicTextElement.from(ctx))
        } ?: TextElementResult.empty
        val errorResult = handleErrorChildren(ctx?.children)
        val rt = textElementResult + errorResult
        return rt
    }

    override fun visitComma(ctx: FormulaParser.CommaContext?): TextElementResult {
        val textElementResult = ctx?.let {
            TextElementResult.from(BasicTextElement.from(ctx))
        } ?: TextElementResult.empty
        val errorResult = handleErrorChildren(ctx?.children)
        val rt = textElementResult + errorResult
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
        val errorResult = handleErrorChildren(ctx?.children)
        val rt = expr0 + operator + expr1 + errorResult
        return rt
    }

    override fun visitFunctionCall(ctx: FormulaParser.FunctionCallContext?): TextElementResult {
        var textElementResult = TextElementResult.empty
        if (ctx != null) {
            for (child in ctx.children) {
                val q = this.visit(child)
                if (q != null) {
                    textElementResult += q
                }
            }
        }
        val errorResult = handleErrorChildren(ctx?.children)
        val rt = textElementResult + errorResult
        return rt
    }


    override fun visitFunctionName(ctx: FormulaParser.FunctionNameContext?): TextElementResult {
        val textElementResult = ctx?.let {
            TextElementResult.from(BasicTextElement.from(it))
        } ?: TextElementResult.empty
        val errorResult = handleErrorChildren(ctx?.children)
        val rt = textElementResult + errorResult
        return rt
    }

    private fun handleCellAddressNode(ctx: FormulaParser.CellAddressContext?): String {
        val terminalNode = ctx?.CELL_LIKE_ADDRESS()
        when (terminalNode) {
            is ErrorNode, null -> {
                return ""
            }

            else -> {
                return terminalNode.text
            }
        }
    }

    override fun visitRangeAsPairCellAddress(ctx: FormulaParser.RangeAsPairCellAddressContext?): TextElementResult {
        val textElementResult = ctx?.let {
            val c1 = handleCellAddressNode(ctx.cellAddress(0))
            val op = ctx.getChild(1)?.text ?: ""
            val c2 = handleCellAddressNode(ctx.cellAddress(1))
            val label = "${c1}${op}${c2}"
            TextElementResult.ferry(BasicTextElement(label, ctx.start.startIndex..ctx.stop.stopIndex))
        } ?: TextElementResult.empty
        val errorResult = handleErrorChildren(ctx?.children)
        val rt = textElementResult + errorResult
        return rt
    }

    override fun visitRangeAsOneCellAddress(ctx: FormulaParser.RangeAsOneCellAddressContext?): TextElementResult {
        val rs1 = ctx?.let {
            val c1 = ctx.cellAddress()?.text ?: ""
            TextElementResult.ferry(BasicTextElement(c1, ctx.start.startIndex..ctx.stop.stopIndex))
        } ?: TextElementResult.empty
        val errorResult = handleErrorChildren(ctx?.children)
        val rt = rs1 + errorResult
        return rt
    }

    override fun visitRangeAsColAddress(ctx: FormulaParser.RangeAsColAddressContext?): TextElementResult {
        val textElementResult = ctx?.let {
            val c1 = ctx.ID_LETTERS(0)?.text ?: ""
            val op = ctx.getChild(1)?.text ?: ""
            val c2 = ctx.ID_LETTERS(1)?.text ?: ""
            val label = "${c1}${op}${c2}"
            TextElementResult.ferry(BasicTextElement(label, ctx.start.startIndex..ctx.stop.stopIndex))
        } ?: TextElementResult.empty
        val errorResult = handleErrorChildren(ctx?.children)
        val rt= textElementResult + errorResult
        return rt
    }

    override fun visitRangeAsRowAddress(ctx: FormulaParser.RangeAsRowAddressContext?): TextElementResult {
        val textElementResult = ctx?.let {
            val c1 = ctx.INT(0)?.text ?: ""
            val op = ctx.getChild(1)?.text ?: ""
            val c2 = ctx.INT(1)?.text ?: ""
            val label = "${c1}${op}${c2}"
            TextElementResult.ferry(BasicTextElement(label, ctx.start.startIndex..ctx.stop.stopIndex))
        } ?: TextElementResult.empty
        val errorResult = handleErrorChildren(ctx?.children)
        val rt= textElementResult + errorResult
        return rt
    }

    override fun visitRangeInparens(ctx: FormulaParser.RangeInparensContext?): TextElementResult {
        var rt = (this.visit(ctx?.openParen()) ?: TextElementResult.empty) +
                (this.visit(ctx?.closeParen()) ?: TextElementResult.empty)
        val raRs = this.visit(ctx?.rangeAddress()) ?: TextElementResult.empty
        rt += raRs
        val errorResult = handleErrorChildren(ctx?.children)
        rt +=errorResult
        return rt
    }

    override fun visitLit(ctx: FormulaParser.LitContext?): TextElementResult {
        val textElementResult= ctx?.let {
            TextElementResult.from(BasicTextElement.from(it))
        } ?: TextElementResult.empty
        val errorResult = handleErrorChildren(ctx?.children)
        val rt = textElementResult + errorResult
        return rt
    }
}
