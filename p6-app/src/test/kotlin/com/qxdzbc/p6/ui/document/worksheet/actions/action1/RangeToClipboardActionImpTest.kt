package com.qxdzbc.p6.ui.document.worksheet.actions.action1

import com.qxdzbc.p6.app.action.range.range_to_clipboard.RangeToClipboardActionImp
import com.qxdzbc.p6.ui.app.state.AppState
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
    lateinit var appState: AppState
    lateinit var stateCont:StateContainer
    lateinit var wsState: WorksheetState
    lateinit var testSample: TestSample
    @BeforeTest
    fun beforeTest() {
        testSample = TestSample()
        appState = testSample.sampleAppState()
        stateCont = testSample.sc
        wsState = stateCont.getStateByWorkbookKey(TestSample.wbk1)!!.workbookStateMs!!.getWsStateMs("Sheet1")!!
    }
}
