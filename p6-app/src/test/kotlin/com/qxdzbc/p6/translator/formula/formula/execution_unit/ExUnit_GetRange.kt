package com.qxdzbc.p6.translator.formula.formula.execution_unit

import com.qxdzbc.common.compose.StateUtils.toSt
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit.Companion.exUnit
import org.mockito.kotlin.mock
import test.TestSample
import kotlin.test.*

internal class ExUnit_GetRange {
    lateinit var ts:TestSample

    @BeforeTest
    fun b(){
        ts = TestSample()
    }
    @Test
    fun toFormula() {
        val u = ExUnit.GetRange(
            funcName = "qwe",
                wbKeyUnit=WorkbookKey("Wb1",null).toSt().exUnit(),
                wsNameUnit=ExUnit.WsNameStUnit("Sheet1".toSt()),
                rangeAddressUnit =ExUnit.RangeAddressUnit(RangeAddress("B2:K9")),
            functionMapSt = mock()
        )
        assertEquals("B2:K9@'Sheet1'@'Wb1'",u.toFormula())

    }

    @Test
    fun toFormulaSelective() {
        val wbk1 = WorkbookKey("Wb1",null)
        val wbk2 = WorkbookKey("Wb2",null)
        val u = ExUnit.GetRange(
            funcName = "qwe",
            wbKeyUnit=wbk1.toSt().exUnit(),
            wsNameUnit=ExUnit.WsNameStUnit("Sheet1".toSt()),
            rangeAddressUnit =ExUnit.RangeAddressUnit(RangeAddress("B2:K9")),
            functionMapSt = mock()
        )
        assertEquals("B2:K9@'Sheet1'",u.toFormulaSelective(wbk1,"Sheet2"))
        assertEquals("B2:K9",u.toFormulaSelective( wbk1,"Sheet1"))
        assertEquals("B2:K9@'Sheet1'@'Wb1'",u.toFormulaSelective( wbk2,"Sheet1"))
    }
}
