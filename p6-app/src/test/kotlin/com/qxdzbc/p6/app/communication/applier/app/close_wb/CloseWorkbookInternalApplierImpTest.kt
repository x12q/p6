package com.qxdzbc.p6.app.communication.applier.app.close_wb

import com.qxdzbc.p6.app.action.app.close_wb.applier.CloseWorkbookInternalApplierImp
import com.qxdzbc.p6.app.action.app.close_wb.CloseWorkbookResponse
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.qxdzbc.p6.ui.app.error_router.ErrorRouterImp
import org.junit.Test
import test.TestSample

import kotlin.test.BeforeTest
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class CloseWorkbookInternalApplierImpTest {

    lateinit var applier: CloseWorkbookInternalApplierImp
    lateinit var errorRouter: ErrorRouter
    lateinit var res: CloseWorkbookResponse
    lateinit var ts: TestSample
    val sc get()= ts.sc
    @BeforeTest
    fun b(){
        ts = TestSample()
        errorRouter = ErrorRouterImp(
            ts.scMs,ts.appState.codeEditorStateMs,ts.appState.errorContainerMs
        )
        applier = CloseWorkbookInternalApplierImp(
            appStateMs = ts.appStateMs,
            stateContMs = ts.comp.stateContMs(),
            pickDefaultActiveWb = ts.comp.pickDefaultActiveWbAction()
        )
        val windowId = sc.windowStateMsList[0].value.id
        res = CloseWorkbookResponse(
            wbKey = TestSample.wbk1,
            windowId = windowId,
            errorReport = null
        )
    }

    @Test
    fun apply() {
        assertNotNull(sc.getWbStateMs(TestSample.wbk1))
        assertNotNull(sc.wbCont.getWb(TestSample.wbk1))
        applier.apply(res.wbKey,res.windowId)
        assertNull(sc.getWbStateMs(TestSample.wbk1))
        assertNull(sc.wbCont.getWb(TestSample.wbk1))
    }

    @Test
    fun `apply null window id`() {
        val res = this.res.copy(windowId = null)
        assertNotNull(sc.getWbStateMs(TestSample.wbk1))
        assertNotNull(sc.wbCont.getWb(TestSample.wbk1))

        applier.apply(res.wbKey,null)

        assertNull(sc.getWbStateMs(TestSample.wbk1))
        assertNull(sc.wbCont.getWb(TestSample.wbk1))
        assertNull(sc.wbStateCont.getWbStateMs(TestSample.wbk1))
    }

    @Test
    fun `apply null window id invalid workbook key`() {
        val res = this.res.copy(windowId = null, wbKey = WorkbookKey("invalid"))

        assertNotNull(sc.getWbStateMs(TestSample.wbk1))
        assertNotNull(sc.getWbStateMs(TestSample.wbk2))
        assertNotNull(sc.wbCont.getWb(TestSample.wbk1))
        assertNotNull(sc.wbCont.getWb(TestSample.wbk2))

        applier.apply(res.wbKey,null)

        assertNotNull(sc.getWbStateMs(TestSample.wbk1))
        assertNotNull(sc.getWbStateMs(TestSample.wbk2))
        assertNotNull(sc.wbCont.getWb(TestSample.wbk1))
        assertNotNull(sc.wbCont.getWb(TestSample.wbk2))
    }
}
