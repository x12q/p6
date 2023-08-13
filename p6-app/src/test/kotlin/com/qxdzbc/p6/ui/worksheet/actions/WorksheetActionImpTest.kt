package com.qxdzbc.p6.ui.worksheet.actions

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.IntRangeUtils.add
import com.qxdzbc.common.IntRangeUtils.dif
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddresses
import com.qxdzbc.p6.document_data_layer.workbook.Workbook
import com.qxdzbc.p6.document_data_layer.worksheet.Worksheet
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper
import com.qxdzbc.p6.composite_actions.worksheet.WorksheetAction
import com.qxdzbc.p6.ui.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.worksheet.cursor.state.CursorStateImp
import com.qxdzbc.p6.ui.worksheet.slider.GridSlider
import com.qxdzbc.p6.ui.worksheet.slider.UnlimitedGridSlider
import com.qxdzbc.p6.ui.worksheet.state.WorksheetState
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import test.TestSample
import kotlin.test.*

//import io.mockk.every
//import io.mockk.mockk

//TODO these tests are still valid, but disabled until better implementation is completed
internal class WorksheetActionImpTest {
    lateinit var ws: Worksheet
    lateinit var wsState: WorksheetState
    lateinit var actions: WorksheetAction
    val cursorStateMs get() = this.wsState.cursorStateMs
    val cursorState: CursorState get() = this.wsState.cursorStateMs.value
    val slider: GridSlider get() = this.wsState.slider
    val mockOffset = Offset(0F, 0F)
    lateinit var layoutMap: MutableMap<CellAddress, LayoutCoorWrapper>
    lateinit var posMap: MutableMap<CellAddress, Rect>
    lateinit var wb: Workbook

    @BeforeTest
    fun before() {
        val testSample = TestSample()
        wb = testSample.wbCont.getWb(testSample.wbKey1)!!
        ws = wb.worksheets[0]
        this.wsState = testSample.sc.getWsState(ws)!!

        layoutMap = mutableMapOf()
        // x: create fake cell positions as Rect
        posMap = mutableMapOf<CellAddress, Rect>().also {
            for (r in 1..20) {
                for (c in 1..20) {
                    val rect = Rect(
                        topLeft = Offset(
                            (c - 1) * this.wsState.defaultColWidth.value,
                            (r - 1) * this.wsState.defaultRowHeight.value,
                        ),
                        bottomRight = Offset(
                            c * this.wsState.defaultColWidth.value,
                            r * this.wsState.defaultRowHeight.value
                        )
                    )
                    val cellAddress = CellAddress(c, r)
                    val mockLayout: LayoutCoorWrapper = mock {
                        whenever(it.boundInWindowOrZero).thenReturn(rect)
                        whenever(it.refreshVar) doReturn false
                    }
                    layoutMap[cellAddress] = mockLayout

                    it[cellAddress] = rect
                }
            }
        }
        actions = testSample.comp.worksheetAction()
        for ((c, l) in layoutMap) {
            this.wsState.addCellLayoutCoor(c, l)
        }
    }

    @Test
    fun determineSliderSize() {
        val o = UnlimitedGridSlider(
            visibleColRange = 1..5,
            visibleRowRange = 1..5,
        )
        val n: GridSlider = actions.computeSliderSize(
            o, DpSize(width = 100.dp, height = 200.dp),
            anchorCell = CellAddress(3, 2),
            colWidthGetter = { 30.dp },
            rowHeightGetter = { 20.dp }
        )
        assertEquals(3 .. 6, n.visibleColRange)
        assertEquals(2 .. 11, n.visibleRowRange)
        assertNull(n.marginRow)
        assertEquals(6,n.marginCol)

        val n2: GridSlider = actions.computeSliderSize(
            o, DpSize(width = 100.dp, height = 200.dp),
            anchorCell = CellAddress(3, 2),
            colWidthGetter = { 30.dp },
            rowHeightGetter = { 30.dp }
        )
        assertEquals(3 .. 6, n2.visibleColRange)
        assertEquals(2 .. 8, n2.visibleRowRange)
        assertEquals(8,n2.marginRow)
        assertEquals(6,n2.marginCol)
    }

    @Test
    fun updateSlider() {
        val d = 3
        val oldSlider = slider
        val newCursor = CursorStateImp.forTest(mock(),mock(), mock()).setMainCell(
            CellAddresses.fromIndices(
                colIndex = slider.visibleColRange.last + d,
                rowIndex = slider.visibleRowRange.random()
            )
        )
        actions.makeSliderFollowCursorMainCell(newCursor, ws)
        println(this.wsState.slider)

        assertEquals(oldSlider.visibleColRange.add(d), slider.visibleColRange)
        assertEquals(oldSlider.firstVisibleCol + d, slider.firstVisibleCol)
        assertEquals(oldSlider.lastVisibleCol + d, slider.lastVisibleCol)

        val cursor2 = CursorStateImp.forTest(mock(),mock(),mock()).setMainCell(
            CellAddresses.fromIndices(
                colIndex = slider.visibleColRange.random(),
                rowIndex = slider.visibleRowRange.add(d).last
            )
        )

        val oldSlider2 = slider
        actions.makeSliderFollowCursorMainCell(cursor2, ws)

        assertEquals(oldSlider2.visibleRowRange.add(d), slider.visibleRowRange)
        assertEquals(oldSlider2.firstVisibleRow + d, slider.firstVisibleRow)
        assertEquals(oldSlider2.lastVisibleRow + d, slider.lastVisibleRow)
    }

    @Test
    fun scroll() {
        val oldSlider = slider
        actions.addCellLayoutCoor(CellAddresses.fromIndices(1, 2), mock(), ws)
        assertFalse { this.wsState.cellLayoutCoorMap.isEmpty() }
        val (x1, y1) = Pair(3, 7)
        actions.scroll(x1, y1, ws)
        assertEquals(oldSlider.visibleRowRange.add(y1), slider.visibleRowRange)
        assertEquals(oldSlider.visibleColRange.add(x1), slider.visibleColRange)

        val (x2, y2) = Pair(-2, -4)
        val oldSlider2 = slider
        actions.scroll(x2, y2, ws)
        assertEquals(oldSlider2.visibleRowRange.add(y2), slider.visibleRowRange)
        assertEquals(oldSlider2.visibleColRange.add(x2), slider.visibleColRange)

        val (x3, y3) = Pair(-1000, -2000)
        actions.scroll(x3, y3, ws)
        assertEquals(oldSlider.visibleRowRange, slider.visibleRowRange)
        assertEquals(oldSlider.visibleColRange, slider.visibleColRange)

        val (x4, y4) = Pair(Int.MAX_VALUE, Int.MAX_VALUE)
        actions.scroll(x4, y4, ws)
        assertEquals(this.wsState.colRange.last, slider.lastVisibleCol)
        assertEquals(this.wsState.colRange.last - oldSlider.visibleColRange.dif(), slider.firstVisibleCol)
        assertEquals(this.wsState.rowRange.last, slider.lastVisibleRow)
        assertEquals(this.wsState.rowRange.last - oldSlider.visibleRowRange.dif(), slider.firstVisibleRow)
    }
//
//    @Test
//    fun clickOnCell() {
//        val cellAddress = CellAddresses.fromIndices(1, 23)
//        assertNotEquals(cellAddress, cursorState.mainCell)
//        wsState.cursorStateMs.value =
//            cursorState.setMainRange(RangeAddresses.wholeCol(2)).addFragCell(CellAddresses.firstOfCol(111))
//        wsActions.clickOnCell(cellAddress, wsState.cursorStateMs)
//        assertEquals(cellAddress, cursorState.mainCell)
//        assertNull(cursorState.mainRange)
//        assertTrue(cursorState.fragmentedCells.isEmpty())
//    }
//
//    @Test
//    fun closeFormulaEditor() {
//        wsState.cursorStateMs.value = cursorState.startEditCell()
//        assertTrue { cursorState.isEditing }
//        wsActions.closeFormulaEditor(wsState.cursorStateMs)
//        assertFalse { cursorState.isEditing }
//    }
//
//    @Test
//    fun startDragSelection() {
//        val expectedCell = CellAddress(3, 2)
//        val mousePoint = expectedCell.toPoint()
//        wsActions.startDragSelection(
//            mousePosition = mousePoint,
//            offset = mockOffset
//        )
//        assertEquals(expectedCell, cursorState.mainCell)
//    }
//
//    //
//    private fun CellAddress.toPoint(): Offset {
//        return Offset(
//            ((this.colIndex - 1) * wsState.defaultColWidth) + wsState.defaultColWidth / 2.toFloat(),
//            ((this.rowIndex - 1) * wsState.defaultRowHeight) + wsState.defaultRowHeight / 2.toFloat(),
//        )
//    }
//
//    //
//    @Test
//    fun makeMouseDragSelectionIfPossible() {
//        val mainCell = CellAddress(3, 2)
//        wsActions.startDragSelection(
//            mousePosition = mainCell.toPoint(),
//            offset = mockOffset
//        )
//
//        val theOtherCell = CellAddress(7, 8)
//        wsActions.makeMouseDragSelectionIfPossible(
//            mousePosition = theOtherCell.toPoint(),
//            offset = mockOffset
//        )
//        assertEquals(mainCell, cursorState.mainCell)
//        assertNotNull(cursorState.mainRange)
//        assertEquals(
//            RangeAddresses.from2Cells(
//                mainCell, theOtherCell
//            ), cursorState.mainRange
//        )
//    }
//
//    @Test
//    fun stopDragSelection() {
//        val mainCell = CellAddress(3, 2)
//        val endCell = CellAddress(7, 8)
//        wsActions.startDragSelection(mainCell.toPoint())
//        wsActions.makeMouseDragSelectionIfPossible(endCell.toPoint())
//        wsActions.stopDragSelection()
//        assertEquals(mainCell, cursorState.mainCell)
//        assertNotNull(cursorState.mainRange)
//        assertEquals(
//            RangeAddresses.from2Cells(mainCell, endCell),
//            cursorState.mainRange
//        )
//    }
//
//    @Test
//    fun addCellLayoutCoor() {
//        val c = CellAddress(999, 999)
//        assertNull(wsState.cellLayoutCoorMap[c])
//        wsActions.addCellLayoutCoor(CellAddress(999, 999), mock())
//        assertNotNull(wsState.cellLayoutCoorMap[c])
//    }
//
//    @Test
//    fun removeCellLayoutCoor() {
//        val c = CellAddress(999, 999)
//        wsState.value = wsState.addCellLayoutCoor(CellAddress(999, 999), mock())
//        assertNotNull(wsState.cellLayoutCoorMap[c])
//        wsActions.removeCellLayoutCoor(c)
//        assertNull(wsState.cellLayoutCoorMap[c])
//    }
//
//
//    @Test
//    fun ctrlClickSelectCell() {
//        val mainCell = cursorState.mainCell
//        val c1 = mainCell.increaseColBy(2).increaseRowBy(3)
//
//        assertTrue { c1 !in cursorState.fragmentedCells }
//        wsActions.ctrlClickSelectCell(c1)
//        assertEquals(mainCell, cursorState.mainCell)
//        assertTrue { c1 in cursorState.fragmentedCells }
//
//        wsActions.ctrlClickSelectCell(c1)
//        assertTrue { c1 !in cursorState.fragmentedCells }
//
//        val c2 = c1.increaseColBy(23)
//        assertTrue { c2 !in cursorState.fragmentedCells }
//        wsActions.ctrlClickSelectCell(c2)
//        assertTrue { c2 in cursorState.fragmentedCells }
//    }
//
//    @Test
//    fun shiftClickSelectRange() {
//        val target1 = cursorState.mainCell.increaseColBy(2).increaseRowBy(3)
//        wsActions.shiftClickSelectRange(target1)
//        assertEquals(RangeAddresses.from2Cells(target1, cursorState.mainCell), cursorState.mainRange)
//        cursorStateMs.value = cursorState.removeMainRange()
//        wsActions.shiftClickSelectRange(cursorState.mainCell)
//        assertNull(cursorState.mainRange)
//    }
}
