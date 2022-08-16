package com.emeraldblast.p6.ui.app.cell_editor.in_cell.state

import androidx.compose.ui.text.input.TextFieldValue
import kotlin.test.*

class CellEditorStateImpTest {

    @Test
    fun allowRangeSelector() {
        val state = CellEditorStateImp.defaultForTest()
        assertTrue(state.setCurrentText("=1+").allowRangeSelector)
        assertFalse(state.setCurrentText("=1").allowRangeSelector)
    }

    @Test
    fun stopGettingRange(){
        val state = CellEditorStateImp.defaultForTest()
        val s2 = state.setCurrentText("=").setRangeSelectorText(TextFieldValue("=A1"))
        assertTrue(s2.allowRangeSelector)
        assertEquals("=",s2.currentText)
        assertEquals("=A1",s2.rangeSelectorText?.text)

        val s3 =s2.stopGettingRangeAddress()
        assertFalse (s3.allowRangeSelector)
        assertEquals("=A1",s3.currentText)
        assertNull(s3.rangeSelectorText)


    }
}
