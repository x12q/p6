package com.qxdzbc.p6.app.action.app.create_new_wb

import androidx.compose.runtime.getValue
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.common.error.CommonErrors
import com.qxdzbc.p6.app.action.app.close_wb.CloseWorkbookAction
import com.qxdzbc.p6.app.action.app.close_wb.CloseWorkbookRequest
import com.qxdzbc.p6.app.document.workbook.WorkbookImp
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.window.state.WindowState
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.mockito.kotlin.mock
import test.BaseAppStateTest
import test.TestSample
import kotlin.test.*

class CreateNewWorkbookActionTest : BaseAppStateTest(){

    lateinit var action: CreateNewWorkbookActionImp
    lateinit var closeWbAct: CloseWorkbookAction
    val wbk = WorkbookKey("NewWb")

    lateinit var scMs:StateContainer
    lateinit var errRouter: ErrorRouter
    lateinit var windowStateMs: WindowState
    val newWB = WorkbookImp(WorkbookKey("newWb").toMs())
    lateinit var okRes: CreateNewWorkbookResponse
    lateinit var errRes: CreateNewWorkbookResponse


    @BeforeTest
    fun b() {
        errRouter = mock()
        action = ts.comp.createNewWbActionImp()
        closeWbAct = ts.comp.closeWbAct()
        scMs = ts.comp.stateContainer
        windowStateMs = scMs.windowStateMsList[0]
        okRes = CreateNewWorkbookResponse(
            errorReport = null,
            wb = newWB,
            windowId = windowStateMs.id
        )
        errRes = CreateNewWorkbookResponse(
            errorReport = CommonErrors.Unknown.header.toErrorReport(),
            wb = null,
            windowId = windowStateMs.id
        )
    }

    @Test
    fun `create new wb when there is no wb`() {

        val windowState=sc.getWindowStateMsById(ts.window1Id)
        assertNotNull(windowState)
        val ws = windowState
        val wbKeySet=ws.wbKeySet
        for(k in wbKeySet){
            closeWbAct.closeWb(CloseWorkbookRequest(
                wbKey =k,
                windowId = ws.id
            ))
        }
        assertTrue(ws.wbList.isEmpty())
        assertTrue(ws.wbKeySet.isEmpty())
        val req = CreateNewWorkbookRequest(
            windowId = ts.window1Id,
            wbName = wbk.name
        )
        action.createNewWb(req)
        assertTrue(wbk in ws.wbKeySet)
        assertTrue(ws.activeWbPointer.isPointingTo(wbk))
    }

    @Test
    fun createNewWb() {
        val req = CreateNewWorkbookRequest(
            windowId = null,
            wbName = wbk.name
        )

        assertNull(ts.sc.getWb(wbk))
        val o = action.createNewWb(req)
        assertTrue(o.isOk)
        assertNull(o.errorReport)
        assertNotNull(o.wb)
        assertEquals(wbk, o.wb?.key)
        assertNotNull(ts.sc.getWb(wbk))

        val o2 = action.createNewWb(req)
        assertTrue(o2.isError)
        assertNotNull(o2.errorReport)
        assertNull(o2.wb)
    }

    @Test
    fun `apply ok on window`() {
        scMs.wbCont.getWb(newWB.key).shouldBeNull()
        scMs.getWindowStateMsByWbKey(newWB.key).shouldBeNull()
        /**/
        action.iapply(okRes.wb, okRes.windowId)
        scMs.wbCont.getWb(newWB.key).shouldNotBeNull()
        scMs.getWindowStateMsByWbKey(newWB.key).shouldNotBeNull()
    }

    @Test
    fun `apply ok with null window id`() {
        val res = okRes.copy(windowId = null)
        val wdsCount = scMs.windowStateMsList.size
        testApplyOnApp(res)
        scMs.windowStateMsList.size shouldBe wdsCount
    }

    @Test
    fun `apply ok with invalid qqq window id`() {
        val res = okRes.copy(windowId = "new windowId")
        val wdsCount = scMs.windowStateMsList.size
        testApplyOnApp(res)
        scMs.windowStateMsList.size shouldBe wdsCount+1
    }

    fun testApplyOnApp(res: CreateNewWorkbookResponse) {
        scMs.wbCont.getWb(newWB.key).shouldBeNull()
        scMs.getWindowStateMsByWbKey(newWB.key).shouldBeNull()
        /**/
        action.iapply(res.wb, res.windowId)
        scMs.wbCont.getWb(newWB.key).shouldNotBeNull()
        scMs.getWindowStateMsByWbKey(newWB.key).shouldNotBeNull()
    }

    @Test
    fun `apply ok with invalid window id`() {
        val res = okRes.copy(windowId = "invalid window id")
        val wdsCount = scMs.windowStateMsList.size
        testApplyOnApp(res)
        scMs.windowStateMsList.size shouldBe wdsCount + 1
    }
}
