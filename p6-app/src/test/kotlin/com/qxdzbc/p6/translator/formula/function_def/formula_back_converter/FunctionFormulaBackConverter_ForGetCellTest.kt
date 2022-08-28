package com.qxdzbc.p6.translator.formula.function_def.formula_back_converter

import com.qxdzbc.common.compose.StateUtils.toSt
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit.Companion.exUnit
import org.mockito.kotlin.mock
import test.TestSample
import kotlin.test.*
internal class FunctionFormulaBackConverter_ForGetCellTest{
    lateinit var ts: TestSample

    @BeforeTest
    fun b(){
        ts = TestSample()

    }

    @Test
    fun toFormula() {
        val converter = FunctionFormulaBackConverter_ForGetCell()
        val u = ExUnit.Func(
            funcName = "qwe",
            args = listOf(
                WorkbookKey("Wb1",null).toSt().exUnit(),
                ExUnit.WsNameStUnit("Sheet1".toSt()),
                CellAddress("B2").exUnit()
            ),
            functionMapSt = mock()
        )
        assertEquals("B2@'Sheet1'@'Wb1'",converter.toFormula(u))
    }

    @Test
    fun toFormulaSelective() {
        val converter = FunctionFormulaBackConverter_ForGetCell()
        val u = ExUnit.Func(
            funcName = "qwe",
            args = listOf(
                WorkbookKey("Wb1",null).toSt().exUnit(),
                ExUnit.WsNameStUnit("Sheet1".toSt()),
                CellAddress("B2").exUnit()
            ),
            functionMapSt = mock()
        )
        assertEquals("B2@'Sheet1'",converter.toFormulaSelective(u, WorkbookKey("Wb1",null),null))
        assertEquals("B2",converter.toFormulaSelective(u, WorkbookKey("Wb1",null),"Sheet1"))
    }
}
