package com.qxdzbc.p6.translator.partial_extrator

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.flatMap
import com.qxdzbc.common.Rs
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.di.PartialTreeExtractor
import com.qxdzbc.p6.di.PartialVisitor
import com.qxdzbc.p6.formula.translator.antlr.FormulaBaseVisitor
import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.translator.jvm_translator.tree_extractor.TreeExtractor
import javax.inject.Inject

@Deprecated("don't use")
class PartialFormulaTranslator @Inject constructor(
    @PartialTreeExtractor
    val treeExtractor:TreeExtractor,
    @PartialVisitor
    val partialVisitor: FormulaBaseVisitor<String?>
) : P6Translator<String?> {
    override fun translate(formula: String): Rs<String?, ErrorReport> {
        val treeRs = treeExtractor.extractTree(formula)
        val rt = treeRs.flatMap {tree->
            val out = partialVisitor.visit(tree)
            Ok(out)
        }
        return rt
    }
}
