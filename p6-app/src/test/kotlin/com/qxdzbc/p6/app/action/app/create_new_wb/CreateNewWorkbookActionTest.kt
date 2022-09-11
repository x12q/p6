package com.qxdzbc.p6.app.action.app.create_new_wb

import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import test.TestSample
import kotlin.test.*

class CreateNewWorkbookActionTest {

    lateinit var ts: TestSample
    lateinit var action: CreateNewWorkbookAction

    @BeforeTest
    fun b() {
        ts = TestSample()
        val p6Comp = ts.p6Comp
        action = p6Comp.createNewWbAction()
    }

    @Test
    fun createNewWb() {
        val wbk=WorkbookKey("NewWb")
        val req =             CreateNewWorkbookRequest(
            windowId = null,
            wbName = "NewWb"
        )

        assertNull(ts.stateContMs().value.getWb(wbk))
        val o = action.createNewWb(req)
        assertTrue(o.isOk)
        assertNull(o.errorReport)
        assertNotNull(o.wb)
        assertEquals(wbk,o.wb?.key)
        assertNotNull(ts.stateContMs().value.getWb(wbk))

        val o2 = action.createNewWb(req)
        assertTrue(o2.isError)
        assertNotNull(o2.errorReport)
        assertNull(o2.wb)
    }
}
