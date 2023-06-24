package com.qxdzbc.p6.translator.partial_text_element_extractor

import com.qxdzbc.p6.formula.translator.antlr.FormulaBaseVisitor
import com.qxdzbc.p6.formula.translator.antlr.FormulaParser
import com.qxdzbc.p6.translator.partial_text_element_extractor.text_element.*
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
     * Filter out error node in [children] list, and call [visitErrorNode] on them.
     * For dealing with error children in a context obj, can be used for any context.
     * This is a catch-all function that must be call at all visiting function, to catch all error node. This is necessary. For example:
     * "=^-" => is parse as a unary subtract operator, and "^" is part of that node. Without calling this function inside the unary sub operator visiting function, the "^" symbol will be missed.
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

    /**
     * If [node] is error, return an empty [TextElementResult],
     * else return whatever [ifNotErr] produce.
     */
    private fun <T : ParseTree> emptyIfErrNode(node: T?, ifNotErr: (T?) -> TextElementResult?): TextElementResult {
        val rt = when (node) {
            is ErrorNode -> TextElementResult.empty
            else -> ifNotErr(node)
        } ?: TextElementResult.empty
        return rt
    }

    override fun visitZFormula(ctx: FormulaParser.ZFormulaContext?): TextElementResult {
        val equalSign = emptyIfErrNode(ctx?.startFormulaSymbol()) {
            visitStartFormulaSymbol(it)
        }
        val exprRs = emptyIfErrNode(ctx?.expr()) {
            visit(it)
        }

        val EOFRs = emptyIfErrNode(ctx?.EOF()) {
            visitTerminal(it)
        }
        val errorResult = handleErrorChildren(ctx?.children)
        val rt = equalSign + exprRs + EOFRs + errorResult
        return rt
    }

    override fun visitInvokation(ctx: FormulaParser.InvokationContext?): TextElementResult {
        val t1 = emptyIfErrNode(ctx?.fullRangeAddress()) {
            visitFullRangeAddress(it)
        }
        val t2 = emptyIfErrNode(ctx?.functionCall()) {
            visitFunctionCall(it)
        }
        val errorResult = handleErrorChildren(ctx?.children)
        val rt = t1 + t2 + errorResult
        return rt
    }


    override fun visitOpenParen(ctx: FormulaParser.OpenParenContext?): TextElementResult {
        val opNode = ctx?.op
        val opResult = opNode?.hasValidRange()?.let { hasValidRange ->
            if (hasValidRange) {
                BasicTextElement.from(opNode).toResult()
            } else {
                null
            }
        } ?: TextElementResult.empty

        val errorResult = handleErrorChildren(ctx?.children)
        val rt = opResult + errorResult
        return rt
    }


    override fun visitCloseParen(ctx: FormulaParser.CloseParenContext?): TextElementResult {
        val opNode = ctx?.op
        val opResult = opNode?.hasValidRange()?.let { hasValidRange ->
            if (hasValidRange) {
                BasicTextElement.from(opNode).toResult()
            } else {
                null
            }
        } ?: TextElementResult.empty

        val errorResult = handleErrorChildren(ctx?.children)
        val rt = opResult + errorResult
        return rt
    }

    override fun visitLiteral(ctx: FormulaParser.LiteralContext?): TextElementResult {
        val errorResult = handleErrorChildren(ctx?.children)
        val rt = emptyIfErrNode(ctx?.lit()) { visitLit(it) } + errorResult
        return rt
    }

    override fun visitUnSubExpr(ctx: FormulaParser.UnSubExprContext?): TextElementResult {
        val textElementResult = ctx?.let {
            val opResult = ctx.op?.let {
                BasicTextElement.from(it).toResult()
            } ?: TextElementResult.empty
            val exprRs = emptyIfErrNode(ctx.expr()) { expr ->
                expr?.let { visit(it) }
            }
            opResult + exprRs
        } ?: TextElementResult.empty
        val errorResult = handleErrorChildren(ctx?.children)
        val rt = textElementResult + errorResult
        return rt
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

        val expr0 = emptyIfErrNode(ctx?.expr(0)) {
            it?.let { visit(it) }
        }
        val expr1 = emptyIfErrNode(ctx?.expr(1)) {
            it?.let { visit(it) }
        }
        val errorResult = handleErrorChildren(ctx?.children)
        val rt = expr0 + operator + expr1 + errorResult
        return rt
    }

    override fun visitAddSubExpr(ctx: FormulaParser.AddSubExprContext?): TextElementResult {
        val operator = ctx?.op?.let { op ->
            BasicTextElement.from(ctx.op).toResult()
        } ?: TextElementResult.empty

        val expr0 = emptyIfErrNode(ctx?.expr(0)) {
            it?.let { visit(it) }
        }

        val expr1 = emptyIfErrNode(ctx?.expr(1)) {
            it?.let { visit(it) }
        }

        val errorResult = handleErrorChildren(ctx?.children)
        val rt = expr0 + operator + expr1 + errorResult
        return rt
    }

    override fun visitPowExpr(ctx: FormulaParser.PowExprContext?): TextElementResult {
        val v = ctx?.let {

            val operatorResult = ctx.op?.let { op ->
                TextElementResult.from(BasicTextElement.from(op))
            } ?: TextElementResult.empty

            val expr0Result = emptyIfErrNode(ctx.expr(0)) {
                it?.let { visit(it) }
            }
            val expr1Result = emptyIfErrNode(ctx.expr(1)) {
                it?.let { visit(it) }
            }

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

            val exprRs = emptyIfErrNode(ctx.expr()) {
                it?.let { visit(it) }
            }

            opResult + exprRs
        } ?: TextElementResult.empty
        val errorResult = handleErrorChildren(ctx?.children)
        val rt = textElementResult + errorResult
        return rt
    }

    override fun visitAndOrExpr(ctx: FormulaParser.AndOrExprContext?): TextElementResult {
        val operator = ctx?.op?.let { op ->
            BasicTextElement.from(op).toResult()
        } ?: TextElementResult.empty


        val expr0 = emptyIfErrNode(ctx?.expr(0)) {
            it?.let { visit(it) }
        }

        val expr1 = emptyIfErrNode(ctx?.expr(1)) {
            it?.let { visit(it) }
        }

        val errorResult = handleErrorChildren(ctx?.children)
        val rt = expr0 + operator + expr1 + errorResult
        return rt
    }

    override fun visitSheetNameWithSpace(ctx: FormulaParser.SheetNameWithSpaceContext?): TextElementResult {
        val withSpaceIdRs = emptyIfErrNode(ctx?.WITH_SPACE_ID()) {
            visitTerminal(it)
        }

        val rs = if (withSpaceIdRs.hasNoErr() && withSpaceIdRs.hasExactlyOneBasicText()) {
            TextElementResult.ferry(withSpaceIdRs.basicTexts[0])
        } else {
            withSpaceIdRs
        }
        val errorResult = handleErrorChildren(ctx?.children)
        val rt = rs + errorResult
        return rt
    }

    override fun visitSheetName(ctx: FormulaParser.SheetNameContext?): TextElementResult {

        val noSpaceIdRs = emptyIfErrNode(ctx?.noSpaceId()) {
            visitNoSpaceId(it)
        }

        val rs = if (noSpaceIdRs.hasNoErr() && noSpaceIdRs.hasExactlyOneBasicText()) {
            TextElementResult.ferry(noSpaceIdRs.basicTexts[0])
        } else {
            noSpaceIdRs
        }

        val errorResult = handleErrorChildren(ctx?.children)
        val rt = rs + errorResult

        return rt

    }

    override fun visitSheetPrefix(ctx: FormulaParser.SheetPrefixContext?): TextElementResult {

        val sheetNameWithSpaceRs = emptyIfErrNode(ctx?.sheetNameWithSpace()) {
            visitSheetNameWithSpace(it)
        }

        val sheetNameRs = emptyIfErrNode(ctx?.sheetName()) {
            visitSheetName(it)
        }

        val ferriedSheetName = sheetNameWithSpaceRs.ferryBasicTextElement ?: sheetNameRs.ferryBasicTextElement

        val r0 = if (ferriedSheetName != null) {
            TextElementResult.ferry(
                WsNameElement(
                    fullText = ctx?.text ?: "",
                    wsName = ferriedSheetName.text,
                    textRange = ctx?.let {
                        it.start.startIndex..it.stop.stopIndex
                    } ?: (-1..-1)
                )
            )
        } else {
            sheetNameWithSpaceRs + sheetNameRs
        }
        val errorResult = handleErrorChildren(ctx?.children)
        val rt = r0 + errorResult
        return rt
    }

    /**
     * This one produces a temporary result obj ferrying temporary text elements to the next parsing function. ([visitWbPrefixNoPath])
     */
    override fun visitWbName(ctx: FormulaParser.WbNameContext?): TextElementResult {

        val r1 = emptyIfErrNode(ctx?.noSpaceId()) {
            visitNoSpaceId(it)
        }

        val r2 = emptyIfErrNode(ctx?.WITH_SPACE_ID()) {
            visitTerminal(it)
        }
        val r3 = (r1 + r2).flattenToBasicTextOrThis()
        val rt = if (r3.hasNoErr() && r3.hasExactlyOneBasicText()) {
            TextElementResult.ferry(r3.basicTexts[0])
        } else {
            r3
        }
        return rt
    }

    override fun visitWbPath(ctx: FormulaParser.WbPathContext?): TextElementResult {

        val rt = emptyIfErrNode(ctx?.WITH_SPACE_ID()) {
            visitTerminal(ctx?.WITH_SPACE_ID()).let {
                if (it.hasNoErr() && it.hasExactlyOneBasicText()) {
                    TextElementResult.ferry(it.basicTexts[0])
                } else {
                    it.flattenToBasicTextOrThis()
                }
            }
        }

        return rt
    }

    /**
     * This one produces a temporary result obj ferrying temporary text elements to the next parsing function. ([visitWbPrefix])
     */
    override fun visitWbPrefixNoPath(ctx: FormulaParser.WbPrefixNoPathContext?): TextElementResult {
        val textElementResult = ctx?.let {
            val visiWbRs = emptyIfErrNode(ctx.wbName()) { visitWbName(it) }
            visiWbRs.ferryBasicTextElement?.let {
                WbElement(
                    fullText = ctx.text,
                    wbName = it.text,
                    wbPath = null,
                    textRange = ctx.start.startIndex..ctx.stop.stopIndex
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
            val wbNameRs = emptyIfErrNode(ctx.wbName()) { visitWbName(it) }
            val wbPathRs = emptyIfErrNode(ctx.wbPath()) { visitWbPath(it) }

            val wbNameFerriedElement = wbNameRs.ferryBasicTextElement
            val wbPathFerriedElement = wbPathRs.ferryBasicTextElement

            if (wbNameFerriedElement != null && wbPathFerriedElement != null) {
                TextElementResult.ferry(
                    WbElement(
                        fullText = ctx.text,
                        wbName = wbNameFerriedElement.text,
                        wbPath = wbPathFerriedElement.text,
                        textRange = ctx.start.startIndex..ctx.stop.stopIndex
                    )
                )
            } else {
                (wbPathRs + wbNameRs).flattenToBasicTextOrThis()
            }
        } ?: TextElementResult.empty
        val errResult = handleErrorChildren(ctx?.children)
        val rt = textElementResult + errResult
        return rt
    }

    /**
     * This one produces a temporary result obj ferrying temporary text elements to the next parsing function. ([visitFullRangeAddress])
     */
    override fun visitWbPrefix(ctx: FormulaParser.WbPrefixContext?): TextElementResult {

        val withPathRs = emptyIfErrNode(ctx?.wbPrefixWithPath()) { visitWbPrefixWithPath(it) }
        val noPathRs = emptyIfErrNode(ctx?.wbPrefixNoPath()) { visitWbPrefixNoPath(it) }

        val withPathWbElement = withPathRs.ferryWbElement
        val noPathWbElement = noPathRs.ferryWbElement

        val wbElement = withPathWbElement ?: noPathWbElement

        val r0 = if (wbElement != null) {
            TextElementResult.ferry(wbElement)
        } else {
            (withPathRs + noPathRs).flattenToBasicTextOrThis()
        }
        val errorResult = handleErrorChildren(ctx?.children)
        val rt = r0 + errorResult
        return rt
    }

    override fun visitFullRangeAddress(ctx: FormulaParser.FullRangeAddressContext?): TextElementResult {
        val rangeAddressRs = emptyIfErrNode(ctx?.rangeAddress()) {
            it?.let { visit(it) }
        }

        val sheetPrefixRs = emptyIfErrNode(ctx?.sheetPrefix()) { visitSheetPrefix(it) }
        val wbPrefix = emptyIfErrNode(ctx?.wbPrefix()) { visitWbPrefix(it) }

        val ferriedRangeAddress = rangeAddressRs.ferryBasicTextElement
        val ferriedSheetSuffix = sheetPrefixRs.ferryWsNameElement
        val ferriedWbSuffix = wbPrefix.ferryWbElement

        val fullRangeAddressResult = if (ferriedRangeAddress != null) {
            TextElementResult.from(
                CellRangeElement(
                    rangeAddress = ferriedRangeAddress,
                    wsSuffix = ferriedSheetSuffix,
                    wbSuffix = ferriedWbSuffix,
                )
            )
        } else {
            val mergedRs = rangeAddressRs.moveFerriedElementsToBasicTexts() +
                    sheetPrefixRs.moveFerriedElementsToBasicTexts() +
                    wbPrefix.moveFerriedElementsToBasicTexts()

            mergedRs.flattenToBasicTextOrThis()
        }
        val errorResult = handleErrorChildren(ctx?.children)
        val rt = fullRangeAddressResult + errorResult
        return rt
    }

    /**
     * if the error node's text is not part of the original text -> ErrorNode is parse into [ErrTextElement] and stored separately from other text elements.
     *
     * If the error node's text is part of the original text -> parse it into a [BasicTextElement] and store it as a normal text elements.
     */
    override fun visitErrorNode(node: ErrorNode?): TextElementResult {
        val token = node?.symbol
        if (token != null && token.hasValidRange()) {
            return BasicTextElement.from(token).toErrResult()
        } else {
            val rt = node?.text?.let {
                ErrTextElement(
                    text = it,
                    textRange = node.symbol.startIndex..node.symbol.stopIndex
                ).toResult()
            } ?: TextElementResult.empty
            return rt
        }
    }

    override fun visitParenExpr(ctx: FormulaParser.ParenExprContext?): TextElementResult {
        val openParenResult = emptyIfErrNode(ctx?.openParen()) { it?.let { visit(it) } }

        val exprResult = emptyIfErrNode(ctx?.expr()) {
            it?.let { visit(it) }
        }
        val closeParentResult = emptyIfErrNode(ctx?.closeParen()) {
            it?.let { visit(it) }
        }
        val errorResult = handleErrorChildren(ctx?.children)
        val rt = openParenResult + exprResult + closeParentResult + errorResult
        return rt
    }

    override fun visitTerminal(node: TerminalNode?): TextElementResult {
        if (node?.symbol?.type == FormulaParser.EOF) {
            return EOFTextElement.toResult()
        }
        val rt = node?.symbol?.let {
            if (it.hasValidRange()) {
                BasicTextElement.from(it).toResult()
            } else {
                ErrTextElement.from(it).toResult()
            }
        } ?: TextElementResult.empty
        return rt
    }

    override fun visitInvokeExpr(ctx: FormulaParser.InvokeExprContext?): TextElementResult {
        // this error handling is necessary, because for "=+A1", the "+" is read in this ctx
        val errorResult = handleErrorChildren(ctx?.children)
        val rt = emptyIfErrNode(ctx?.invokation()) { visitInvokation(it) } + errorResult
        return rt
    }


    override fun visitCellAddress(ctx: FormulaParser.CellAddressContext?): TextElementResult {
        val errorResult = handleErrorChildren(ctx?.children)
        val cellLikeAddress = ctx?.CELL_LIKE_ADDRESS()
        val rt = emptyIfErrNode(cellLikeAddress) {
            when (it) {
                is TerminalNode -> visitTerminal(cellLikeAddress)
                else -> TextElementResult.empty
            }
        } + errorResult
        return rt
    }

    override fun visitNoSpaceId(ctx: FormulaParser.NoSpaceIdContext?): TextElementResult {
        val t1 = emptyIfErrNode(ctx?.CELL_LIKE_ADDRESS()) { visitTerminal(it) }
        val t2 = emptyIfErrNode(ctx?.NO_SPACE_ID()) { visitTerminal(it) }
        val errorResult = handleErrorChildren(ctx?.children)
        val rt = t1 + t2 + errorResult
        return rt
    }

    override fun visitStartFormulaSymbol(ctx: FormulaParser.StartFormulaSymbolContext?): TextElementResult {
        val opNode = ctx?.op
        val opResult = opNode?.hasValidRange()?.let { hasValidRange ->
            if (hasValidRange) {
                BasicTextElement.from(opNode).toResult()
            } else {
                null
            }
        } ?: TextElementResult.empty

        val errorResult = handleErrorChildren(ctx?.children)
        val rt = opResult + errorResult
        return rt
    }

    override fun visitComma(ctx: FormulaParser.CommaContext?): TextElementResult {
        val opNode = ctx?.op
        val opResult = opNode?.hasValidRange()?.let { hasValidRange ->
            if (hasValidRange) {
                BasicTextElement.from(opNode).toResult()
            } else {
                null
            }
        } ?: TextElementResult.empty

        val errorResult = handleErrorChildren(ctx?.children)
        val rt = opResult + errorResult
        return rt
    }

    override fun visitBoolOperation(ctx: FormulaParser.BoolOperationContext?): TextElementResult {
        val operator = ctx?.op?.let { op ->
            BasicTextElement.from(ctx.op).toResult()
        } ?: TextElementResult.empty

        val expr0 = emptyIfErrNode(ctx?.expr(0)) { it?.let { visit(it) } }

        val expr1 = emptyIfErrNode(ctx?.expr(1)) { it?.let { visit(it) } }

        val errorResult = handleErrorChildren(ctx?.children)
        val rt = expr0 + operator + expr1 + errorResult
        return rt
    }

    override fun visitFunctionCall(ctx: FormulaParser.FunctionCallContext?): TextElementResult {
        var textElementResult = TextElementResult.empty
        if (ctx != null) {
            for (child in ctx.children) {
                if (child !is ErrorNode) {
                    val q = emptyIfErrNode(child) { it?.let { visit(it) } }
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


    /**
     * This one produce a temporary result obj ferrying temporary text elements to the next parsing function. ([visitFullRangeAddress])
     */
    override fun visitRangeAsPairCellAddress(ctx: FormulaParser.RangeAsPairCellAddressContext?): TextElementResult {
        val textElementResult = ctx?.let {

            val c1 = emptyIfErrNode(ctx.cellAddress(0)) { visitCellAddress(it) }
            val op = emptyIfErrNode(ctx.getChild(1)) { it?.let { visit(it) } }
            val c2 = emptyIfErrNode(ctx.cellAddress(1)) { visitCellAddress(it) }

            val q = if (c1.hasNoErr() && op.hasNoErr() && c2.hasNoErr()) {
                if (c1.hasExactlyOneBasicText() && op.hasExactlyOneBasicText() && c2.hasExactlyOneBasicText()) {
                    // ideal condition
                    val label = "${c1.basicTexts[0].text}${op.basicTexts[0].text}${c2.basicTexts[0].text}"
                    TextElementResult.ferry(BasicTextElement(label, ctx.start.startIndex..ctx.stop.stopIndex))
                } else {
                    c1 + op + c2
                }
            } else {
                c1 + op + c2
            }
            q
        } ?: TextElementResult.empty
        val errorResult = handleErrorChildren(ctx?.children)
        val rt = textElementResult + errorResult
        return rt
    }

    /**
     * This one produce a temporary result obj ferrying temporary text elements to the next parsing function. ([visitFullRangeAddress])
     */
    override fun visitRangeAsOneCellAddress(ctx: FormulaParser.RangeAsOneCellAddressContext?): TextElementResult {
        val rs1 = emptyIfErrNode(ctx?.cellAddress()) {
            it?.let {
                val r1 = visitCellAddress(it)
                if (r1.hasNoErr() && r1.hasExactlyOneBasicText()) {
                    TextElementResult.ferry(r1.basicTexts[0])
                } else {
                    r1
                }
            }
        }

        val errorResult = handleErrorChildren(ctx?.children)
        val rt = rs1 + errorResult
        return rt
    }

    override fun visitColAddress(ctx: FormulaParser.ColAddressContext?): TextElementResult {
        val c1 = emptyIfErrNode(ctx?.ID_LETTERS(0)) { visitTerminal(it) }
        val o = emptyIfErrNode(ctx?.getChild(1)) { it?.let { visit(it) } }
        val c2 = emptyIfErrNode(ctx?.ID_LETTERS(1)) { visitTerminal(it) }
        val rt = (c1 + o + c2).flattenToBasicTextOrEmpty()
        return rt
    }

    override fun visitRowAddress(ctx: FormulaParser.RowAddressContext?): TextElementResult {
        val c1 = emptyIfErrNode(ctx?.INT(0)) { visitTerminal(it) }
        val o = emptyIfErrNode(ctx?.getChild(1)) { it?.let { visit(it) } }
        val c2 = emptyIfErrNode(ctx?.INT(1)) { visitTerminal(it) }
        val rt = (c1 + o + c2).flattenToBasicTextOrThis()
        return rt
    }

    /**
     * This one produce a temporary result obj ferrying temporary text elements to the next parsing function. ([visitFullRangeAddress])
     */
    override fun visitRangeAsColAddress(ctx: FormulaParser.RangeAsColAddressContext?): TextElementResult {
        val r1 = emptyIfErrNode(ctx?.colAddress()) { visitColAddress(it) }
        val rt = if (r1.hasNoErr() && r1.hasExactlyOneBasicText()) {
            TextElementResult.ferry(r1.basicTexts[0])
        } else {
            r1
        }
        return rt
    }

    /**
     * This one produce a temporary result obj ferrying temporary text elements to the next parsing function. ([visitFullRangeAddress])
     */
    override fun visitRangeAsRowAddress(ctx: FormulaParser.RangeAsRowAddressContext?): TextElementResult {
        val r1 = emptyIfErrNode(ctx?.rowAddress()) { visitRowAddress(it) }
        val rt = if (r1.hasNoErr() && r1.hasExactlyOneBasicText()) {
            TextElementResult.ferry(r1.basicTexts[0])
        } else {
            r1
        }
        return rt
    }

    override fun visitRangeInparens(ctx: FormulaParser.RangeInparensContext?): TextElementResult {
        val raRs = emptyIfErrNode(ctx?.rangeAddress()) { it?.let { visit(it) } }

        var rt =
            (emptyIfErrNode(ctx?.openParen()) {
                visitOpenParen(it)
            }) + raRs +
            (emptyIfErrNode(ctx?.closeParen()) {
                visitCloseParen(it)
            })

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

    companion object {
        private fun Token.hasValidRange(): Boolean {
            return this.startIndex >= 0 && this.stopIndex >= 0
        }
    }
}
