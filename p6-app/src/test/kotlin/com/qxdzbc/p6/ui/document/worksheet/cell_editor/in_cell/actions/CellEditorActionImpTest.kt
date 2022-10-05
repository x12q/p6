package com.qxdzbc.p6.ui.document.worksheet.cell_editor.in_cell.actions

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.ui.app.cell_editor.actions.CellEditorAction
import test.BaseTest
import test.test_implementation.MockPKeyEvent
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class CellEditorActionImpTest : BaseTest() {

    lateinit var actions: CellEditorAction
    lateinit var wbws: WbWsSt
    val editorStateMs get() = ts.stateCont.cellEditorStateMs
    val editorState get() = editorStateMs.value

    @BeforeTest
    override fun b() {
        super.b()
        actions = ts.p6Comp.cursorEditorAction()
        wbws = ts.wb1.getWs(ts.wsn1)!!
        actions.openCellEditor(wbws)
    }

    @AfterTest
    fun afterTes() {
        actions.closeEditor()
        ts.stateCont.cellEditorStateMs.value = editorState.clearAllText()
    }

    fun testBrace(b1:String, b2:String) {
        val pair = "${b1}${b2}"
        actions.updateTextField(TextFieldValue(b1, TextRange(1)))
        assertEquals(pair, editorState.currentText)
        assertEquals(pair, editorState.displayText)
        assertEquals(pair, editorState.displayTextField.text)
        assertEquals(TextRange(1), editorState.currentTextField.selection)

        editorStateMs.value = editorState.clearAllText()
        actions.updateTextField(TextFieldValue("abc",TextRange(3)))
        actions.updateTextField(TextFieldValue("abc${b1}",TextRange(4)))
        assertEquals("abc${pair}", editorState.currentText)
        assertEquals("abc${pair}", editorState.displayText)
        assertEquals("abc${pair}", editorState.displayTextField.text)
        assertEquals(TextRange(4), editorState.currentTextField.selection)


        editorStateMs.value = editorState.clearAllText()
        actions.updateTextField(TextFieldValue("abc12345",TextRange(8)))
        actions.updateTextField(TextFieldValue("abc1${b1}45",TextRange(5)))
        assertEquals("abc1${pair}45", editorState.currentText)
        assertEquals("abc1${pair}45", editorState.displayText)
        assertEquals("abc1${pair}45", editorState.displayTextField.text)
        assertEquals(TextRange(5), editorState.currentTextField.selection)
    }

    @Test
    fun testAutoCompleteParentheses() {
        testBrace("(",")")
        testBrace("[","]")
        testBrace("{","}")
    }
}
