package com.qxdzbc.p6.translator.partial_text_element_extractor

import com.qxdzbc.common.CollectionUtils.generateCombinations
import com.qxdzbc.common.test_util.TestSplitter
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
    fun `parErrFormula`() {
        test("") {
            val inputs = listOf(
                "=+-*B1:C12A1@'Ws1'@'Wb1'"
            )
            inputs.forEach { formula ->
                println(formula)
                val parseTree = treeExtractor.extractTree(formula)
                val e = visitor.visit(parseTree.component1())
                e.makeText() shouldBe formula
            }
        }
    }


    @Test
    fun `parErrFormula mixed formula`() {
        test("Parse erroneous formula that contains a mix of operator, functions, range addresses") {
            generateErrorFormulaWithRangeAddress(-1){formulaList->
                formulaList.forEach { formula->
                    val parseTree = treeExtractor.extractTree(formula)
                    val e = visitor.visit(parseTree.component1())
                    e.makeText() shouldBe formula
                    println(formula)
                }
            }
        }
    }

    @Test
    fun `parErrFormula error formulas with only operators`() {
        test("Parse erroneous formula that contain only operators") {
            val inputs = generateErrorOperatorFormulas(-1)
            inputs.forEach { formula ->
                val parseTree = treeExtractor.extractTree(formula)
                val e = visitor.visit(parseTree.component1())
                e.makeText() shouldBe formula
            }
        }
    }

    val operator = listOf(
        "+", "-", "*", "/", "^", "&&",
        "||", "%", "==", "!=", ">",
        ">=", "<", "<=", ","
    )

    /**
     * [combinationSize] < 0 means generate exhaustively.
     */
    fun generateErrorFormulaWithRangeAddress(combinationSize: Int, onEach:(List<String>)->Unit) {
        val candidates = operator + listOf(
            "A1",
            "B1:C12",
            "1",
            "\"abc\"",
            "A1@'Ws1'@'Wb1'",
            "SUM(D)",
            "SUM(1,2,3)",
            "F(A1:A2,\"\")"
        )
        if (combinationSize < 0) {
            candidates.indices.map {
                onEach(generateErrorFormulas(candidates, it))
            }
        } else {
            onEach(generateErrorFormulas(candidates, combinationSize))
        }
    }


    /**
     * Generate erroneous formulas consist of only operators
     */
    fun generateErrorOperatorFormulas(combinationSize: Int): List<String> {
        if (combinationSize < 0) {
            val rt = operator.indices.map {
                generateErrorFormulas(operator, it)
            }.flatten()
            return rt
        } else {
            return generateErrorFormulas(operator, combinationSize)
        }
    }

    /**
     * Generate erroneous formulas by generating combinations with size = [combinationSize] from a list of [candidates].
     */
    fun generateErrorFormulas(candidates: List<String>, combinationSize: Int = 2): List<String> {
        val rt = candidates.generateCombinations(combinationSize).map {
            "=" + it.joinToString("")
        }
        return rt
    }

}
