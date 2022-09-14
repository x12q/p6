package com.qxdzbc.p6.app.communication.applier.app.load_wb

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.p6.app.action.app.load_wb.applier.LoadWorkbookInternalApplierImp
import com.qxdzbc.p6.app.document.workbook.WorkbookImp
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.qxdzbc.p6.ui.app.error_router.ErrorRouterImp
import com.qxdzbc.p6.ui.app.state.AppState
import test.TestSample
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class LoadWorkbookInternalApplierImpTest {
    lateinit var appStateMs: Ms<AppState>
    val appState get() = appStateMs.value
    lateinit var loadWbInternalApplier: LoadWorkbookInternalApplierImp
    lateinit var errorRouter: ErrorRouter
    lateinit var ts: TestSample

    @BeforeTest
    fun b() {
        ts = TestSample()
        appStateMs = ts.sampleAppStateMs()
        errorRouter = ErrorRouterImp(appStateMs)
        loadWbInternalApplier = LoadWorkbookInternalApplierImp(ts.stateContMs())
    }

    @Test
    fun `applyLoadWorkbook std case`() {
        val windowId = appStateMs.value.windowStateMsList[0].value.id
        val wb = WorkbookImp(WorkbookKey("Book33").toMs())
        ts.stateContMs().value.wbCont = ts.stateContMs().value.wbCont.addWb(wb)
        loadWbInternalApplier.apply(windowId, wb)
        assertNotNull(appState.getWbState(wb.key))
    }

    @Test
    fun `apply Load Workbook into invalid window`() {
        val windowId = "invalid wd id"
        val wb = WorkbookImp(WorkbookKey("Book33").toMs())
        ts.stateContMs().value.wbCont = ts.stateContMs().value.wbCont.addWb(wb)
        loadWbInternalApplier.apply(windowId, wb)
        val wds = appState.getWindowStateMsById(windowId)
        assertNotNull(wds)
        assertEquals(listOf(wb), wds.value.workbookList)
        assertEquals(listOf(wb.key), wds.value.workbookStateList.map { it.wbKey })
    }
}
