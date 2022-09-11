package com.qxdzbc.p6.app.action.app.set_active_wb

import com.github.michaelbull.result.Ok
import test.TestSample
import kotlin.test.*

class SetActiveWorkbookActionTest{
    lateinit var ts:TestSample
    lateinit var action:SetActiveWorkbookAction
    @BeforeTest
    fun b(){
        ts = TestSample()
        action = ts.p6Comp.setActiveWorkbookAction()
    }

    @Test
    fun setActiveWb(){
        assertNotEquals(ts.wbKey4, ts.appState.activeWindowState?.activeWbKey)
        val rs = action.setActiveWb(ts.wbKey4)
        assertTrue(rs is Ok)
        assertEquals(ts.wbKey4, ts.appState.activeWindowState?.activeWbKey)
    }

    @Test
    fun `setActiveWb error case`(){
        assertNotEquals(ts.wbKey4, ts.appState.activeWindowState?.activeWbKey)
        val rs = action.setActiveWb(ts.wbKey4)
        assertTrue(rs is Ok)
        assertEquals(ts.wbKey4, ts.appState.activeWindowState?.activeWbKey)
    }
}
