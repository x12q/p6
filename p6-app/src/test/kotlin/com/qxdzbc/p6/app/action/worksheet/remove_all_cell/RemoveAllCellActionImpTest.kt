package com.qxdzbc.p6.app.action.worksheet.remove_all_cell

import androidx.compose.runtime.getValue
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.d.CellContentImp
import com.qxdzbc.p6.app.document.cell.d.CellValue
import com.qxdzbc.p6.app.document.cell.d.IndCellImp
import test.TestSample
import kotlin.test.*

internal class RemoveAllCellActionImpTest {
    lateinit var ts:TestSample
    lateinit var act:RemoveAllCellAction
    @BeforeTest
    fun b(){
        ts = TestSample()
        act=ts.p6Comp.removeAllCellAction()
    }

    @Test
    fun removeAllCell() {
        val wbk = ts.wbKey1
        val wsn = ts.wsn1
        val sc by ts.stateContMs
        val wsStateMs = sc.getWsStateMs(wbk,wsn)
        assertNotNull(wsStateMs)
        val wsMs = wsStateMs.value.wsMs
        wsMs.value = wsMs.value.removeAllCell()
        wsMs.value = wsMs.value.addOrOverwrite(IndCellImp(
            address = CellAddress("A1"),
            content = CellContentImp(
                cellValueMs = ms(CellValue.from(123))
            )
        ))
        wsStateMs.value = wsStateMs.value.refreshCellState()
        assertTrue(wsStateMs.value.worksheet.isNotEmpty())
        assertTrue(wsStateMs.value.cellStateCont.isNotEmpty())
        act.removeAllCell(wsStateMs.value.id)
        assertTrue(wsStateMs.value.worksheet.isEmpty())
    }
}
