package com.qxdzbc.p6.ui.document.worksheet.cell_editor.in_cell.actions

import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.Key
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.ui.app.cell_editor.actions.CellEditorAction
import test.BaseTest
import test.test_implementation.MockPKeyEvent
import kotlin.test.*

internal class CellEditorActionImpTest :BaseTest(){

    lateinit var actions: CellEditorAction
    lateinit var wbws:WbWsSt
    val editorState get() = ts.stateCont.cellEditorStateMs.value
    @BeforeTest
    override fun b() {
        super.b()
        actions = ts.p6Comp.cursorEditorAction()
        wbws = ts.wb1.getWs(ts.wsn1)!!
        actions.openCellEditor(wbws)
    }

    @AfterTest
    fun afterTes(){
        actions.closeEditor()
        ts.stateCont.cellEditorStateMs.value = editorState.clearAllText()
    }

    @Test
    fun testAutoCompleteParentheses(){
        val parenthesis = MockPKeyEvent(
            isParentheses = true
        )
        actions.handleKeyboardEvent(parenthesis)
        assertEquals("()",editorState.currentText)
        assertEquals("()",editorState.displayText)
        assertEquals("()",editorState.displayTextField.text)
//        println(">>>>" + editorState.displayTextField.toString())
        val selectionRange = with(editorState.currentTextField.selection){
            start .. end
        }
        assertEquals(1 .. 1,selectionRange)
    }

    @Test
    fun testAutoCompleteBracket(){
        val parenthesis = MockPKeyEvent(
            isBracket = true
        )
        actions.handleKeyboardEvent(parenthesis)
        assertEquals("[]",editorState.currentText)
        assertEquals("[]",editorState.displayText)
        val selectionRange = with(editorState.currentTextField.selection){
            start .. end
        }
        assertEquals(1 .. 1,selectionRange)
    }

    @Test
    fun testAutoCompleteCurlyBracket(){
        val parenthesis = MockPKeyEvent(
            isCurlyBracket = true
        )
        actions.handleKeyboardEvent(parenthesis)
        assertEquals("{}",editorState.currentText)
        assertEquals("{}",editorState.displayText)
        val selectionRange = with(editorState.currentTextField.selection){
            start .. end
        }
        assertEquals(1 .. 1,selectionRange)
    }
}
