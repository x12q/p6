package com.emeraldblast.p6.ui.document.worksheet.slider

import com.emeraldblast.p6.app.document.cell.address.CellAddresses
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.ui.common.compose.ms
import com.emeraldblast.p6.ui.document.workbook.state.WorkbookStateID
import com.emeraldblast.p6.ui.document.worksheet.cursor.state.CursorStateImp
import com.emeraldblast.p6.ui.document.worksheet.state.WorksheetStateId
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
    val workbookStateID = ms(mock<WorkbookStateID>(){
        whenever(it.wbKey) doReturn WorkbookKey("")
    })
    val worksheetStateID = ms(mock<WorksheetStateId>(){
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
    fun move() {
        // cursor on left-most col
        val cursor = CursorStateImp.default2(worksheetStateID).setAnchorCell(
            CellAddresses.fromIndices(gridSlider.firstVisibleCol - 1, 2)
        )
        val m1 = gridSlider.move(cursor)
        assertNotNull(m1)
        assertEquals(gridSlider.firstVisibleCol - 1, m1.firstVisibleCol)
        assertEquals(gridSlider.lastVisibleCol - 1, m1.lastVisibleCol)
        assertEquals(gridSlider.visibleRowRange, m1.visibleRowRange)

        // cursor on right-most col
        val cursor2 = CursorStateImp.default2(worksheetStateID).setAnchorCell(
            CellAddresses.fromIndices(gridSlider.lastVisibleCol + 1, 2)
        )
        val m2 = gridSlider.move(cursor2)
        assertNotNull(m2)
        assertEquals(gridSlider.firstVisibleCol + 1, m2.firstVisibleCol)
        assertEquals(gridSlider.lastVisibleCol + 1, m2.lastVisibleCol)
        assertEquals(gridSlider.visibleRowRange, m2.visibleRowRange)

        // cursor on bot row
        val cursor3 = CursorStateImp.default2(worksheetStateID).setAnchorCell(
            CellAddresses.fromIndices(5, gridSlider.lastVisibleRow + 1)
        )
        val m3 = gridSlider.move(cursor3)
        assertNotNull(m3)
        assertEquals(gridSlider.lastVisibleRow + 1, m3.lastVisibleRow)
        assertEquals(gridSlider.firstVisibleRow + 1, m3.firstVisibleRow)
        assertEquals(gridSlider.visibleColRange, m3.visibleColRange)

        // cursor on top row
        val cursor4 = CursorStateImp.default2(worksheetStateID).setAnchorCell(
            CellAddresses.fromIndices(5, gridSlider.firstVisibleRow - 1)
        )
        val m4 = gridSlider.move(cursor4)
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
