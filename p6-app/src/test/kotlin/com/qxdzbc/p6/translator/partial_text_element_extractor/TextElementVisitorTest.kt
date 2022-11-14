package com.qxdzbc.p6.translator.partial_text_element_extractor

import kotlin.test.*

internal class TextElementVisitorTest {
    val visitor = TextElementVisitor()
    val treeExtractor = PartialFormulaTreeExtractor()
    @Test
    fun qweqwe(){
        val formula="=A1  +  B1"
        val parseTree = treeExtractor.extractTree(formula)
        val e=visitor.visit(parseTree.component1())
        println(e)

    }
}
