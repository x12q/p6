package com.qxdzbc.p6.ui.document.worksheet.actions.action1

import com.qxdzbc.p6.app.action.cell.CellRM
import com.qxdzbc.p6.app.action.worksheet.WorksheetRM
import com.qxdzbc.p6.app.action.range.range_to_clipboard.RangeToClipboardActionImp
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetState
import org.mockito.kotlin.mock
import test.TestSample

import kotlin.test.BeforeTest

/**
 * TODO test this
 */
internal class RangeToClipboardActionImpTest {

    lateinit var action: RangeToClipboardActionImp
    lateinit var wsRequestMaker: WorksheetRM
    lateinit var cellRM: CellRM
    lateinit var appState: AppState
    lateinit var stateCont:StateContainer
    lateinit var wsStateMs: Ms<WorksheetState>
    lateinit var testSample: TestSample
    @BeforeTest
    fun beforeTest() {
        testSample = TestSample()
        appState = testSample.sampleAppState()
        stateCont = testSample.sc
        wsStateMs = stateCont.getStateByWorkbookKey(TestSample.wbk1)!!.workbookStateMs!!.getWsStateMs("Sheet1")!!
        wsRequestMaker = mock()
        cellRM = mock()
    }
}
