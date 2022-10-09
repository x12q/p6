package com.qxdzbc.p6.translator.cell_range_extractor

import com.github.michaelbull.result.map
import com.qxdzbc.common.Rs
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.di.CellRangeVisitor_Qualifier
import com.qxdzbc.p6.di.PartialTreeExtractor
import com.qxdzbc.p6.formula.translator.antlr.FormulaBaseVisitor
import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.translator.jvm_translator.tree_extractor.TreeExtractor
import javax.inject.Inject

class CellRangeExtractor @Inject constructor(
    @CellRangeVisitor_Qualifier
//    val visitor: FormulaBaseVisitor<List<CellRangePosition>>,
    val visitor: CellRangeVisitor,
    @PartialTreeExtractor
    val treeExtractor:TreeExtractor
) : P6Translator<List<CellRangePosition>> {
    override fun translate(formula: String): Rs<List<CellRangePosition>, ErrorReport> {
        val treeRs = treeExtractor.extractTree(formula)
        val rt=treeRs.map {tree->
            visitor.visit(tree)
        }
        return rt
    }
}
