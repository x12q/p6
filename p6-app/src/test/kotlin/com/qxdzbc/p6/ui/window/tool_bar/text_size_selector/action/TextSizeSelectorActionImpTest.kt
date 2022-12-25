package com.qxdzbc.p6.ui.window.tool_bar.text_size_selector.action

import com.qxdzbc.p6.app.document.cell.CellId
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.ui.window.tool_bar.text_size_selector.state.TextSizeSelectorStateImp
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.Before
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import test.BaseTest
import kotlin.test.Test

internal class TextSizeSelectorActionImpTest : BaseTest() {

    lateinit var action: TextSizeSelectorActionImp
    lateinit var textSizeSelectorState: TextSizeSelectorStateImp
    val newTextSize = 333

    @Before
    fun b() {
        action = TextSizeSelectorActionImp(
            ts.stateContMs,
            mock(),
            mock(),
        )
        textSizeSelectorState = TextSizeSelectorStateImp(headerText = newTextSize.toString())
    }


    @Test
    fun submitManualEdit() {
        ts.stateCont.getTextSizeSelectorState(ts.window1Id) shouldNotBe textSizeSelectorState

        action.submitManualEdit(ts.window1Id, textSizeSelectorState.headerText)

        ts.stateCont.getTextSizeSelectorState(ts.window1Id) shouldBe textSizeSelectorState
        verify(action.updateCellFormatAction,times(1)).setCellTextSize(
            ts.stateCont.getActiveCursorState()!!.mainCellId,
            newTextSize.toFloat()
        )
        verify(action.returnFocusToCellCursor,times(1)).returnFocusToCurrentCellCursor()
    }

    @Test
    fun pickTextSize() {
        ts.stateCont.getTextSizeSelectorState(ts.window1Id) shouldNotBe textSizeSelectorState

        action.pickTextSize(ts.window1Id, newTextSize)

        ts.stateCont.getTextSizeSelectorState(ts.window1Id) shouldBe textSizeSelectorState
        verify(action.updateCellFormatAction,times(1)).setCellTextSize(
            ts.stateCont.getActiveCursorState()!!.mainCellId,
            newTextSize.toFloat()
        )
        verify(action.returnFocusToCellCursor,times(1)).returnFocusToCurrentCellCursor()
    }

    @Test
    fun setHeaderTextOfTextSizeSelector() {
        val text= "text 123"
        val expect = TextSizeSelectorStateImp(text)
        ts.stateCont.getTextSizeSelectorState(ts.window1Id) shouldNotBe expect

        action.setHeaderTextOfTextSizeSelector(ts.window1Id,text)

        ts.stateCont.getTextSizeSelectorState(ts.window1Id) shouldBe expect
    }
}
