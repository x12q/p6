package com.qxdzbc.p6.ui.worksheet.cursor.thumb.action

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.composite_actions.common_data_structure.WbWs
import com.qxdzbc.p6.composite_actions.common_data_structure.WbWsSt
import com.qxdzbc.p6.ui.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.composite_actions.cursor.thumb.drag_thumb_action.DragThumbAction
import test.BaseAppStateTest
import kotlin.test.*

internal class DragThumbActionImpTest:BaseAppStateTest(){
    lateinit var act: DragThumbAction
    lateinit var wbwsSt:WbWsSt
    lateinit var cursorStateMs:Ms<CursorState>
    @BeforeTest
    override fun _b() {
        super._b()
        act = ts.comp.dragThumbAction()
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
