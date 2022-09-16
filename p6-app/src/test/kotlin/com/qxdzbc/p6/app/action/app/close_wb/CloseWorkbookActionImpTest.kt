package com.qxdzbc.p6.app.action.app.close_wb

import com.qxdzbc.p6.app.action.app.create_new_wb.CreateNewWorkbookAction
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import test.TestSample
import kotlin.test.*

class CloseWorkbookActionImpTest{
    lateinit var ts: TestSample
    lateinit var closeWbAct: CloseWorkbookAction

    val sc get()=ts.stateContMs().value
    @BeforeTest
    fun b() {
        ts = TestSample()
        val p6Comp = ts.p6Comp
        closeWbAct = p6Comp.closeWbAct()
    }

    @Test
    fun `close 1 wb`(){
        val wbk = ts.wbKey1
        val windowStateMs = sc.getWindowStateMsById(ts.window1Id)

        assertNotNull(windowStateMs)
        assertTrue(wbk in windowStateMs.value.wbKeySet)
        assertNotNull(sc.getWb(wbk))

        closeWbAct.closeWb(CloseWorkbookRequest(
            wbKey = wbk,
            windowId = ts.window1Id
        ))

        assertTrue(wbk !in windowStateMs.value.wbKeySet )
        assertNull(sc.getWb(wbk))
    }
}

