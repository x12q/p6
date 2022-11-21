package com.qxdzbc.p6.app.communication.applier.range.range_to_clipboard

import com.qxdzbc.p6.app.action.range.range_to_clipboard.applier.RangeToClipboardInternalApplierImp
import com.qxdzbc.p6.app.action.common_data_structure.ErrorIndicator
import com.qxdzbc.p6.app.action.range.RangeIdDM
import com.qxdzbc.p6.app.action.range.range_to_clipboard.RangeToClipboardResponse
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.mockito.kotlin.mock
import test.TestSample
import kotlin.test.BeforeTest

class RangeToClipboardInternalApplierImpTest {

    lateinit var appStateMs: Ms<StateContainer>
    private val appState:StateContainer get() = appStateMs.value
    lateinit var applier: RangeToClipboardInternalApplierImp
    private val cursorState: CursorState
        get()= appState
        .getWbState(TestSample.wbk1)
        ?.getWsState("Sheet1")
        ?.cursorState!!
    lateinit var errorRouter: ErrorRouter
    lateinit var testSample: TestSample
    @BeforeTest
    fun b() {
        testSample = TestSample()
        appStateMs = testSample.scMs
        errorRouter = mock()
        applier = RangeToClipboardInternalApplierImp(
            stateContMs = appStateMs
        )
    }

    @Test
    fun `applyRes ok res`() {
        val res = RangeToClipboardResponse(
            errorIndicator = ErrorIndicator.noError,
            rangeId = RangeIdDM(
                rangeAddress = RangeAddress("O12:K23"),
                wsName = "Sheet1",
                wbKey = TestSample.wbk1
            ),
            windowId = null
        )
        assertNotEquals(res.rangeId.rangeAddress,cursorState.clipboardRange)
        applier.apply(res.rangeId,res.windowId)
        assertEquals(res.rangeId.rangeAddress,cursorState.clipboardRange)
    }
}
