package com.qxdzbc.p6.ui.document.worksheet.slider

import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.address.CellAddresses
import com.qxdzbc.p6.ui.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorStateImp
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetId
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


internal class GridSliderImpTest {

    val gridSlider = GridSliderImp(
        visibleColRange = IntRange(5, 10),
        visibleRowRange = IntRange(3, 20),
    )

    val wsId = ms(mock<WorksheetId>() {
        whenever(it.wsName) doReturn ""
    })


    @Test
    fun constructorTest() {
        assertEquals(5, gridSlider.firstVisibleCol)
        assertEquals(10, gridSlider.lastVisibleCol)
        assertEquals(3, gridSlider.firstVisibleRow)
        assertEquals(20, gridSlider.lastVisibleRow)
    }

    @Test
    fun `followCursor with margin`() {
        val gridSlider = GridSliderImp(
            visibleColRange = IntRange(5, 10),
            visibleRowRange = IntRange(3, 20),
            marginRow = 20,
            marginCol = 10,
        )
        // x: cursor on left-most col
        val cursor = CursorStateImp.default2(wsId)
            .setMainCell(CellAddress(gridSlider.firstVisibleCol - 1, 2))
        val m1 = gridSlider.followCursor(cursor)
        assertNotNull(m1)
        assertEquals(gridSlider.firstVisibleCol - 1, m1.firstVisibleCol)
        assertEquals(gridSlider.lastVisibleCol - 1, m1.lastVisibleCol)
        assertEquals(gridSlider.visibleRowRange, m1.visibleRowRange)

        // x: cursor on the margin col
        val cursor2 = CursorStateImp.default2(wsId).setMainCell(
            CellAddress(gridSlider.lastVisibleCol, 2)
        )
        val m2 = gridSlider.followCursor(cursor2)
        assertNotNull(m2)
        assertEquals(gridSlider.firstVisibleCol + 1, m2.firstVisibleCol)
        assertEquals(gridSlider.lastVisibleCol + 1, m2.lastVisibleCol)
        assertEquals(gridSlider.visibleRowRange, m2.visibleRowRange)

        // x: cursor on margin row
        val cursor3 = CursorStateImp.default2(wsId).setMainCell(
            CellAddress(5, gridSlider.lastVisibleRow)
        )
        val m3 = gridSlider.followCursor(cursor3)
        assertNotNull(m3)
        assertEquals(gridSlider.lastVisibleRow + 1, m3.lastVisibleRow)
        assertEquals(gridSlider.firstVisibleRow + 1, m3.firstVisibleRow)
        assertEquals(gridSlider.visibleColRange, m3.visibleColRange)

        // x: cursor on top row
        val cursor4 = CursorStateImp.default2(wsId).setMainCell(
            CellAddress(5, gridSlider.firstVisibleRow - 1)
        )
        val m4 = gridSlider.followCursor(cursor4)
        assertNotNull(m4)
        assertEquals(gridSlider.lastVisibleRow - 1, m4.lastVisibleRow)
        assertEquals(gridSlider.firstVisibleRow - 1, m4.firstVisibleRow)
        assertEquals(gridSlider.visibleColRange, m4.visibleColRange)
    }


    @Test
    fun `followCursor no margin`() {
        // cursor on left-most col
        val cursor = CursorStateImp.default2(wsId).setMainCell(
            CellAddress(gridSlider.firstVisibleCol - 1, 2)
        )
        val m1 = gridSlider.followCursor(cursor)
        assertNotNull(m1)
        assertEquals(gridSlider.firstVisibleCol - 1, m1.firstVisibleCol)
        assertEquals(gridSlider.lastVisibleCol - 1, m1.lastVisibleCol)
        assertEquals(gridSlider.visibleRowRange, m1.visibleRowRange)

        // cursor on right-most col
        val cursor2 = CursorStateImp.default2(wsId).setMainCell(
            CellAddress(gridSlider.lastVisibleCol + 1, 2)
        )
        val m2 = gridSlider.followCursor(cursor2)
        assertNotNull(m2)
        assertEquals(gridSlider.firstVisibleCol + 1, m2.firstVisibleCol)
        assertEquals(gridSlider.lastVisibleCol + 1, m2.lastVisibleCol)
        assertEquals(gridSlider.visibleRowRange, m2.visibleRowRange)

        // cursor on bot row
        val cursor3 = CursorStateImp.default2(wsId).setMainCell(
            CellAddress(5, gridSlider.lastVisibleRow + 1)
        )
        val m3 = gridSlider.followCursor(cursor3)
        assertNotNull(m3)
        assertEquals(gridSlider.lastVisibleRow + 1, m3.lastVisibleRow)
        assertEquals(gridSlider.firstVisibleRow + 1, m3.firstVisibleRow)
        assertEquals(gridSlider.visibleColRange, m3.visibleColRange)

        // cursor on top row
        val cursor4 = CursorStateImp.default2(wsId).setMainCell(
            CellAddress(5, gridSlider.firstVisibleRow - 1)
        )
        val m4 = gridSlider.followCursor(cursor4)
        assertNotNull(m4)
        assertEquals(gridSlider.lastVisibleRow - 1, m4.lastVisibleRow)
        assertEquals(gridSlider.firstVisibleRow - 1, m4.firstVisibleRow)
        assertEquals(gridSlider.visibleColRange, m4.visibleColRange)
    }

    @Test
    fun shiftLeft() {
        val s1 = gridSlider.shiftLeft(2)
        assertEquals(gridSlider.firstVisibleCol - 2, s1.firstVisibleCol)
        assertEquals(gridSlider.lastVisibleCol - 2, s1.lastVisibleCol)
        assertEquals(gridSlider.visibleRowRange, s1.visibleRowRange)
    }

    @Test
    fun shiftRight() {
        val s1 = gridSlider.shiftRight(2)
        assertEquals(gridSlider.firstVisibleCol + 2, s1.firstVisibleCol)
        assertEquals(gridSlider.lastVisibleCol + 2, s1.lastVisibleCol)
        assertEquals(gridSlider.visibleRowRange, s1.visibleRowRange)
    }

    @Test
    fun shiftUp() {
        val s1 = gridSlider.shiftUp(2)
        assertEquals(gridSlider.firstVisibleRow - 2, s1.firstVisibleRow)
        assertEquals(gridSlider.lastVisibleRow - 2, s1.lastVisibleRow)
        assertEquals(gridSlider.visibleColRange, s1.visibleColRange)
    }

    @Test
    fun shiftDown() {
        val s1 = gridSlider.shiftDown(2)
        assertEquals(gridSlider.firstVisibleRow + 2, s1.firstVisibleRow)
        assertEquals(gridSlider.lastVisibleRow + 2, s1.lastVisibleRow)
        assertEquals(gridSlider.visibleColRange, s1.visibleColRange)
    }
}
