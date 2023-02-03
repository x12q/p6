package com.qxdzbc.p6.translator.partial_text_element_extractor

import com.github.michaelbull.result.map
import com.qxdzbc.common.Rs
import com.qxdzbc.common.error.SingleErrorReport
import com.qxdzbc.p6.di.PartialTreeExtractor
import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.translator.jvm_translator.tree_extractor.TreeExtractor
import javax.inject.Inject

class PartialTextElementTranslator @Inject constructor(
    val visitor: TextElementVisitor,
    @PartialTreeExtractor
    val treeExtractor:TreeExtractor
) : P6Translator<TextElementResult> {
    override fun translate(formula: String): Rs<TextElementResult, SingleErrorReport> {
        val treeRs = treeExtractor.extractTree(formula)
        val rt=treeRs.map {tree->
            visitor.visit(tree)
        }
        return rt
    }
}
