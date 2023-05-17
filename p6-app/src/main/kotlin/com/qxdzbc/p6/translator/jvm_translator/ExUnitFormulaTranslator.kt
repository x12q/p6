package com.qxdzbc.p6.translator.jvm_translator

import com.qxdzbc.common.error.CommonErrors
import com.qxdzbc.common.error.SingleErrorReport
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.formula.translator.antlr.FormulaBaseVisitor
import com.qxdzbc.p6.translator.jvm_translator.tree_extractor.TreeExtractor
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.map
import com.qxdzbc.common.Rse
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * Translate a text formula into [ExUnit]
 */
class ExUnitFormulaTranslator @AssistedInject constructor(
    @Assisted private val visitor: FormulaBaseVisitor<ExUnit>,
    private val treeExtractor: TreeExtractor,
) : P6Translator<ExUnit> {

    override fun translate(formula: String): Rse<ExUnit> {
        val treeRs = treeExtractor.extractTree(formula)
        val visitRs = treeRs.map { it->
            visitor.visit(it)
        }
        if (visitRs is Ok) {
            val e = visitRs.value
            if (e != null) {
                return Ok(e)
            } else {
                return SingleErrorReport(
                    header = CommonErrors.Unknown.header
                ).toErr()
            }
        } else {
            return visitRs
        }
    }
}
