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
        val u = ExUnit.Func(
            funcName = "qwe",
            args = listOf(
                WorkbookKey("Wb1",null).toSt().exUnit(),
                ExUnit.WsNameStUnit("Sheet1".toSt()),
                CellAddress("B2").exUnit()
            ),
            functionMapSt = mock()
        )
        assertEquals("B2@'Sheet1'@'Wb1'",u.toFormula())
    }

    @Test
    fun toFormulaSelective() {
        val wbk1 = WorkbookKey("Wb1",null)
        val wbk2 = WorkbookKey("Wb2",null)
        val u = ExUnit.Func(
            funcName = "qwe",
            args = listOf(
                wbk1.toSt().exUnit(),
                ExUnit.WsNameStUnit("Sheet1".toSt()),
                CellAddress("B2").exUnit()
            ),
            functionMapSt = mock()
        )
        assertEquals("B2@'Sheet1'", u.toFormulaSelective(wbk1,"Sheet2"))
        assertEquals("B2", u.toFormulaSelective(wbk1,"Sheet1"))
        assertEquals("B2@'Sheet1'@'Wb1'", u.toFormulaSelective( wbk2,"Sheet1"))
    }
}
