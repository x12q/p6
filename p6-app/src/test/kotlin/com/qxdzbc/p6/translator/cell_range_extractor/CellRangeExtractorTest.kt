package com.qxdzbc.p6.translator.cell_range_extractor

import com.github.michaelbull.result.Ok
import com.qxdzbc.p6.translator.P6Translator
import test.BaseTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class CellRangeExtractorTest : BaseTest() {
    lateinit var extractor: P6Translator<List<CellRangePosition>>

    @BeforeTest
    override fun b() {
        super.b()
        extractor = ts.p6Comp.cellRangeExtractor()
    }

    @Test
    fun extract() {
        val m = mapOf(
            "=F1(A1+F2(A2)+\"A3\"+11)" +
                    "\n\n" +
                    "+F3()*F4(A1:B9)" to listOf(
                CellRangePosition(
                    text = "A1",
                    start = TokenPosition( 4),
                    stop = TokenPosition( 5),
                ),
                CellRangePosition(
                    text = "A2",
                    start = TokenPosition( 10),
                    stop = TokenPosition( 11),
                ),
                CellRangePosition(
                    text = "A1:B9",
                    start = TokenPosition( 33),
                    stop = TokenPosition( 37),
                ),
            )
        )

        for ((formula, expectation) in m) {
            val o = extractor.translate(formula)
//            assertEquals(3,o.component1()!!.size)
            assertEquals(Ok(expectation), o, formula)
        }
    }
}
