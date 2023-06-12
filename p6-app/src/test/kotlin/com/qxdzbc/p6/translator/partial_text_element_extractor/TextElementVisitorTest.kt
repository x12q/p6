package com.qxdzbc.p6.translator.partial_text_element_extractor

import com.qxdzbc.common.test_util.TestSplitter
import kotlin.test.*

internal class TextElementVisitorTest :TestSplitter(){
    val visitor = TextElementVisitor()
    val treeExtractor = PartialFormulaTreeExtractor()
    @Test
    fun qweqwe(){
        val formula="=A1  +  B1"
        val parseTree = treeExtractor.extractTree(formula)
        val e=visitor.visit(parseTree.component1())
        println(e)
    }

    @Test
    fun parErrFormula(){
        test("test parsing the erro formula =B1+SUM(D)"){
            val formula="=B1+SUM(D)"
            val parseTree = treeExtractor.extractTree(formula)
            val e=visitor.visit(parseTree.component1())
            println(e)
        }
    }

}
