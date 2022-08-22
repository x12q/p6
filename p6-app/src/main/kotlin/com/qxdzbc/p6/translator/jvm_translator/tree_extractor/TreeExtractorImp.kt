package com.qxdzbc.p6.translator.jvm_translator.tree_extractor

import com.qxdzbc.p6.common.exception.error.ErrorReport
import com.qxdzbc.p6.formula.translator.antlr.FormulaLexer
import com.qxdzbc.p6.formula.translator.antlr.FormulaParser
import com.qxdzbc.p6.formula.translator.errors.TranslatorErrors
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import org.antlr.v4.runtime.*
import org.antlr.v4.runtime.tree.ParseTree
import javax.inject.Inject

class TreeExtractorImp @Inject constructor() : TreeExtractor {
    override fun extractTree(formula: String): Result<ParseTree, ErrorReport> {
        var parserErrorData: TranslatorErrors.ParserErr.Data? = null
        var lexerErrData: TranslatorErrors.LexerErr.Data? = null

        val charStream: CharStream = CharStreams.fromString(formula)
        val lexer = FormulaLexer(charStream)
        lexer.removeErrorListeners()
        lexer.addErrorListener( object : BaseErrorListener(){
            override fun syntaxError(
                recognizer: Recognizer<*, *>?,
                offendingSymbol: Any?,
                line: Int,
                charPositionInLine: Int,
                msg: String?,
                e: RecognitionException?,
            ) {
                lexerErrData = TranslatorErrors.LexerErr.Data(
                    formula = formula,
                    recognizer = recognizer,
                    offendingSymbol = offendingSymbol,
                    line = line,
                    charPositionInLine = charPositionInLine,
                    msg = msg,
                    recognitionException = e
                )
            }
        })

        val tokenStream  = CommonTokenStream(lexer)
        val parser = FormulaParser(tokenStream)

        parser.removeErrorListeners()
        parser.addErrorListener( object : BaseErrorListener(){
            override fun syntaxError(
                recognizer: Recognizer<*, *>?,
                offendingSymbol: Any?,
                line: Int,
                charPositionInLine: Int,
                msg: String?,
                e: RecognitionException?,
            ) {
                parserErrorData = TranslatorErrors.ParserErr.Data(
                    formula = formula,
                    recognizer = recognizer,
                    offendingSymbol = offendingSymbol,
                    line = line,
                    charPositionInLine = charPositionInLine,
                    msg = msg,
                    recognitionException = e
                )
            }
        })
        val tree: ParseTree = parser.formula()
        if(parserErrorData!=null || lexerErrData!=null){
            return Err(
                ErrorReport(
                    header = TranslatorErrors.TranslatingErr.header,
                    data = TranslatorErrors.TranslatingErr.Data(
                        lexerErr = lexerErrData,
                        parserErr = parserErrorData
                    ),
                )
            )
        }
        return Ok(tree)
    }
}
