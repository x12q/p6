package com.qxdzbc.p6.translator.partial_text_element_extractor

import com.qxdzbc.common.test_util.TestSplitter
import com.qxdzbc.p6.translator.partial_text_element_extractor.text_element.ErrTextElement
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import kotlin.test.*

internal class TextElementVisitorTest :TestSplitter(){
    val visitor = TextElementVisitor()
    val treeExtractor = PartialFormulaTreeExtractor()
    @Test
    fun `parse formula with multiple space between elements`(){
        val formula="=A1  +  B1"
        val parseTree = treeExtractor.extractTree(formula)
        val e=visitor.visit(parseTree.component1())
        e.makeText() shouldBe formula
    }

    @Test
    fun parErrFormula(){
        test("Parse erroneous formula, test that the original text is preserved"){
            val inputs = listOf(
//                "=B1+SUM(D)",
//                "=B+1",
//                "=SUM(B,A2)",
//                "=B1/D",
//                "=!B",
                "=,,,," // => check visitInvokeExpr
            )
            inputs.forEach {formula->
                val parseTree = treeExtractor.extractTree(formula)
                val e=visitor.visit(parseTree.component1())
//                e.errs.shouldNotBeEmpty()
                e.makeText() shouldBe formula
            }
        }
    }

}
