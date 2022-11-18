package com.qxdzbc.p6.translator.partial_text_element_extractor

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.map
import com.qxdzbc.p6.translator.partial_text_element_extractor.text_element.CellRangeElement
import com.qxdzbc.p6.translator.partial_text_element_extractor.text_element.OtherElement
import com.qxdzbc.p6.translator.partial_text_element_extractor.text_element.TokenPosition
import test.BaseTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class PartialCellRangeExtractorTest : BaseTest() {
    lateinit var extractor: PartialTextElementTranslator

    @BeforeTest
    override fun b() {
        super.b()
        extractor = ts.comp.partialTextElementExtractor()
    }
    @Test
    fun `extract-check cell range element only`() {
        val m = mapOf(
            "=F1(A1+F2(A2)+\"A3\"+11)" +
                    "\n\n" +
                    "+F3()*F4(A1:B9)" to listOf(
                CellRangeElement(
                    cellRangeLabel = "A1",
                    startTP = TokenPosition( 4),
                    stopTP = TokenPosition( 5),
                ),
                CellRangeElement(
                    cellRangeLabel = "A2",
                    startTP = TokenPosition( 10),
                    stopTP = TokenPosition( 11),
                ),
                CellRangeElement(
                    cellRangeLabel = "A1:B9",
                    startTP = TokenPosition( 33),
                    stopTP = TokenPosition( 37),
                ),
            ),
        )

        for ((formula, expectation) in m) {
            val o = extractor.translate(formula).map { it.cellRangeElements }
            assertEquals(Ok(expectation), o, formula)
        }
    }

    @Test
    fun `extract - check all elements`() {
        val m = mapOf(
            "=A1 B2" to listOf(
                OtherElement("=",0 .. 0),
                CellRangeElement(
                    cellRangeLabel = "A1",
                    startTP = TokenPosition( 1),
                    stopTP = TokenPosition( 2),
                ),
                OtherElement(
                    "B2",
                    4 .. 5
                ),
            ),
            "=F1()" to listOf(
                OtherElement("=",0 .. 0),
                OtherElement("F1",1 .. 2),
                OtherElement("(",3 .. 3),
                OtherElement(")",4 .. 4),
            ),
            "=1+2+\"q\"" to listOf(
                OtherElement("=",0 .. 0),
                OtherElement("1",1 .. 1),
                OtherElement("+",2 .. 2),
                OtherElement("2",3 .. 3),
                OtherElement("+",4 .. 4),
                OtherElement("\"q\"",5 .. 7),
            ),
            "=1^2" to listOf(
                OtherElement("=",0 .. 0),
                OtherElement("1",1 .. 1),
                OtherElement("^",2 .. 2),
                OtherElement("2",3 .. 3),
            ),
            "=1%2" to listOf(
                OtherElement("=",0 .. 0),
                OtherElement("1",1 .. 1),
                OtherElement("%",2 .. 2),
                OtherElement("2",3 .. 3),
            ),
            "=TRUE && FALSE || 1" to listOf(
                OtherElement("=",0 .. 0),
                OtherElement("TRUE",1 .. 4),
                OtherElement("&&",6 .. 7),
                OtherElement("FALSE",9 .. 13),
                OtherElement("||",15 .. 16),
                OtherElement("1",18 .. 18)
            ),
            "=FX(1,2)" to listOf(
                OtherElement("=", 0),
                OtherElement("FX", 1..2),
                OtherElement("(", 3),
                OtherElement("1", 4),
                OtherElement(",", 5),
                OtherElement("2", 6),
                OtherElement(")", 7),
            ),
            "=FX(1>2,!(3+2>3),A3)" to listOf(
                OtherElement("=", 0),
                OtherElement("FX", 1..2),
                OtherElement("(", 3),
                OtherElement("1", 4),
                OtherElement(">", 5),
                OtherElement("2", 6),
                OtherElement(",", 7),
                OtherElement("!", 8),
                OtherElement("(", 9),
                OtherElement("3", 10),
                OtherElement("+", 11),
                OtherElement("2", 12),
                OtherElement(">", 13),
                OtherElement("3", 14),
                OtherElement(")", 15),
                OtherElement(",", 16),
                CellRangeElement(cellRangeLabel="A3",cellRangeSuffix = null,startTP = TokenPosition(17),TokenPosition(18)),
                OtherElement(")", 19),
            )
        )
        for ((i, e) in m) {
            val o = extractor.translate(i).component1()?.allSorted()
            assertEquals(e, o)
        }
    }
}
