package com.qxdzbc.p6.app.action.worksheet.make_cell_editor_display_text

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorState
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.common.formatter.RangeAddressFormatter
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import test.TestSample
import kotlin.test.*

class MakeCellEditorTextActionImpTest {

    lateinit var ts: TestSample
    lateinit var action: MakeCellEditorTextActionImp
    val currentText=  "currentText+"
    val currentTextField = TextFieldValue(currentText,TextRange(currentText.length))
    lateinit var rangeSelectorCursorMs:Ms<CursorState>
    lateinit var editorState: CellEditorState
    lateinit var fm: RangeAddressFormatter

    @BeforeTest
    fun b() {
        ts = TestSample()
        fm = ts.comp.rangeFormatter()
        action = MakeCellEditorTextActionImp(ts.appStateMs,fm)
        rangeSelectorCursorMs = ts.appState.getCursorStateMs(ts.wbKey1, ts.wsn2)!!
        editorState = mock<CellEditorState> {
            whenever(it.allowRangeSelector) doReturn true
            whenever(it.currentText) doReturn currentTextField.text
            whenever(it.currentTextField) doReturn currentTextField
            whenever(it.rangeSelectorCursorId) doReturn rangeSelectorCursorMs.value.id
        }
    }

    @Test
    fun `create text when cursor is in the middle of the text`() {
        val  o = action.makeRangeSelectorText(
            TextFieldValue("=(123+)", TextRange(6)),
            editorState.rangeSelectorCursorId,
            editorState.targetCursorId
        )
        val rs by rangeSelectorCursorMs
        val address = fm.format(rs.mainCell,rs.wsName,rs.wbKey)
        // =(123+A1@Sheet2@Book1|)
        val expect= TextFieldValue("=(123+${address})",TextRange(address.length+6))
        assertEquals(expect,o)
    }

    @Test
    fun `create text after brace completion`() {
        val  o = action.makeRangeSelectorText(
            TextFieldValue("=()", TextRange(2)),
            editorState.rangeSelectorCursorId,
            editorState.targetCursorId
        )
        val rs by rangeSelectorCursorMs
        val address = fm.format(rs.mainCell,rs.wsName,rs.wbKey)
        // =(A1@Sheet2@Book1|)
        val expect= TextFieldValue("=(${address})",TextRange(address.length+2))
        assertEquals(expect,o)
    }

    @Test
    fun `makeDisplayText when range selector is activating on a single cell, different cursor in different sheet`() {
        val rs by rangeSelectorCursorMs
        whenever(editorState.targetCursorId) doReturn ts.appState.getCursorStateMs(ts.wbKey1, ts.wsn1)!!.value.id
        val outTextField=action.makeRangeSelectorText(editorState)
        val expectText = currentText+fm.format(rs.mainCell,rs.wsName)
        assertEquals(expectText,outTextField.text)
        println(outTextField.text)
    }

    @Test
    fun `makeDisplayText when range selector is activating on a range, diff wb, diff ws`() {
        var rs by rangeSelectorCursorMs
        whenever(editorState.targetCursorId) doReturn ts.appState.getCursorStateMs(ts.wbKey3, ts.wsn1)!!.value.id
        val range = RangeAddress("C8:E32")
        rs = rs.setMainRange(range)
        val outTextField=action.makeRangeSelectorText(editorState)
        val expectText = currentText+fm.format(rs.mainRange!!,rs.wsName,rs.wbKey)
        assertEquals(expectText,outTextField.text)
        println(outTextField.text)
    }
}
