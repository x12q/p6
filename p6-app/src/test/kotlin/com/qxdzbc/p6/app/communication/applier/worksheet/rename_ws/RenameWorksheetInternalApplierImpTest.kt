package com.qxdzbc.p6.app.communication.applier.worksheet.rename_ws

import com.qxdzbc.p6.app.action.worksheet.rename_ws.applier.RenameWorksheetInternalApplierImp
import com.qxdzbc.p6.app.action.worksheet.rename_ws.RenameWorksheetResponse
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.p6.ui.app.ErrorRouter
import com.qxdzbc.p6.ui.app.ErrorRouterImp
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.p6.ui.common.compose.Ms
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState
import com.qxdzbc.p6.ui.window.state.WindowState
import test.TestSample

import kotlin.test.*

class RenameWorksheetInternalApplierImpTest {

    lateinit var appStateMs: Ms<AppState>
    val appState get() = appStateMs.value
    val workbook: Workbook get() = appState.globalWbCont.getWb(TestSample.wbk1)!!
    lateinit var workbookStateMs: Ms<WorkbookState>
    lateinit var windowStateMs: Ms<WindowState>
    lateinit var s1: Worksheet
    lateinit var s2: Worksheet

    lateinit var applier: RenameWorksheetInternalApplierImp
    lateinit var errorRouter: ErrorRouter
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
        errorRouter = ErrorRouterImp(appStateMs)
        applier = RenameWorksheetInternalApplierImp(
            appStateMs, errorRouter
        )
    }

    @Test
    fun `renameLogic error when rename`() {
        val newName = "newName"
        val res = RenameWorksheetResponse(
            wbKey = workbook.key,
            oldName = "invalid_old_name",
            newName = newName,
        )
        applier.apply(res.wbKey, res.oldName, res.newName)

        assertEquals(s2.name, workbook.getWs(1)?.name)
        assertEquals(s1.name, workbookStateMs.value.activeSheetPointer.wsName)
        assertNotNull(workbook.getWs(s2.name))
        assertTrue(windowStateMs.value.oddityContainer.isNotEmpty())
    }

    @Test
    fun `renameLogic on activate sheet`() {
        val newSheetName = "newName"
        val workbook = appState.globalWbCont.getWb(TestSample.wbk1)!!
        val oldSheetName = workbook.getWs(0)!!.name
        val res = RenameWorksheetResponse(
            wbKey = workbook.key,
            oldName = oldSheetName,
            newName = newSheetName,
            isError = false,
        )

        applier.apply(res.wbKey, res.oldName, res.newName)
        assertTrue(appState.oddityContainer.isEmpty())

        val q = appState.queryStateByWorkbookKey(TestSample.wbk1)
        assertTrue { q.isOk }
        q.ifOk {
            val newWb = it.workbookStateMs.value.wb
            assertNotNull(newWb)
            assertEquals(newSheetName, newWb.getWs(0)?.name)
            assertNull(newWb.getWs(oldSheetName))
            assertEquals(newSheetName, it.workbookStateMs.value.activeSheetPointer.wsName)
            assertTrue(it.windowStateMs.value.oddityContainer.isEmpty())
        }
    }

    @Test
    fun `renameLogic on inactive sheet`() {
        val newName = "newName"
        val workbook = appState.globalWbCont.getWb(TestSample.wbk1)!!
        val oldSheetName = workbook.getWs(1)!!.name
        val res = RenameWorksheetResponse(
            wbKey = workbook.key,
            oldName = oldSheetName,
            newName = newName,
        )
        applier.apply(res.wbKey, res.oldName, res.newName)
        val q = appState.queryStateByWorkbookKey(TestSample.wbk1)
        assertTrue { q.isOk }
        q.ifOk {
            assertEquals(newName, it.workbookStateMs.value.wb?.getWs(1)?.name)
            assertNull(it.workbookStateMs.value.wb?.getWs(oldSheetName))
            assertTrue(it.windowStateMs.value.oddityContainer.isEmpty())
            assertTrue(appState.oddityContainer.isEmpty())
        }
    }

    @Test
    fun `renameLogic with incorrect WorkbookKey`() {
        val newName = "newName"
        val workbook = appState.globalWbCont.getWb(TestSample.wbk1)!!
        val s2 = workbook.getWs(1)!!
        val s1 = workbook.getWs(0)!!
        val oldSheetName = s2.name

        val res = RenameWorksheetResponse(
            wbKey = WorkbookKey("IncorrectKey", null),
            oldName = oldSheetName,
            newName = newName,
        )
        applier.apply(res.wbKey, res.oldName, res.newName)

        val q = appState.queryStateByWorkbookKey(TestSample.wbk1)
        assertTrue { q.isOk }
        q.ifOk {
            assertEquals(s2.name, workbook.getWs(1)?.name)
            assertEquals(s1.name, it.workbookStateMs.value.activeSheetPointer.wsName)
            assertNotNull(it.workbookStateMs.value.wb?.getWs(s2.name))
            assertTrue(appState.oddityContainer.isNotEmpty())
        }
    }
}
