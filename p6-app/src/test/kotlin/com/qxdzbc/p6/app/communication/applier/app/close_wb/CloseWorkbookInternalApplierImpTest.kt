package com.qxdzbc.p6.app.communication.applier.app.close_wb

import com.qxdzbc.p6.app.action.app.close_wb.applier.CloseWorkbookInternalApplierImp
import com.qxdzbc.p6.app.action.app.close_wb.CloseWorkbookResponse
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.ui.document.workbook.action.WorkbookActionTable
import com.qxdzbc.p6.ui.app.ErrorRouter
import com.qxdzbc.p6.ui.app.ErrorRouterImp
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.p6.ui.common.compose.Ms
import org.junit.Test
import org.mockito.kotlin.mock
import test.TestSample

import kotlin.test.BeforeTest
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class CloseWorkbookInternalApplierImpTest {

    lateinit var appStateMs:Ms<AppState>
    lateinit var applier: CloseWorkbookInternalApplierImp
    lateinit var errorRouter:ErrorRouter
    val appState get()=appStateMs.value
    lateinit var res: CloseWorkbookResponse
    lateinit var testSample: TestSample
    @BeforeTest
    fun b(){
        testSample = TestSample()
        appStateMs = testSample.sampleAppStateMs()
        errorRouter = ErrorRouterImp(appStateMs)
        applier = CloseWorkbookInternalApplierImp(
            appStateMs = appStateMs,
            stateContMs = testSample.p6Comp.stateContMs()
        )
        val windowId = appStateMs.value.windowStateMsList[0].value.id
        res = CloseWorkbookResponse(
            isError = false,
            wbKey = TestSample.wbk1,
            windowId = windowId,
            errorReport = null
        )
    }

    @Test
    fun apply() {
        assertNotNull(appState.getWbStateMs(TestSample.wbk1))
        assertNotNull(appState.globalWbCont.getWb(TestSample.wbk1))
        applier.apply(res.wbKey,res.windowId)
        assertNull(appState.getWbStateMs(TestSample.wbk1))
        assertNull(appState.globalWbCont.getWb(TestSample.wbk1))
    }

    @Test
    fun `apply null window id`() {
        val res = this.res.copy(windowId = null)
        assertNotNull(appState.getWbStateMs(TestSample.wbk1))
        assertNotNull(appState.globalWbCont.getWb(TestSample.wbk1))

        applier.apply(res.wbKey,null)

        assertNull(appState.getWbStateMs(TestSample.wbk1))
        assertNull(appState.globalWbCont.getWb(TestSample.wbk1))
        assertNull(appState.globalWbStateCont.getWbStateMs(TestSample.wbk1))
    }

    @Test
    fun `apply null window id invalid workbook key`() {
        val res = this.res.copy(windowId = null, wbKey = WorkbookKey("invalid"))

        assertNotNull(appState.getWbStateMs(TestSample.wbk1))
        assertNotNull(appState.getWbStateMs(TestSample.wbk2))
        assertNotNull(appState.globalWbCont.getWb(TestSample.wbk1))
        assertNotNull(appState.globalWbCont.getWb(TestSample.wbk2))

        applier.apply(res.wbKey,null)

        assertNotNull(appState.getWbStateMs(TestSample.wbk1))
        assertNotNull(appState.getWbStateMs(TestSample.wbk2))
        assertNotNull(appState.globalWbCont.getWb(TestSample.wbk1))
        assertNotNull(appState.globalWbCont.getWb(TestSample.wbk2))
    }
}
