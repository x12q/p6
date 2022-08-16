package com.emeraldblast.p6.ui.document.worksheet.actions.action2

//import io.mockk.every
//import io.mockk.mockk

//TODO these tests are still valid, but disabled until better implementation is completed
internal class WorksheetAction2ImpTest {
//    lateinit var ws: Worksheet
//    lateinit var wsStateMs: Ms<WorksheetState>
//    lateinit var wsActions: WorksheetAction2Imp
//    val cursorStateMs get() = wsState.cursorStateMs
//    val cursorState: CursorState get() = wsStateMs.value.cursorStateMs.value
//    val wsState: WorksheetState get() = wsStateMs.value
//    val slider: GridSlider get() = wsState.slider
//    val mockOffset = Offset(0F, 0F)
//    lateinit var layoutMap: MutableMap<CellAddress, LayoutCoorWrapper>
//    lateinit var posMap: MutableMap<CellAddress, Rect>
//    lateinit var wb:Workbook
//    @BeforeTest
//    fun before() {
//        val testSample = TestSample()
//        ws = WorksheetImp("Sheet1",wbKey =mock())
//        wb = WorkbookImp(WorkbookKey("Book1",null), listOf(ws))
//        testSample.wbContMs.value = testSample.wbContMs.value.overwriteWB(wb)
//
//        wsStateMs = ms(
//            WorksheetStateImp.default(
//                wsName=ws.name,
//                workbookStateIDMs = ms(
//                    WorkbookStateIDImp(wb.key)
//                ),
//                wbContMs = testSample.wbContMs
//            )!!
//        )
//
//        layoutMap = mutableMapOf()
//        posMap = mutableMapOf<CellAddress, Rect>().also {
//            for (r in 1..20) {
//                for (c in 1..20) {
//                    val rect = Rect(
//                        topLeft = Offset(
//                            (c - 1) * wsStateMs.value.defaultColWidth.toFloat(),
//                            (r - 1) * wsStateMs.value.defaultRowHeight.toFloat()
//                        ),
//                        bottomRight = Offset(
//                            c * wsStateMs.value.defaultColWidth.toFloat(),
//                            r * wsStateMs.value.defaultRowHeight.toFloat()
//                        )
//                    )
//                    val cellAddress = CellAddress(c, r)
//                    val mockLayout = mock<LayoutCoorWrapper>() {
//                        whenever(it.boundInWindow).thenReturn(rect)
//                    }
//                    layoutMap[cellAddress] = mockLayout
//
//                    it[cellAddress] = rect
//                }
//            }
//        }
//        wsActions = WorksheetAction2Imp(testSample.appStateMs)
//        for ((c, l) in layoutMap) {
//            wsStateMs.value = wsStateMs.value.addCellLayoutCoor(c, l)
//        }
//    }
//
//    @Test
//    fun updateSlider() {
//        val d = 3
//        val oldSlider = slider
//        val newCursor = CursorStateImp.default(mock()).copy(
//            mainCell = CellAddresses.fromIndices(
//                colIndex = slider.visibleColRange.last + d,
//                rowIndex = slider.visibleRowRange.random()
//            )
//        )
//        wsActions.makeSliderFollowCursor(newCursor,)
//        println(wsState.slider)
//
//        assertEquals(oldSlider.visibleColRange.add(d), slider.visibleColRange)
//        assertEquals(oldSlider.firstVisibleCol + d, slider.firstVisibleCol)
//        assertEquals(oldSlider.lastVisibleCol + d, slider.lastVisibleCol)
//
//        val cursor2 = CursorStateImp.default(mock()).copy(
//            mainCell = CellAddresses.fromIndices(
//                colIndex = slider.visibleColRange.random(),
//                rowIndex = slider.visibleRowRange.add(d).last
//            )
//        )
//
//        val oldSlider2 = slider
//        wsActions.makeSliderFollowCursor(cursor2)
//
//        assertEquals(oldSlider2.visibleRowRange.add(d), slider.visibleRowRange)
//        assertEquals(oldSlider2.firstVisibleRow + d, slider.firstVisibleRow)
//        assertEquals(oldSlider2.lastVisibleRow + d, slider.lastVisibleRow)
//    }
//
//    @Test
//    fun scroll() {
//        val oldSlider = slider
//        wsActions.addCellLayoutCoor(CellAddresses.fromIndices(1, 2), mock())
//        assertFalse { wsState.cellLayoutCoorMap.isEmpty() }
//        val (x1, y1) = Pair(3, 7)
//        wsActions.scroll(x1, y1)
//        assertEquals(oldSlider.visibleRowRange.add(y1), slider.visibleRowRange)
//        assertEquals(oldSlider.visibleColRange.add(x1), slider.visibleColRange)
//
//        val (x2, y2) = Pair(-2, -4)
//        val oldSlider2 = slider
//        wsActions.scroll(x2, y2)
//        assertEquals(oldSlider2.visibleRowRange.add(y2), slider.visibleRowRange)
//        assertEquals(oldSlider2.visibleColRange.add(x2), slider.visibleColRange)
//
//        val (x3, y3) = Pair(-1000, -2000)
//        wsActions.scroll(x3, y3)
//        assertEquals(oldSlider.visibleRowRange, slider.visibleRowRange)
//        assertEquals(oldSlider.visibleColRange, slider.visibleColRange)
//
//        val (x4, y4) = Pair(Int.MAX_VALUE, Int.MAX_VALUE)
//        wsActions.scroll(x4, y4)
//        assertEquals(wsState.colRange.last, slider.lastVisibleCol)
//        assertEquals(wsState.colRange.last - oldSlider.visibleColRange.dif(), slider.firstVisibleCol)
//        assertEquals(wsState.rowRange.last, slider.lastVisibleRow)
//        assertEquals(wsState.rowRange.last - oldSlider.visibleRowRange.dif(), slider.firstVisibleRow)
//    }
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
//        wsStateMs.value = wsState.addCellLayoutCoor(CellAddress(999, 999), mock())
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
