package com.qxdzbc.p6.app.action.app.create_new_wb

import androidx.compose.runtime.getValue
import com.qxdzbc.p6.app.action.app.close_wb.CloseWorkbookAction
import com.qxdzbc.p6.app.action.app.close_wb.CloseWorkbookRequest
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import test.TestSample
import kotlin.test.*

class CreateNewWorkbookActionTest {

    lateinit var ts: TestSample
    lateinit var action: CreateNewWorkbookAction
    lateinit var closeWbAct: CloseWorkbookAction
    val wbk = WorkbookKey("NewWb")
    val sc get()=ts.stateContMs().value
    @BeforeTest
    fun b() {
        ts = TestSample()
        val p6Comp = ts.comp
        action = p6Comp.createNewWbAction()
        closeWbAct = p6Comp.closeWbAct()
    }

    @Test
    fun `create new wb when there is no wb`() {

        val windowState=sc.getWindowStateMsById(ts.window1Id)
        assertNotNull(windowState)
        val ws by windowState
        val wbKeySet=ws.wbKeySet
        for(k in wbKeySet){
            closeWbAct.closeWb(CloseWorkbookRequest(
                wbKey =k,
                windowId = ws.id
            ))
        }
        assertTrue(ws.wbList.isEmpty())
        assertTrue(ws.wbKeySet.isEmpty())
        val req = CreateNewWorkbookRequest(
            windowId = ts.window1Id,
            wbName = wbk.name
        )
        action.createNewWb(req)
        assertTrue(wbk in ws.wbKeySet)
        assertTrue(ws.activeWbPointer.isPointingTo(wbk))
    }

    @Test
    fun createNewWb() {
        val req = CreateNewWorkbookRequest(
            windowId = null,
            wbName = wbk.name
        )

        assertNull(ts.stateContMs().value.getWb(wbk))
        val o = action.createNewWb(req)
        assertTrue(o.isOk)
        assertNull(o.errorReport)
        assertNotNull(o.wb)
        assertEquals(wbk, o.wb?.key)
        assertNotNull(ts.stateContMs().value.getWb(wbk))

        val o2 = action.createNewWb(req)
        assertTrue(o2.isError)
        assertNotNull(o2.errorReport)
        assertNull(o2.wb)
    }
}
