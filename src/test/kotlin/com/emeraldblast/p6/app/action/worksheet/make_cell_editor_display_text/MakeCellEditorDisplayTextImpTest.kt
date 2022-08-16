package com.emeraldblast.p6.app.action.worksheet.make_cell_editor_display_text

import androidx.compose.ui.text.input.TextFieldValue
import com.emeraldblast.p6.app.document.range.address.RangeAddress
import com.emeraldblast.p6.ui.app.cell_editor.in_cell.state.CellEditorState
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.document.worksheet.cursor.state.CursorState
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import test.TestSample
import kotlin.test.*

class MakeCellEditorDisplayTextImpTest {

    lateinit var ts: TestSample
    lateinit var action: MakeCellEditorDisplayTextImp
    val currentText=  "currentText+"
    val currentTextField = TextFieldValue(currentText)
    lateinit var rangeSelectorCursorMs:Ms<CursorState>
    lateinit var editorState:CellEditorState

    @BeforeTest
    fun b() {
        ts = TestSample()
        action = MakeCellEditorDisplayTextImp(ts.appStateMs)
        rangeSelectorCursorMs = ts.appState.getCursorStateMs(ts.wbKey1, ts.wsn2)!!
        editorState = mock<CellEditorState> {
            whenever(it.allowRangeSelector) doReturn true
            whenever(it.currentText) doReturn currentTextField.text
            whenever(it.currentTextField) doReturn currentTextField
            whenever(it.rangeSelectorCursorId) doReturn rangeSelectorCursorMs.value.id
        }
    }

    @Test
    fun `makeDisplayText when range selector is activating on a single cell, same cursor`() {
        whenever(editorState.targetCursorId) doReturn rangeSelectorCursorMs.value.id
        val outTextField=action.makeRangeSelectorText(editorState)
        val expectText = currentText+ rangeSelectorCursorMs.value.mainCell.toRawLabel()
        assertEquals(expectText,outTextField.text)
        println(outTextField.text)
    }

    @Test
    fun `makeDisplayText when range selector is activating on a single cell, different cursor`() {
        whenever(editorState.targetCursorId) doReturn ts.appState.getCursorStateMs(ts.wbKey1, ts.wsn1)!!.value.id
        val outTextField=action.makeRangeSelectorText(editorState)
        val expectText = currentText+"${rangeSelectorCursorMs.value.id.wsName}!${rangeSelectorCursorMs.value.mainCell.toRawLabel()}"
        assertEquals(expectText,outTextField.text)
        println(outTextField.text)
    }

    @Test
    fun `makeDisplayText when range selector is activating on a range`() {
        val range = RangeAddress("C8:E32")
        rangeSelectorCursorMs.value = rangeSelectorCursorMs.value.setMainRange(range)
        val outTextField=action.makeRangeSelectorText(editorState)
        val expectText = currentText+"${rangeSelectorCursorMs.value.id.wsName}!${rangeSelectorCursorMs.value.mainRange!!.rawLabel}"
        assertEquals(expectText,outTextField.text)
        println(outTextField.text)
    }
}
