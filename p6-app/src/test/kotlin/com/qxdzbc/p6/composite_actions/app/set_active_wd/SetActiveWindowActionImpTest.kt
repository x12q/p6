package com.qxdzbc.p6.composite_actions.app.set_active_wd

import kotlin.test.*
import test.TestSample

class SetActiveWindowActionImpTest {
    lateinit var ts: TestSample
    lateinit var action: SetActiveWindowAction
    val sc get()=ts.sc

    @BeforeTest
    fun b() {
        ts = TestSample()
        val p6Comp = ts.comp
        action = p6Comp.setActiveWindowAction()
    }

    @Test
    fun setActiveWd(){
        val pointer = ts.activeWindowPointer
        val wid = ts.window2Id
        assertTrue(pointer.windowId != wid)
        action.setActiveWindow(wid)
        assertTrue(pointer.windowId == wid)
    }
}
