package com.qxdzbc.p6.app.action.workbook.new_worksheet.applier

import com.qxdzbc.p6.app.action.workbook.new_worksheet.rm.CreateNewWorksheetResponse2
import com.qxdzbc.p6.app.common.err.ErrorReportWithNavInfo.Companion.withNav
import com.qxdzbc.common.ResultUtils.toOk
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.common.compose.Ms
import com.github.michaelbull.result.Err
import test.TestSample
import kotlin.test.*

class NewWorksheetApplierImpTest{

    lateinit var appStateMs:Ms<AppState>
    lateinit var ts: TestSample
    val appState get()=appStateMs.value
    lateinit var internalApplier:NewWorksheetInternalApplierImp
    lateinit var applier: NewWorksheetApplierImp
    val wbk= TestSample.wbk1
    @BeforeTest
    fun b(){
        ts = TestSample()
        appStateMs = ts.appStateMs
        internalApplier = NewWorksheetInternalApplierImp(
            appStateMs,ts.p6Comp.errorRouter()
        )
        applier = NewWorksheetApplierImp(
            internalApplier, ts.p6Comp.errorRouter()
        )
    }

    @Test
    fun applyNewWorksheet() {

        val wb = appState.globalWbCont.getWb(wbk)!!
        val wsName = "NewSheet"
        val res = CreateNewWorksheetResponse2(
            newWsName = wsName,
            newWb = wb.createNewWs(wsName)
        )
        applier.applyRes2(res.toOk())
        val q = appState.queryStateByWorkbookKey(wbk)
        assertTrue { q.isOk }
        assertNotNull(q.workbookStateMs.value.wb.getWs(wsName))
        assertNotNull(q.workbookStateMs.value.getWsStateMs(wsName))
    }
    @Test
    fun `applyNewWorksheet err`() {
        val wsName = "NewSheet"
        val res = Err(
            TestSample.sampleErrorReport.withNav(
            wbKey = wbk
        ))
        applier.applyRes2(res)
        val q = appState.queryStateByWorkbookKey(wbk)
        assertTrue { q.isOk }
        assertNull(q.workbookStateMs.value.wb.getWs(wsName))
        assertNull(q.workbookStateMs.value.getWsState(wsName))
        assertTrue { q.oddityContainerMs.value.isEmpty().not() }
        val reportedError = q.oddityContainerMs.value.oddList.first().errorReport
        assertTrue { reportedError.isType(TestSample.sampleErrorReport) }
    }

}
