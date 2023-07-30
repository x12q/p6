package com.qxdzbc.p6.ui.document.worksheet.cell_editor.in_cell.actions

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.qxdzbc.common.P6ExperimentalApi
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.ui.app.cell_editor.actions.CellEditorAction
import test.BaseAppStateTest
import kotlin.test.*

internal class CellEditorActionImpTest : BaseAppStateTest() {

    lateinit var act: CellEditorAction
    lateinit var wbws: WbWsSt
    val editorStateMs get() = ts.sc.cellEditorStateMs
    val editorState get() = editorStateMs.value

    @BeforeTest
    override fun _b() {
        super._b()
        act = ts.comp.cursorEditorAction()
        wbws = ts.wb1.getWs(ts.wsn1)!!
        act.openCellEditor(wbws)
    }

    @AfterTest
    fun afterTes() {
        act.closeEditor()
        ts.sc.cellEditorStateMs.value = editorState.clearAllText()
    }

    fun testBrace(b1:String, b2:String) {
        val pair = "${b1}${b2}"
        act.changeTextField(TextFieldValue(b1, TextRange(1)))
        assertEquals(pair, editorState.currentText)
        assertEquals(pair, editorState.displayText)
        assertEquals(pair, editorState.displayTextField.text)
        assertEquals(TextRange(1), editorState.currentTextField.selection)

        editorStateMs.value = editorState.clearAllText()
        act.changeTextField(TextFieldValue("abc",TextRange(3)))
        act.changeTextField(TextFieldValue("abc${b1}",TextRange(4)))
//        assertEquals("abc${pair}", editorState.currentText)
        assertEquals("abc${pair}", editorState.displayText)
        assertEquals("abc${pair}", editorState.displayTextField.text)
        assertEquals(TextRange(4), editorState.currentTextField.selection)

        editorStateMs.value = editorState.clearAllText()
        act.changeTextField(TextFieldValue("abc12345",TextRange(8)))
        act.changeTextField(TextFieldValue("abc1${b1}45",TextRange(5)))
        assertEquals("abc1${pair}45", editorState.currentText)
        assertEquals("abc1${pair}45", editorState.displayText)
        assertEquals("abc1${pair}45", editorState.displayTextField.text)
        assertEquals(TextRange(5), editorState.currentTextField.selection)
    }

    @Test
    fun testAutoCompleteParentheses() {
        testBrace("(",")")
//        testBrace("[","]")
//        testBrace("{","}")
    }

    @OptIn(P6ExperimentalApi::class)
    @Test
    fun updateTextField(){
        assertNull(sc.cellEditorState.currentParseTree)
        act.changeRawText("")
        assertNull(sc.cellEditorState.currentParseTree)
        act.changeRawText("=")
        val pt1 = sc.cellEditorState.currentParseTree
        assertNotNull(pt1)
        act.changeRawText("=F1(A1)")
        val pt2 = sc.cellEditorState.currentParseTree
        assertNotNull(pt2)
        assertNotEquals(pt1,pt2)
    }

    @OptIn(P6ExperimentalApi::class)
    @Test
    fun closeEditor(){
        // x: pre conditions
        act.changeRawText("=F1(A1)")
        assertNotNull(sc.cellEditorState.currentParseTree)
        assertTrue(sc.cellEditorState.currentText.isNotEmpty())
        assertTrue(sc.cellEditorState.displayText.isNotEmpty())
        assertTrue(sc.cellEditorState.currentTextField.text.isNotEmpty())
        // x: act
        act.closeEditor()
        // x: post conditions
        assertNull(sc.cellEditorState.currentParseTree)
        assertTrue(sc.cellEditorState.currentText.isEmpty())
        assertTrue(sc.cellEditorState.displayText.isEmpty())
        assertTrue(sc.cellEditorState.currentTextField.text.isEmpty())
    }

    @Test
    fun openEditor(){
        // x: pre conditions
        act.closeEditor()
        assertNull(sc.cellEditorState.currentParseTree)
        assertTrue(sc.cellEditorState.currentText.isEmpty())
        assertTrue(sc.cellEditorState.displayText.isEmpty())
        assertTrue(sc.cellEditorState.currentTextField.text.isEmpty())
        // x: act
        act.openCellEditor(wbws)
        // x: post conditions

    }
}
