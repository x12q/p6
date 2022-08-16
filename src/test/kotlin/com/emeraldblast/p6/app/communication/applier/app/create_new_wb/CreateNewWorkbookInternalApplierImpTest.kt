package com.emeraldblast.p6.app.communication.applier.app.create_new_wb


import androidx.compose.runtime.getValue
import com.emeraldblast.p6.app.action.app.create_new_wb.applier.CreateNewWorkbookInternalApplierImp
import com.emeraldblast.p6.app.action.app.create_new_wb.CreateNewWorkbookResponse
import com.emeraldblast.p6.app.document.workbook.WorkbookImp
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.common.exception.error.CommonErrors
import com.emeraldblast.p6.ui.app.ErrorRouter
import com.emeraldblast.p6.ui.app.state.AppState
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.common.compose.MsUtils.toMs
import com.emeraldblast.p6.ui.window.state.WindowState
import org.mockito.kotlin.mock
import test.TestSample

import kotlin.test.*

class CreateNewWorkbookInternalApplierImpTest {

    lateinit var applier: CreateNewWorkbookInternalApplierImp
    lateinit var appStateMs:Ms<AppState>
    lateinit var errRouter: ErrorRouter
    lateinit var windowStateMs:Ms<WindowState>
    val newWB = WorkbookImp(WorkbookKey("newWb").toMs())
    lateinit var okRes: CreateNewWorkbookResponse
    lateinit var errRes: CreateNewWorkbookResponse
    lateinit var testSample:TestSample
    @BeforeTest
    fun b(){
        errRouter = mock()
        testSample = TestSample()
        appStateMs = testSample.sampleAppStateMs()
        applier = CreateNewWorkbookInternalApplierImp(
            appStateMs=appStateMs,
        )
        windowStateMs =appStateMs.value.windowStateMsList[0]
        okRes = CreateNewWorkbookResponse(
            isError = false,
            errorReport = null,
            workbook = newWB,
            windowId = windowStateMs.value.id
        )
        errRes = CreateNewWorkbookResponse(
            isError = true,
            errorReport = CommonErrors.Unknown.header.toErrorReport(),
            workbook = null,
            windowId = windowStateMs.value.id
        )
    }
    @Test
    fun `apply ok on window`() {
        val wds by windowStateMs
        assertNull(appStateMs.value.globalWbCont.getWb(newWB.key))
        assertNull(appStateMs.value.getWindowStateMsByWbKey(newWB.key))
        /**/
        applier.apply(okRes.workbook,okRes.windowId)
        assertNotNull(appStateMs.value.globalWbCont.getWb(newWB.key))
        assertNotNull(appStateMs.value.getWindowStateMsByWbKey(newWB.key))
    }

    @Test
    fun `apply ok with null window id`() {
        val res = okRes.copy(windowId = null)
        val wdsCount = appStateMs.value.windowStateMsList.size
        testApplyOnApp(res)
        assertEquals(wdsCount+1,appStateMs.value.windowStateMsList.size)
    }

    fun testApplyOnApp(res: CreateNewWorkbookResponse){
        assertNull(appStateMs.value.globalWbCont.getWb(newWB.key))
        assertNull(appStateMs.value.getWindowStateMsByWbKey(newWB.key))
        /**/
        applier.apply(res.workbook,res.windowId)
        assertNotNull(appStateMs.value.globalWbCont.getWb(newWB.key))
        assertNotNull(appStateMs.value.getWindowStateMsByWbKey(newWB.key))
    }

    @Test
    fun `apply ok with invalid window id`() {
        val res = okRes.copy(windowId = "invalid window id")
        val wdsCount = appStateMs.value.windowStateMsList.size
        testApplyOnApp(res)
        assertEquals(wdsCount+1,appStateMs.value.windowStateMsList.size)
    }

//    @Test
//    fun `apply ok with invalid window id on single window`() {
//        appStateMs.value = TestSample.sampleAppStateMs().value
//        val res:CreateNewWorkbookResponse = okRes.copy(windowId = "invalid window id")
//        val wdsCount = appStateMs.value.windowStateMsList.size
//        testApplyOnApp(res)
//        assertEquals(wdsCount,appStateMs.value.windowStateMsList.size)
//    }
}
