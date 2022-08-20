package com.emeraldblast.p6.ui.document.worksheet.actions.action1

import com.emeraldblast.p6.app.action.cell.CellRM
import com.emeraldblast.p6.app.action.worksheet.WorksheetApplier
import com.emeraldblast.p6.app.action.worksheet.WorksheetRM
import com.emeraldblast.p6.app.action.range.range_to_clipboard.RangeToClipboardActionImp
import com.emeraldblast.p6.ui.app.state.AppState
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.document.worksheet.state.WorksheetState
import org.mockito.kotlin.mock
import test.TestSample

import kotlin.test.BeforeTest

internal class RangeToClipboardActionImpTest {

    lateinit var action: RangeToClipboardActionImp
    lateinit var wsRequestMaker: WorksheetRM
    lateinit var cellRM: CellRM
    lateinit var wsHandler: WorksheetApplier
    lateinit var appStateMs: Ms<AppState>
    lateinit var wsStateMs: Ms<WorksheetState>
    lateinit var testSample: TestSample
    @BeforeTest
    fun beforeTest() {
        testSample = TestSample()
        appStateMs = testSample.sampleAppStateMs()
        wsStateMs = appStateMs.value.queryStateByWorkbookKey(TestSample.wbk1).workbookStateMs.value.getWsStateMs("Sheet1")!!
        wsRequestMaker = mock()
        cellRM = mock()
        wsHandler = mock()
    }
}
