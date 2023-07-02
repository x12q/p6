package com.qxdzbc.p6.app.action.workbook.new_worksheet

import androidx.compose.runtime.getValue
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.qxdzbc.common.Rse
import com.qxdzbc.p6.ui.app.state.AppState
import test.TestSample
import kotlin.test.*

internal class NewWorksheetActionImpTest {
    lateinit var appStateMs:AppState
    lateinit var ts: TestSample
    lateinit var act: NewWorksheetAction
    val wbk = TestSample.wbk1

    @BeforeTest
    fun b() {
        ts = TestSample()
        appStateMs = ts.appState
        act = ts.comp.newWorksheetAction()
    }

    @Test
    fun `new worksheet with a specified name`() {
        val wsName = "NewSheet"
        val req = CreateNewWorksheetRequest(ts.wbKey1, wsName)
        assertNull(ts.sc.getWsState(req))
        val o: Rse<CreateNewWorksheetResponse> = act.createNewWorksheetRs(req)
        assertTrue(o is Ok)
        val res = o.value
        val wsState = ts.sc.getWsState(res)
        assertNotNull(wsState)
    }

    @Test
    fun `new worksheet with an auto-generated name`() {
        val req = CreateNewWorksheetRequest(ts.wbKey1, null)
        val o: Rse<CreateNewWorksheetResponse> = act.createNewWorksheetRs(req)
        assertTrue(o is Ok)
        val res = o.value
        val wsState = ts.sc.getWsState(res)
        assertNotNull(wsState)
    }

    @Test
    fun `error case`() {
        val req = CreateNewWorksheetRequest(ts.wbKey1, ts.wsn1)
        val windowState = ts.sc.getWindowStateByWbKey(req.wbKey)
        assertNotNull(windowState)
        val errorContainer by windowState.errorContainerMs

        // x: precondition
        assertTrue(errorContainer.isEmpty())

        // x: action
        val o: Rse<CreateNewWorksheetResponse> = act.createNewWorksheetRs(req, true)

        // x: post condition
        assertTrue(o is Err)
        assertEquals(1, errorContainer.size)
        assertEquals(o.error, errorContainer.errList[0].errorReport)
    }
}
