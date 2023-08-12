package com.qxdzbc.p6.composite_actions.worksheet.remove_all_cell

import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.document_data_layer.cell.CellContentImp
import com.qxdzbc.p6.document_data_layer.cell.CellValue
import com.qxdzbc.p6.document_data_layer.cell.IndCellImp
import test.TestSample
import kotlin.test.*

internal class RemoveAllCellActionImpTest {
    lateinit var ts:TestSample
    lateinit var act:RemoveAllCellAction
    @BeforeTest
    fun b(){
        ts = TestSample()
        act=ts.comp.removeAllCellAction()
    }

    @Test
    fun removeAllCell() {
        val wbk = ts.wbKey1
        val wsn = ts.wsn1
        val sc = ts.sc
        val wsState = sc.getWsState(wbk,wsn)
        assertNotNull(wsState)
        val wsMs = wsState.wsMs
        wsMs.value.removeAllCell()
        wsMs.value.addOrOverwrite(
            IndCellImp(
            address = CellAddress("A1"),
            content = CellContentImp(
                cellValueMs = ms(CellValue.from(123)),
            )
        )
        )
        wsState.refreshCellState()
        assertTrue(wsState.worksheet.isNotEmpty())
        assertTrue(wsState.cellStateCont.isNotEmpty())
        act.removeAllCell(wsState.id)
        assertTrue(wsState.worksheet.isEmpty())
    }
}
