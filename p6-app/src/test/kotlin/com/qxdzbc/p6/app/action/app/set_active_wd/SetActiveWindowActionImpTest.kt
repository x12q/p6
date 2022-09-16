package com.qxdzbc.p6.app.action.app.set_active_wd

import androidx.compose.runtime.getValue
import com.qxdzbc.p6.app.action.app.close_wb.CloseWorkbookAction
import com.qxdzbc.p6.app.action.app.create_new_wb.CreateNewWorkbookAction
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import kotlin.test.*
import test.TestSample

class SetActiveWindowActionImpTest {
    lateinit var ts: TestSample
    lateinit var action: SetActiveWindowAction
    val sc get()=ts.stateContMs().value

    @BeforeTest
    fun b() {
        ts = TestSample()
        val p6Comp = ts.p6Comp
        action = p6Comp.setActiveWindowAction()
    }

    @Test
    fun setActiveWd(){
        val pointer by ts.appState.activeWindowPointerMs
        val wid = ts.window2Id
        assertTrue(pointer.windowId != wid)
        action.setActiveWindow(wid)
        assertTrue(pointer.windowId == wid)
    }
}
