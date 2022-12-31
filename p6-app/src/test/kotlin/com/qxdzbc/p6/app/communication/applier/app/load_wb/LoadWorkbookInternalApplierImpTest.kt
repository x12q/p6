package com.qxdzbc.p6.app.communication.applier.app.load_wb

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.p6.app.action.app.load_wb.LoadWorkbookActionImp
import com.qxdzbc.p6.app.action.app.load_wb.applier.LoadWorkbookInternalApplierImp
import com.qxdzbc.p6.app.document.workbook.WorkbookImp
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.qxdzbc.p6.ui.app.error_router.ErrorRouterImp
import com.qxdzbc.p6.ui.app.state.StateContainer
import test.TestSample
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class LoadWorkbookInternalApplierImpTest {
    lateinit var scMs: Ms<StateContainer>
    val appState get() = scMs.value
    lateinit var loadWbInternalApplier: LoadWorkbookActionImp
    lateinit var errorRouter: ErrorRouter
    lateinit var ts: TestSample

    @BeforeTest
    fun b() {
        ts = TestSample()
        scMs = ts.scMs
        errorRouter = ErrorRouterImp(scMs,ts.appState.errorContainerMs)
        loadWbInternalApplier = ts.comp.loadWorkbookActionImp()
    }

    @Test
    fun `applyLoadWorkbook std case`() {
        val windowId = scMs.value.windowStateMsList[0].value.id
        val wb = WorkbookImp(WorkbookKey("Book33").toMs())
        ts.stateContMs().value.wbCont = ts.stateContMs().value.wbCont.addWb(wb)
        loadWbInternalApplier.apply(windowId, wb,null)
        assertNotNull(appState.getWbState(wb.key))
    }

    @Test
    fun `apply Load Workbook into invalid window`() {
        val windowId = "invalid wd id"
        val wb = WorkbookImp(WorkbookKey("Book33").toMs())
        ts.stateContMs().value.wbCont = ts.stateContMs().value.wbCont.addWb(wb)
        loadWbInternalApplier.apply(windowId, wb,null)
        val wds = appState.getWindowStateMsById(windowId)
        assertNotNull(wds)
        assertEquals(listOf(wb), wds.value.wbList)
        assertEquals(listOf(wb.key), wds.value.wbStateList.map { it.wbKey })
    }
}
