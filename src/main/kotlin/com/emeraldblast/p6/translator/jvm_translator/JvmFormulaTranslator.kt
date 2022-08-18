package com.emeraldblast.p6.translator.jvm_translator

import com.emeraldblast.p6.app.common.utils.Rs
import com.emeraldblast.p6.common.exception.error.CommonErrors
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.emeraldblast.p6.translator.formula.execution_unit.ExUnit
import com.emeraldblast.p6.translator.P6Translator
import com.emeraldblast.p6.formula.translator.antlr.FormulaBaseVisitor
import com.emeraldblast.p6.translator.jvm_translator.tree_extractor.TreeExtractor
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.map
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


class JvmFormulaTranslator @AssistedInject constructor(
    @Assisted private val visitor: FormulaBaseVisitor<ExUnit>,
    private val treeExtractor: TreeExtractor,
) : P6Translator<ExUnit> {

    override fun translate(formula: String): Rs<ExUnit, ErrorReport> {
        val treeRs = treeExtractor.extractTree(formula)
        val visitRs = treeRs.map { visitor.visit(it) }
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
