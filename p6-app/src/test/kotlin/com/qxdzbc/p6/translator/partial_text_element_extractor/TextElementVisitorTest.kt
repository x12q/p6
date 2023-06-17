package com.qxdzbc.p6.translator.partial_text_element_extractor

import com.qxdzbc.common.test_util.TestSplitter
import com.qxdzbc.p6.translator.partial_text_element_extractor.text_element.ErrTextElement
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import kotlin.test.*

internal class TextElementVisitorTest : TestSplitter() {
    val visitor = TextElementVisitor()
    val treeExtractor = PartialFormulaTreeExtractor()

    @Test
    fun `parse formula with multiple space between elements`() {
        val formula = "=A1  +  B1"
        val parseTree = treeExtractor.extractTree(formula)
        val e = visitor.visit(parseTree.component1())
        e.makeText() shouldBe formula
    }

    @Test
    fun parErrFormula() {
        test("Parse erroneous formula, test that the original text is preserved") {
            val illegalOperatorUse = listOf(
                "=",
                "=1+-",
                "=++1",
                "=1^-",
            )
            val inputs = illegalOperatorUse + generateOperator2CombinationFormula() +listOf(
                "=", // illegal use of formula starting symbol
                "=B1+SUM(D)", //illegal range address inside function
                "=B+1", // illegal range address
                "=1+B", // illegal range address
                "=SUM(B,A2)", // illegal range address inside function, before legal range address
                "=B1/D", // illegal range address after div operator
                "=!B", // illegal symbol after ! operator
            )
            inputs.forEach { formula ->
                val parseTree = treeExtractor.extractTree(formula)
                val e = visitor.visit(parseTree.component1())
                e.makeText() shouldBe formula
            }
        }
    }

    fun generateOperator2CombinationFormula(): List<String> {
        val operator = listOf(
            "+", "-", "*", "/", "^", "&&",
            "||", "%", "==", "!=", ">",
            ">=", "<", "<=",","
        )
        val rs = mutableListOf<String>()
        for (o1 in operator) {
            for (o2 in operator) {
                rs.add("=$o1$o2")
            }
        }
        return rs
    }

    fun generateOperator2CombinationFormulaWithNumber(): List<String> {
        val operator = listOf(
            "+", "-", "*", "/", "^", "&&",
            "||", "%", "==", "!=", ">",
            ">=", "<", "<=",","
        ) + (0 .. 9).toList()
        val rs = mutableListOf<String>()
        for (o1 in operator) {
            for (o2 in operator) {
                rs.add("=$o1$o2")
            }
        }
        return rs
    }

}
