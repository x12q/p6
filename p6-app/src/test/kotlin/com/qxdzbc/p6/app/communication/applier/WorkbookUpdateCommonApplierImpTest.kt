package com.qxdzbc.p6.app.communication.applier

import com.github.michaelbull.result.onSuccess
import com.qxdzbc.p6.app.action.applier.WorkbookUpdateCommonApplierImp
import com.qxdzbc.p6.app.action.common_data_structure.WorkbookUpdateCommonResponse
import com.qxdzbc.p6.app.action.worksheet.update_multi_cell.DeleteMultiResponse
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookImp
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.common.error.SingleErrorReport
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState
import com.qxdzbc.p6.ui.window.state.WindowState
import test.TestSample

import kotlin.test.*


internal class WorkbookUpdateCommonApplierImpTest {
    lateinit var ts:TestSample
    lateinit var appState: AppState
    val workbook: Workbook get() = ts.sc.wbCont.getWb(TestSample.wbk1)!!
    lateinit var workbookStateMs: WorkbookState
    lateinit var windowState: WindowState
    lateinit var s1: Worksheet
    lateinit var s2: Worksheet
    lateinit var applier: WorkbookUpdateCommonApplierImp

    @BeforeTest
    fun beforeTest() {
        ts = TestSample()
        appState = ts.sampleAppState()
        s2 = workbook.getWs(1)!!
        s1 = workbook.getWs(0)!!

        appState.stateCont.getStateByWorkbookKeyRs(TestSample.wbk1).onSuccess {
            workbookStateMs = it.workbookStateMs!!
            windowState = it.windowState!!
        }
        applier = WorkbookUpdateCommonApplierImp(
            stateCont = ts.sc,
            errorRouter = ts.errorRouter
        )
    }

    @Test
    fun `applyDeleteMulti ok msg`() {
        val key1 = TestSample.wbk1
        val q = appState.stateCont.getStateByWorkbookKey(key1)!!
        val newWb = WorkbookImp(keyMs = key1.toMs())
        val r = WorkbookUpdateCommonResponse(
            wbKey = key1,
            newWorkbook = newWb
        )
        assertTrue { q.windowState!!.errorContainer.isEmpty() }
        assertTrue { appState.appErrorContainer.isEmpty() }
        applier.apply(r)
        assertTrue { q.windowState!!.errorContainer.isEmpty() }
        assertTrue { appState.appErrorContainer.isEmpty() }
    }

    @Test
    fun `applyDeleteMulti error response`() {
        val e = SingleErrorReport.random()
        val response = DeleteMultiResponse(
            WorkbookUpdateCommonResponse(
                errorReport = e,
                wbKey = TestSample.wbk1
            )
        )
        assertTrue { ts.sc.windowStateMsList.first().errorContainer.isEmpty() }
        applier.apply(response)
        assertTrue {
            // oddity container of the window is not empty
            ts.sc.windowStateMsList.first().errorContainer.isNotEmpty()
        }
    }


    private fun stateIsUnchanged() {
        assertEquals(s2.name, workbook.getWs(1)?.name)
        assertEquals(s1.name, workbook.getWs(0)?.name)
        assertEquals(s1.name, workbookStateMs.activeSheetPointer.wsName)
        assertNotNull(workbook.getWs(s1.name))
        assertNotNull(workbook.getWs(s2.name))
        assertTrue { appState.appErrorContainer.isEmpty() }
        assertTrue { ts.sc.windowStateMsList.first().errorContainer.isEmpty() }
    }

}
