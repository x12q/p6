package com.emeraldblast.p6.app.communication.applier.app.load_wb

import com.emeraldblast.p6.app.action.app.load_wb.applier.LoadWorkbookInternalApplierImp
import com.emeraldblast.p6.app.document.workbook.WorkbookImp
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.ui.app.ErrorRouter
import com.emeraldblast.p6.ui.app.ErrorRouterImp
import com.emeraldblast.p6.ui.app.state.AppState
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.common.compose.MsUtils.toMs
import test.TestSample
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class LoadWorkbookInternalApplierImpTest {
    lateinit var appStateMs: Ms<AppState>
    val appState get() = appStateMs.value
    lateinit var logic: LoadWorkbookInternalApplierImp
    lateinit var errorRouter: ErrorRouter
    lateinit var testSample: TestSample
    @BeforeTest
    fun b() {
        testSample = TestSample()
        appStateMs = testSample.sampleAppStateMs()
        errorRouter = ErrorRouterImp(appStateMs)
        logic = LoadWorkbookInternalApplierImp(appStateMs,)
    }

    @Test
    fun `applyLoadWorkbook std case`() {
        val windowId = appStateMs.value.windowStateMsList[0].value.id
        val wb = WorkbookImp(WorkbookKey("Book3").toMs())
        logic.apply(windowId,wb)
        assertNotNull(appState.getWorkbookState(wb.key))
    }

    @Test
    fun `applyLoadWorkbook invalid window`() {
        val windowId = "invalid wd id"
        val wb = WorkbookImp(WorkbookKey("Book3").toMs())
        logic.apply(windowId,wb)
        val wds = appState.getWindowStateMsById(windowId)
        assertNotNull(wds)
        assertEquals(listOf(wb), wds.value.workbookList)
        assertEquals(listOf(wb.key), wds.value.workbookStateList.map { it.wbKey })
    }
}
