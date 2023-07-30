package com.qxdzbc.p6.ui.window.tool_bar.text_size_selector.action

import com.qxdzbc.p6.ui.window.tool_bar.text_size_selector.state.TextSizeSelectorStateImp
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.Before
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import test.BaseAppStateTest
import kotlin.test.Test

internal class TextSizeSelectorActionImpTest : BaseAppStateTest() {

    lateinit var action: TextSizeSelectorActionImp
    lateinit var textSizeSelectorState: TextSizeSelectorStateImp
    val newTextSize = 333

    @Before
    fun b() {
        action = TextSizeSelectorActionImp(
            ts.sc,
            mock(),
            mock(),
        )
        textSizeSelectorState = TextSizeSelectorStateImp(headerText = newTextSize.toString())
    }


    @Test
    fun submitManualEdit() {
        ts.sc.getTextSizeSelectorState(ts.window1Id) shouldNotBe textSizeSelectorState

        action.submitManualEdit(ts.window1Id, textSizeSelectorState.headerText)

        ts.sc.getTextSizeSelectorState(ts.window1Id) shouldBe textSizeSelectorState
        verify(action.updateCellFormatAction,times(1)).setSelectedCellsTextSize(
            newTextSize.toFloat(),undoable=true
        )
        verify(action.returnFocusToCellCursor,times(1)).returnFocusToCurrentCellCursor()
    }

    @Test
    fun pickTextSize() {
        ts.sc.getTextSizeSelectorState(ts.window1Id) shouldNotBe textSizeSelectorState

        action.pickTextSize(ts.window1Id, newTextSize)

        ts.sc.getTextSizeSelectorState(ts.window1Id) shouldBe textSizeSelectorState
        verify(action.updateCellFormatAction,times(1)).setSelectedCellsTextSize(
            newTextSize.toFloat(),undoable=true
        )
        verify(action.returnFocusToCellCursor,times(1)).returnFocusToCurrentCellCursor()
    }

    @Test
    fun setHeaderTextOfTextSizeSelector() {
        val text= "text 123"
        val expect = TextSizeSelectorStateImp(text)
        ts.sc.getTextSizeSelectorState(ts.window1Id) shouldNotBe expect

        action.setHeaderTextOfTextSizeSelector(ts.window1Id,text)

        ts.sc.getTextSizeSelectorState(ts.window1Id) shouldBe expect
    }
}
