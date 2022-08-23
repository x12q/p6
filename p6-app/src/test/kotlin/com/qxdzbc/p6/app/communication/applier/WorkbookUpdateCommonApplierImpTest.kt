package com.qxdzbc.p6.app.communication.applier

import com.qxdzbc.p6.app.action.applier.BaseApplierImp
import com.qxdzbc.p6.app.action.applier.ErrorApplierImp
import com.qxdzbc.p6.app.action.applier.WorkbookUpdateCommonApplierImp
import com.qxdzbc.p6.app.action.common_data_structure.WorkbookUpdateCommonResponse
import com.qxdzbc.p6.app.action.worksheet.update_multi_cell.DeleteMultiResponse
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookImp
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.p6.common.exception.error.ErrorHeader
import com.qxdzbc.p6.common.exception.error.ErrorReport
import com.qxdzbc.p6.ui.app.ErrorRouter
import com.qxdzbc.p6.ui.app.ErrorRouterImp
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState
import com.qxdzbc.p6.ui.window.state.WindowState
import org.mockito.kotlin.mock
import test.TestSample

import kotlin.test.*


internal class WorkbookUpdateCommonApplierImpTest {

    lateinit var appStateMs: Ms<AppState>
    val appState get() = appStateMs.value
    val workbook: Workbook get() = appState.globalWbCont.getWb(TestSample.wbk1)!!
    lateinit var workbookStateMs: Ms<WorkbookState>
    lateinit var windowStateMs: Ms<WindowState>
    lateinit var s1: Worksheet
    lateinit var s2: Worksheet
    lateinit var applier: WorkbookUpdateCommonApplierImp
    lateinit var testSample:TestSample
    @BeforeTest
    fun beforeTest() {
        testSample = TestSample()
        appStateMs = testSample.sampleAppStateMs()
        s2 = workbook.getWs(1)!!
        s1 = workbook.getWs(0)!!

        appState.queryStateByWorkbookKey(TestSample.wbk1).ifOk {
            workbookStateMs = it.workbookStateMs
            windowStateMs = it.windowStateMs
        }
        applier = WorkbookUpdateCommonApplierImp(
            stateContMs = testSample.stateContMs(),
            baseApplier = BaseApplierImp(ErrorApplierImp(ErrorRouterImp(appStateMs)), mock<ErrorRouter>())
        )
    }

    @Test
    fun `applyDeleteMulti ok msg`() {
        val key1 = TestSample.wbk1
        val q = appStateMs.value.queryStateByWorkbookKey(key1)
        val newWb = WorkbookImp(keyMs = key1.toMs())
        val r = WorkbookUpdateCommonResponse(
            wbKey = key1,
            newWorkbook = newWb
        )
        assertTrue { q.oddityContainerMs.value.isEmpty() }
        assertTrue { appStateMs.value.oddityContainer.isEmpty() }
        applier.apply(r)
        assertTrue { q.oddityContainerMs.value.isEmpty() }
        assertTrue { appStateMs.value.oddityContainer.isEmpty() }
    }

    @Test
    fun `applyDeleteMulti error response`() {
        val appState = appStateMs
        val e = ErrorReport(
            header = ErrorHeader("z", "q")
        )
        val response = DeleteMultiResponse(
            WorkbookUpdateCommonResponse(
                errorReport = e,
                wbKey = TestSample.wbk1
            )
        )
        assertTrue { appState.value.windowStateMsList.first().value.oddityContainer.isEmpty() }
        applier.apply(response)
        assertTrue {
            // oddity container of the window is not empty
            appState.value.windowStateMsList.first().value.oddityContainer.isNotEmpty()
        }
    }


    private fun stateIsUnchanged() {
        assertEquals(s2.name, workbook.getWs(1)?.name)
        assertEquals(s1.name, workbook.getWs(0)?.name)
        assertEquals(s1.name, workbookStateMs.value.activeSheetPointer.wsName)
        assertNotNull(workbook.getWs(s1.name))
        assertNotNull(workbook.getWs(s2.name))
        assertTrue { appState.oddityContainer.isEmpty() }
        assertTrue { appState.windowStateMsList.first().value.oddityContainer.isEmpty() }
    }

}
