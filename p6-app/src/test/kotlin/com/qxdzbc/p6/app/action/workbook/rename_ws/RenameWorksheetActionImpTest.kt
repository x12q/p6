package com.qxdzbc.p6.app.action.workbook.rename_ws

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.worksheet.rename_ws.RenameWorksheetRequest
import com.qxdzbc.p6.app.action.worksheet.rename_ws.RenameWorksheetResponse
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.qxdzbc.p6.ui.app.error_router.ErrorRouterImp
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState
import com.qxdzbc.p6.ui.window.state.WindowState
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import test.BaseAppStateTest
import test.TestSample
import kotlin.test.*

internal class RenameWorksheetActionImpTest : BaseAppStateTest() {
    val workbook: Workbook get() = sc.wbCont.getWb(ts.wbKey1)!!
    lateinit var workbookStateMs: Ms<WorkbookState>
    lateinit var windowStateMs: Ms<WindowState>
    lateinit var ws1: Worksheet
    lateinit var ws2: Worksheet

    lateinit var action: RenameWorksheetActionImp
    lateinit var errorRouter: ErrorRouter

    @BeforeTest
    fun beforeTest() {
        ws2 = workbook.getWs(1)!!
        ws1 = workbook.getWs(0)!!
        workbookStateMs = ts.sc.getWbStateMs(ts.wbKey1)!!
        windowStateMs = ts.sc.getWindowStateMsByWbKey(ts.wbKey1)!!
        errorRouter = ErrorRouterImp(ts.sc, ts.appState.errorContainerMs)
        action = ts.comp.renameWorksheetActionImp()
    }

    @Test
    fun testCommand(){
        val newName = "newName"
        val oldSheetName = workbook.getWs(0)!!.name
        val res = RenameWorksheetRequest(
            wbKey = workbook.key,
            oldName = oldSheetName,
            newName = newName,
        )
        val command = action.makeCommand(res)
        command.shouldNotBeNull()
        val oldWbWs= WbWs(workbook.key,oldSheetName)
        val newWbWs = WbWs(workbook.key,newName)


        preCondition {
            ts.sc.getWs(oldWbWs).shouldNotBeNull()
            ts.sc.getWs(newWbWs).shouldBeNull()
        }
        command.run()
        postCondition {
            ts.sc.getWs(newWbWs).shouldNotBeNull()
            ts.sc.getWs(oldWbWs).shouldBeNull()
        }

        action("interfere action: rename the workbook before undo the command. Expect the command to undo successfully"){
            workbook.keyMs.value = workbook.keyMs.value.setName("new book name")
        }

        command.undo()
        postCondition {
            ts.sc.getWs(WbWs(ts.wbKey1,oldSheetName)).shouldNotBeNull()
            ts.sc.getWs(WbWs(ts.wbKey1,newName)).shouldBeNull()
        }
    }


    @Test
    fun `apply - encounter error when rename`() {
        /*
         apply rename data, but encounter an error,
         Expect: the error to be routed to the correct place
         */

        val newName = "newName"
        val res = RenameWorksheetResponse(
            wbKey = workbook.key,
            oldName = "invalid_old_name",
            newName = newName,
        )
        action.apply(res.wbKey, res.oldName, res.newName)

        workbook.getWs(1)?.name shouldBe ws2.name

        workbookStateMs.value.activeSheetPointer.wsName shouldBe ws1.name
        workbook.getWs(ws2.name).shouldNotBeNull()
        windowStateMs.value.errorContainer.isNotEmpty().shouldBeTrue()
    }

    @Test
    fun `apply - on activate sheet`() {
        /*
        Apply rename data on active sheet.
        Expect:
           - the active sheet name to change
           - active sheet pointer also change
         */
        val newSheetName = "newName"
        val workbook = sc.wbCont.getWb(TestSample.wbk1)!!
        val oldSheetName = workbook.getWs(0)!!.name
        val res = RenameWorksheetResponse(
            wbKey = workbook.key,
            oldName = oldSheetName,
            newName = newSheetName,
            isError = false,
        )

        action.apply(res.wbKey, res.oldName, res.newName)

        val wbState = ts.sc.getWbState(workbook.key)
        wbState.shouldNotBeNull()
        wbState.wb.getWs(0)!!.name shouldBe newSheetName
        wbState.wb.getWs(oldSheetName).shouldBeNull()
        wbState.activeSheetPointer.wsName shouldBe newSheetName
    }

    @Test
    fun `apply - on inactive sheet`() {
        val newName = "newName"
        val workbook = sc.wbCont.getWb(TestSample.wbk1)!!
        val oldSheetName = workbook.getWs(1)!!.name
        val res = RenameWorksheetResponse(
            wbKey = workbook.key,
            oldName = oldSheetName,
            newName = newName,
        )
        action.apply(res.wbKey, res.oldName, res.newName)

        val wbState = ts.sc.getWbState(workbook.key)
        wbState?.wb?.getWs(1)?.name shouldBe newName
        wbState?.wb?.getWs(oldSheetName).shouldBeNull()

        ts.sc.getWindowStateByWbKey(workbook.key)?.errorContainer?.isEmpty()!!.shouldBeTrue()
        ts.appState.errorContainer.isEmpty().shouldBeTrue()
    }

    @Test
    fun `apply- with non-existing WorkbookKey`() {
        /*
        Attempt to rename a worksheet on a non-existing workbook.
        Expect nothing is changed and an error is appended in to app level error container
         */
        val newName = "newName"
        val workbook = sc.wbCont.getWb(TestSample.wbk1)!!
        val s2 = workbook.getWs(1)!!
        val oldSheetName = s2.name

        val res = RenameWorksheetResponse(
            wbKey = WorkbookKey("non existing wb key", null),
            oldName = oldSheetName,
            newName = newName,
        )
        ts.appState.errorContainer.isNotEmpty() shouldBe false
        action.apply(res.wbKey, res.oldName, res.newName)
        ts.appState.errorContainer.isNotEmpty() shouldBe true
    }

}

