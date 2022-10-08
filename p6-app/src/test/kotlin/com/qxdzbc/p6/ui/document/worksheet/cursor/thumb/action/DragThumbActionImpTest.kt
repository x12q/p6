package com.qxdzbc.p6.ui.document.worksheet.cursor.thumb.action

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import test.BaseTest
import kotlin.test.*

internal class DragThumbActionImpTest:BaseTest(){
    lateinit var act:DragThumbAction
    lateinit var wbwsSt:WbWsSt
    lateinit var cursorStateMs:Ms<CursorState>
    @BeforeTest
    override fun b() {
        super.b()
        act = ts.p6Comp.dragThumbAction()
        wbwsSt = ts.sc.getWbWsSt(WbWs(ts.wbKey1,ts.wsn1))!!
        cursorStateMs = ts.sc.getCursorStateMs(wbwsSt)!!
    }
    @Test
    fun startDrag(){
//        val c3 = CellAddress("C3")
//        cursorStateMs.value = cursorStateMs.value.setMainCell(c3)
//        act.startDrag_forTest(wbwsSt, c3)
    }
}
