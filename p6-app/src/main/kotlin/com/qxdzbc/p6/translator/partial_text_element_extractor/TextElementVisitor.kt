package com.qxdzbc.p6.translator.partial_text_element_extractor

import com.qxdzbc.p6.formula.translator.antlr.FormulaBaseVisitor
import com.qxdzbc.p6.formula.translator.antlr.FormulaParser
import com.qxdzbc.p6.translator.partial_text_element_extractor.text_element.*
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.Token
import org.antlr.v4.runtime.tree.ErrorNode
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.RuleNode
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

    private fun handleErrorChild(children: ErrorNode): TextElementResult {
        val rt = visitErrorNode(children)
        return rt
    }

    override fun visitZFormula(ctx: FormulaParser.ZFormulaContext?): TextElementResult {
        val equalSign = visitStartFormulaSymbol(ctx?.startFormulaSymbol())
        val exprRs = this.visit(ctx?.expr()) ?: TextElementResult.empty
        val EOFRs = visitTerminal(ctx?.EOF())
        val rt = equalSign + exprRs + EOFRs
        return rt
    }

    override fun visitInvokation(ctx: FormulaParser.InvokationContext?): TextElementResult {
        val t1 = visitFullRangeAddress(ctx?.fullRangeAddress())
        val t2 = visitFunctionCall(ctx?.functionCall())
        val rt = t1 + t2
        return rt
    }


    override fun visitOpenParen(ctx: FormulaParser.OpenParenContext?): TextElementResult {
        return handleSingleCharNode(ctx)
    }


    override fun visitCloseParen(ctx: FormulaParser.CloseParenContext?): TextElementResult {
        return handleSingleCharNode(ctx)
    }

    override fun visitLiteral(ctx: FormulaParser.LiteralContext?): TextElementResult {
        return this.visitLit(ctx?.lit())
    }

    override fun visitUnSubExpr(ctx: FormulaParser.UnSubExprContext?): TextElementResult {
        val textElementResult = ctx?.let {
            val opResult = ctx.op?.let {
                BasicTextElement.from(it).toResult()
            } ?: TextElementResult.empty
            val exprRs = this.visit(ctx.expr()) ?: TextElementResult.empty
            opResult + exprRs
        } ?: TextElementResult.empty
//        val errorResult = handleErrorChildren(ctx?.children)
//        val rt = textElementResult + errorResult
//        return rt
        return textElementResult
    }

    override fun defaultResult(): TextElementResult {
        return TextElementResult.empty
    }

    override fun shouldVisitNextChild(node: RuleNode?, currentResult: TextElementResult?): Boolean {
        return true
    }

    override fun aggregateResult(aggregate: TextElementResult?, nextResult: TextElementResult?): TextElementResult {
        return aggregate?.let {
            nextResult?.let {
                aggregate + nextResult
            } ?: aggregate
        } ?: TextElementResult.empty
    }

    override fun visitMulDivModExpr(ctx: FormulaParser.MulDivModExprContext?): TextElementResult {
        val operator = ctx?.op?.let { op ->
            BasicTextElement.from(op).toResult()
        } ?: TextElementResult.empty
        val expr0 = this.visit(ctx?.expr(0)) ?: TextElementResult.empty
        val expr1 = this.visit(ctx?.expr(1)) ?: TextElementResult.empty
        val errorResult = handleErrorChildren(ctx?.children)
        val rt = expr0 + operator + expr1 + errorResult
        return rt
    }

    override fun visitAddSubExpr(ctx: FormulaParser.AddSubExprContext?): TextElementResult {
        val operator = ctx?.op?.let { op ->
            BasicTextElement.from(ctx.op).toResult()
        } ?: TextElementResult.empty
        val expr0 = this.visit(ctx?.expr(0)) ?: TextElementResult.empty
        val expr1 = this.visit(ctx?.expr(1)) ?: TextElementResult.empty
        val errorResult = handleErrorChildren(ctx?.children)
        val rt = expr0 + operator + expr1 + errorResult
        return rt
    }

    override fun visitPowExpr(ctx: FormulaParser.PowExprContext?): TextElementResult {
        val v = ctx?.let {

            val operatorResult = ctx.op?.let { op ->
                TextElementResult.from(BasicTextElement.from(op))
            } ?: TextElementResult.empty

            val expr0Result = this.visit(ctx.expr(0)) ?: TextElementResult.empty

            val expr1Result = this.visit(ctx.expr(1)) ?: TextElementResult.empty

            expr0Result + operatorResult + expr1Result
        } ?: TextElementResult.empty
        val errorResult = handleErrorChildren(ctx?.children)
        val rt = v + errorResult
        return rt
    }


    override fun visitNotExpr(ctx: FormulaParser.NotExprContext?): TextElementResult {
        val textElementResult = ctx?.let {
            val opResult = ctx.op?.let {
                TextElementResult.from(BasicTextElement.from(it))
            } ?: TextElementResult.empty
            val exprRs = this.visit(ctx.expr()) ?: TextElementResult.empty
            opResult + exprRs
        } ?: TextElementResult.empty
//        val errorResult = handleErrorChildren(ctx?.children)
//        val rt = textElementResult + errorResult
//        return rt
        return textElementResult
    }

    override fun visitAndOrExpr(ctx: FormulaParser.AndOrExprContext?): TextElementResult {
        val operator = ctx?.op?.let { op ->
            BasicTextElement.from(op).toResult()
        } ?: TextElementResult.empty
        val expr0 = this.visit(ctx?.expr(0)) ?: TextElementResult.empty
        val expr1 = this.visit(ctx?.expr(1)) ?: TextElementResult.empty
        val errorResult = handleErrorChildren(ctx?.children)
        val rt = expr0 + operator + expr1 + errorResult
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

    /**
     * This one produces a temporary result obj ferrying temporary text elements to the next parsing function. ([visitWbPrefixNoPath])
     */
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

    /**
     * This one produces a temporary result obj ferrying temporary text elements to the next parsing function. ([visitWbPrefix])
     */
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

    /**
     * This one produces a temporary result obj ferrying temporary text elements to the next parsing function. ([visitFullRangeAddress])
     */
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
        val fullRangeAddressResult = ctx?.let {

            val rangeAddressResult: TextElementResult = this.visit(ctx.rangeAddress()) ?: TextElementResult.empty

            rangeAddressResult.ferryBasicTextElement?.let { r ->
                val wsSuffixResult: TextElementResult? = ctx.sheetPrefix()?.let { this.visitSheetPrefix(it) }
                val wbSuffixResult: TextElementResult? = ctx.wbPrefix()?.let { this.visitWbPrefix(it) }
                CellRangeElement(
                    rangeAddress = r,
                    wsSuffix = wsSuffixResult?.ferryWsNameElement,
                    wbSuffix = wbSuffixResult?.ferryWbElement
                )
            }

        }?.let {
            TextElementResult.from(it)
        } ?: TextElementResult.empty
        val errorResult = handleErrorChildren(ctx?.children)
        val rt = fullRangeAddressResult + errorResult
        return rt
    }

    /**
     * parse err node: ErrorNode is parse into [ErrTextElement] and stored separately from other text elements.
     */
    override fun visitErrorNode(node: ErrorNode?): TextElementResult {
        val token = node?.symbol
        if(token!=null && token.hasValidRange()){
            return BasicTextElement.from(token).toResult()
        }else{
            val rt = node?.text?.let {
                ErrTextElement(
                    text = it,
                    range = node.symbol.startIndex..node.symbol.stopIndex
                ).toResult()
            } ?: TextElementResult.empty
            return rt
        }
    }

    override fun visitParenExpr(ctx: FormulaParser.ParenExprContext?): TextElementResult {
        val openParenResult = this.visit(ctx?.openParen()) ?: TextElementResult.empty
        val exprResult = this.visit(ctx?.expr()) ?: TextElementResult.empty
        val closeParentResult = this.visit(ctx?.closeParen()) ?: TextElementResult.empty
        val errorResult = handleErrorChildren(ctx?.children) // TODO ??
        val rt = openParenResult + exprResult + closeParentResult + errorResult
        return rt
    }

    override fun visitTerminal(node: TerminalNode?): TextElementResult {
        if (node?.symbol?.type == FormulaParser.EOF) {
            return EOFTextElement.toResult()
        }
        val rt = node?.symbol?.let {
            BasicTextElement.from(it).toResult()
        } ?: TextElementResult.empty
        return rt
    }

    override fun visitInvokeExpr(ctx: FormulaParser.InvokeExprContext?): TextElementResult {
        val rt = this.visitInvokation(ctx?.invokation())
        return rt
    }


    override fun visitCellAddress(ctx: FormulaParser.CellAddressContext?): TextElementResult {
        return visitTerminal(ctx?.CELL_LIKE_ADDRESS())
    }

    override fun visitNoSpaceId(ctx: FormulaParser.NoSpaceIdContext?): TextElementResult {
        val t1 = visitTerminal(ctx?.CELL_LIKE_ADDRESS())
        val t2 = visitTerminal(ctx?.NO_SPACE_ID())
        val rt = t1 + t2
        return rt
    }

    override fun visitStartFormulaSymbol(ctx: FormulaParser.StartFormulaSymbolContext?): TextElementResult {
        return handleSingleCharNode(ctx)
    }

    /**
     * For handling single-character node. These nodes have exactly 1 child node which is a terminal node.
     */
    private fun handleSingleCharNode(ctx: ParserRuleContext?): TextElementResult {
        val exception = ctx?.exception
        if (exception != null) {
            val rt = ErrTextElement.fromException(exception).toResult()
            return rt
        } else {
            val onlyChild: ParseTree? = ctx?.getChild(0)
            val textRs = when (onlyChild) {
                is ErrorNode -> visitErrorNode(onlyChild)
                is TerminalNode -> visitTerminal(onlyChild)
                else -> TextElementResult.empty
            }
            val rt = textRs
            return rt
        }
    }

    override fun visitComma(ctx: FormulaParser.CommaContext?): TextElementResult {
        return handleSingleCharNode(ctx)
    }

    override fun visitBoolOperation(ctx: FormulaParser.BoolOperationContext?): TextElementResult {
        val operator = ctx?.op?.let { op ->
            BasicTextElement.from(ctx.op).toResult()
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
            BasicTextElement.from(it).toResult()
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

    /**
     * This one produce a temporary result obj ferrying temporary text elements to the next parsing function. ([visitFullRangeAddress])
     */
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

    /**
     * This one produce a temporary result obj ferrying temporary text elements to the next parsing function. ([visitFullRangeAddress])
     */
    override fun visitRangeAsOneCellAddress(ctx: FormulaParser.RangeAsOneCellAddressContext?): TextElementResult {
        val rs1 = ctx?.let {
            val c1 = ctx.cellAddress()?.text ?: ""
            TextElementResult.ferry(BasicTextElement(c1, ctx.start.startIndex..ctx.stop.stopIndex))
        } ?: TextElementResult.empty
        val errorResult = handleErrorChildren(ctx?.children)
        val rt = rs1 + errorResult
        return rt
    }

    /**
     * This one produce a temporary result obj ferrying temporary text elements to the next parsing function. ([visitFullRangeAddress])
     */
    override fun visitRangeAsColAddress(ctx: FormulaParser.RangeAsColAddressContext?): TextElementResult {
        val textElementResult = ctx?.let {
            val c1 = ctx.ID_LETTERS(0)?.text ?: ""
            val op = ctx.getChild(1)?.text ?: ""
            val c2 = ctx.ID_LETTERS(1)?.text ?: ""
            val label = "${c1}${op}${c2}"
            TextElementResult.ferry(BasicTextElement(label, ctx.start.startIndex..ctx.stop.stopIndex))
        } ?: TextElementResult.empty
        val errorResult = handleErrorChildren(ctx?.children)
        val rt = textElementResult + errorResult
        return rt
    }

    /**
     * This one produce a temporary result obj ferrying temporary text elements to the next parsing function. ([visitFullRangeAddress])
     */
    override fun visitRangeAsRowAddress(ctx: FormulaParser.RangeAsRowAddressContext?): TextElementResult {
        val textElementResult = ctx?.let {
            val c1 = ctx.INT(0)?.text ?: ""
            val op = ctx.getChild(1)?.text ?: ""
            val c2 = ctx.INT(1)?.text ?: ""
            val label = "${c1}${op}${c2}"
            TextElementResult.ferry(BasicTextElement(label, ctx.start.startIndex..ctx.stop.stopIndex))
        } ?: TextElementResult.empty
        val errorResult = handleErrorChildren(ctx?.children)
        val rt = textElementResult + errorResult
        return rt
    }

    override fun visitRangeInparens(ctx: FormulaParser.RangeInparensContext?): TextElementResult {
        val raRs = this.visit(ctx?.rangeAddress()) ?: TextElementResult.empty
        var rt =
            (this.visit(ctx?.openParen()) ?: TextElementResult.empty) +
                    raRs +
                    (this.visit(ctx?.closeParen()) ?: TextElementResult.empty)

        rt += raRs
        val errorResult = handleErrorChildren(ctx?.children)
        rt += errorResult
        return rt
    }

    override fun visitLit(ctx: FormulaParser.LitContext?): TextElementResult {
        val textElementResult = ctx?.let {
            TextElementResult.from(BasicTextElement.from(it))
        } ?: TextElementResult.empty
        val errorResult = handleErrorChildren(ctx?.children)
        val rt = textElementResult + errorResult
        return rt
    }

    companion object{
        private fun Token.hasValidRange():Boolean{
            return this.startIndex >=0 && this.stopIndex>=0
        }
    }
}
