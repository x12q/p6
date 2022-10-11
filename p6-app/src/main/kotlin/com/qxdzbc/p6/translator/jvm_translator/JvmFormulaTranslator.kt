package com.qxdzbc.p6.translator.jvm_translator

import com.qxdzbc.common.Rs
import com.qxdzbc.common.error.CommonErrors
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.formula.translator.antlr.FormulaBaseVisitor
import com.qxdzbc.p6.translator.jvm_translator.tree_extractor.TreeExtractor
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.flatMap
import com.github.michaelbull.result.map
import com.qxdzbc.p6.formula.translator.antlr.FormulaLexer
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import org.antlr.v4.runtime.tree.ParseTree


class JvmFormulaTranslator @AssistedInject constructor(
    @Assisted private val visitor: FormulaBaseVisitor<ExUnit>,
    private val treeExtractor: TreeExtractor,
) : P6Translator<ExUnit> {

    override fun translate(formula: String): Rs<ExUnit, ErrorReport> {
        val treeRs = treeExtractor.extractTree(formula)
        val visitRs = treeRs.map { it->
            visitor.visit(it)
        }
        if (visitRs is Ok) {
            val e = visitRs.value
            if (e != null) {
                return Ok(e)
            } else {
                return ErrorReport(
                    header = CommonErrors.Unknown.header
                ).toErr()
            }
        } else {
            return visitRs
        }
    }
}
