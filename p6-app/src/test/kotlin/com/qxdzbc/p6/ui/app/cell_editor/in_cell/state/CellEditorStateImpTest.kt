package com.qxdzbc.p6.ui.app.cell_editor.in_cell.state

import androidx.compose.ui.text.input.TextFieldValue
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.p6.ui.app.cell_editor.RangeSelectorAllowState
import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorStateImp
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorId
import org.mockito.Mockito.mock
import kotlin.test.*

class CellEditorStateImpTest {
    lateinit var stdState: CellEditorStateImp

    @BeforeTest
    fun b() {
        stdState = CellEditorStateImp.defaultForTest()
    }

    @Test
    fun `rangeSelectorAllowState when open-close editor`() {
        val cursorIdMs:St<CursorId> = ms(mock(CursorId::class.java))
        val o1=stdState.setCurrentText("").open(cursorIdMs)
        assertEquals(cursorIdMs,o1.targetCursorIdSt)
        assertEquals(RangeSelectorAllowState.START,o1.rangeSelectorAllowState)
        val o1Closed = o1.close()
        assertEquals(RangeSelectorAllowState.NOT_AVAILABLE,o1Closed.rangeSelectorAllowState)

        val o2=stdState.setCurrentText("qqq").open(cursorIdMs)
        assertEquals(RangeSelectorAllowState.DISALLOW,o2.rangeSelectorAllowState)
        assertEquals(RangeSelectorAllowState.NOT_AVAILABLE,o2.close().rangeSelectorAllowState)
    }

    @Test
    fun allowRangeSelector() {
        val state = CellEditorStateImp.defaultForTest()
        val mockCursorStatId=mock(CursorId::class.java).toMs()
        val openedState = state.open(mockCursorStatId)
        assertTrue(openedState.setCurrentText("=1+").allowRangeSelector)
        assertFalse(openedState.setCurrentText("=1").allowRangeSelector)
    }

    @Test
    fun stopGettingRange() {
        val mockCursorStatId=mock(CursorId::class.java).toMs()
        val state = CellEditorStateImp.defaultForTest().open(mockCursorStatId)
        val s2 = state.setCurrentText("=").setRangeSelectorTextField(TextFieldValue("=A1"))
        assertTrue(s2.allowRangeSelector)
        assertEquals("=", s2.currentText)
        assertEquals("=A1", s2.rangeSelectorTextField?.text)

        val s3 = s2.stopGettingRangeAddress()
        assertFalse(s3.allowRangeSelector)
        assertEquals("=A1", s3.currentText)
        assertNull(s3.rangeSelectorTextField)
    }
}
