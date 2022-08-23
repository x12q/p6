package com.qxdzbc.p6.app.communication.applier.workbook

import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.common.compose.Ms
import test.TestSample

import kotlin.test.*

class WorkbookEventApplyLogicImpTest {

    lateinit var appStateMs:Ms<AppState>
    lateinit var ts:TestSample
    val appState get()=appStateMs.value
    @BeforeTest
    fun b(){
        ts = TestSample()
        appStateMs = ts.appStateMs
    }

//    @Test
//    fun applyDeleteWorksheet() {
//        val res = DeleteWorksheetResponse(
//            workbookKey = sampleWbKey1,
//            targetWorksheetName = "Sheet1",
//            isError = false,
//            errorReport = null
//        )
//        logic.applyDeleteWorksheet(res)
//        val q = appState.queryStateByWorkbookKey(sampleWbKey1)
//        assertTrue { q.isOk }
//        assertNull(q.workbookStateMs.value.workbook?.getSheet(res.targetWorksheetName))
//        assertNull(q.workbookStateMs.value.getSheetState(res.targetWorksheetName))
//    }

//    @Test
//    fun applySaveWorkbook() {
//        val res = SaveWorkbookResponse(
//            isError = false,
//            errorReport = null,
//            workbookKey = sampleWbKey1,
//            path = "new_path/file.txt"
//        )
//        val newWbKey = WorkbookKey(name="file.txt",path = Path.of(res.path))
//        assertNull(appState.wbCont.getWb(newWbKey))
//        assertNull(appState.getWindowStateMsByWorkbookKey(newWbKey))
//        logic.applySaveWorkbook(res)
//        assertNull(appState.wbCont.getWb(sampleWbKey1))
//        assertNotNull(appState.wbCont.getWb(newWbKey))
//
//        val windowState = appState.getWindowStateMsByWorkbookKey(newWbKey)
//        assertNotNull(windowState)
//
//        val wsState = windowState.value.getWorkbookState(newWbKey)
//        assertNotNull(wsState)
//        assertEquals(newWbKey,wsState.workbookKey)
//    }
}
