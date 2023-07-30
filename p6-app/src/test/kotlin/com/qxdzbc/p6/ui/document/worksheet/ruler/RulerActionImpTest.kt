package com.qxdzbc.p6.ui.document.worksheet.ruler


internal class RulerActionImpTest {

//    lateinit var wsState: WorksheetState
//    lateinit var rowRulerStateMs: Ms<RulerState>
//    lateinit var colRulerStateMs: Ms<RulerState>
//    lateinit var rowRulerActions: RulerActionImp
//    lateinit var colRulerActions: RulerActionImp
//    lateinit var ws: Worksheet
//    val wsState get() = wsState.value
//
//    lateinit var rowLayoutMap: Map<Int, LayoutCoorWrapper>
//    lateinit var rowRectMap: Map<Int, Rect>
//
//    lateinit var colRectMap: Map<Int, Rect>
//    lateinit var colLayoutMap: Map<Int, LayoutCoorWrapper>
//
//    val mockOffset = Offset(0F, 0F)
//    val cursorState: CursorState get() = wsState.cursorState
//    lateinit var wb: Workbook
//    @BeforeTest
//    fun before() {
//        ws = WorksheetImp("sheet1",wbKey =mock())
//
//        wb = WorkbookImp(
//            WorkbookKey("Wb", null), listOf(ws)
//        )
//        val testSample = TestSample()
//        testSample.wbContMs.value = testSample.wbContMs.value.overwriteWB(wb)
//        wsState = ms(
//            WorksheetStateImp.default(
//                wsName=ws.name,
//                workbookStateIDMs = ms(
//                    WorkbookStateIDImp(wb.key)
//                ),
//                wbContMs = testSample.wbContMs
//            )!!
//        )
//        rowRulerStateMs = wsState.value.rowRulerStateMs
//        colRulerStateMs = wsState.value.colRulerStateMs
//
//        rowRectMap = mutableMapOf()
//        rowLayoutMap = emptyMap()
//        colRectMap = emptyMap()
//        colLayoutMap = emptyMap()
//        for (x in 1..20) {
//            val rowRect = Rect(
//                topLeft = Offset(
//                    x = 0F,
//                    y = (x - 1) * wsState.defaultRowHeight.toFloat()
//                ),
//                bottomRight = Offset(
//                    x = wsState.defaultColWidth.toFloat(),
//                    y = x * wsState.defaultRowHeight.toFloat()
//                )
//            )
//            rowRectMap = rowRectMap + (x to rowRect)
//            val rowItemLayout = mock<LayoutCoorWrapper>() {
//                whenever(it.boundInWindow).thenReturn(rowRect)
//            }
//            rowLayoutMap = rowLayoutMap + (x to rowItemLayout)
//
//            rowRulerStateMs.value = rowRulerStateMs.value.addItemLayout(x, rowItemLayout)
//
//            val colRect = Rect(
//                topLeft = Offset(
//                    x = (x - 1) * wsState.defaultColWidth.toFloat(),
//                    y = 0F,
//                ),
//                bottomRight = Offset(
//                    x = x * wsState.defaultColWidth.toFloat(),
//                    y = wsState.defaultRowHeight.toFloat(),
//                )
//            )
//            colRectMap = colRectMap + (x to colRect)
//            val colLayout = mock<LayoutCoorWrapper>() {
//                whenever(it.boundInWindow).thenReturn(colRect)
//            }
//            colLayoutMap = colLayoutMap + (x to colLayout)
//            colRulerStateMs.value = colRulerStateMs.value.addItemLayout(x, colLayout)
//        }
//
//        rowRulerActions = RulerActionImp(
//            testSample.appStateMs
//        )
//
//        colRulerActions = RulerActionImp(
//            testSample.appStateMs
//        )
//    }
//
//    @Test
//    fun shiftClick() {
//        assertNull(cursorState.mainRange)
//        val anchor = cursorState.mainCell
//        colRulerActions.clickRulerItem(2)
//        colRulerActions.shiftClick(2)
//        assertEquals(RangeAddresses.wholeCol(2), cursorState.mainRange)
//        assertEquals(CellAddress(2, 1), cursorState.mainCell)
//
//        colRulerActions.shiftClick(4)
//        assertEquals(RangeAddresses.wholeMultiCol(2, 4), cursorState.mainRange)
//        assertEquals(CellAddress(2, 1), cursorState.mainCell)
//
//        colRulerActions.shiftClick(1)
//        assertEquals(RangeAddresses.wholeMultiCol(1, 2), cursorState.mainRange)
//        assertEquals(CellAddress(2, 1), cursorState.mainCell)
//    }
//
//    @Test
//    fun ctrlClick() {
//        assertNull(cursorState.mainRange)
//        val anchor = cursorState.mainCell
//        colRulerActions.ctrlClick(4)
//
//        assertEquals(anchor, cursorState.mainCell)
//        assertTrue { RangeAddresses.wholeCol(4) in cursorState.fragmentedRanges }
//
//        colRulerActions.ctrlClick(6)
//        assertEquals(anchor, cursorState.mainCell)
//        assertTrue { RangeAddresses.wholeCol(6) in cursorState.fragmentedRanges }
//
//        colRulerActions.ctrlClick(4)
//        assertEquals(anchor, cursorState.mainCell)
//        assertTrue { RangeAddresses.wholeCol(4) !in cursorState.fragmentedRanges }
//        assertTrue { RangeAddresses.wholeCol(6) in cursorState.fragmentedRanges }
//    }
//
//    @Test
//    fun clickRulerItem() {
//        val rowIndex = 12
//        assertNull(wsState.cursorState.mainRange)
//        rowRulerActions.clickRulerItem(rowIndex)
//        assertEquals(RangeAddresses.wholeRow(rowIndex), wsState.cursorState.mainRange)
//    }
//
//    @Test
//    fun changeColWidth() {
//        val col = 123
//        val d = 200F
//        val oldWidth = wsState.getColumnWidthOrDefault(col)
//        assertEquals(wsState.defaultColWidth, oldWidth)
//        rowRulerActions.changeColWidth(col, d)
//        assertEquals(oldWidth + d, wsState.getColumnWidthOrDefault(col).toFloat())
//    }
//
//    @Test
//    fun changeRowHeight() {
//        val row = 123
//        val d = 200F
//        val oldWidth = wsState.getRowHeightOrDefault(row)
//        assertEquals(wsState.defaultRowHeight, oldWidth)
//        rowRulerActions.changeRowHeight(row, d)
//        assertEquals(oldWidth + d, wsState.getRowHeightOrDefault(row).toFloat())
//    }
//
//    private fun makeRowPoint(i: Int): Offset {
//        return Offset(
//            x = wsState.defaultColWidth / 2.toFloat(),
//            y = (i - 1) * wsState.defaultRowHeight + wsState.defaultRowHeight / 2.toFloat()
//        )
//    }
//
//    @Test
//    fun startDragSelection() {
//        assertFalse(rowRulerStateMs.value.itemSelectRect.isActive)
//        assertFalse(rowRulerStateMs.value.itemSelectRect.isShow)
//
//        val startingRow = 2
//        rowRulerActions.startDragSelection(
//            mousePosition = makeRowPoint(startingRow),
//        )
//
//        assertTrue(rowRulerStateMs.value.itemSelectRect.isActive)
//        assertFalse(rowRulerStateMs.value.itemSelectRect.isShow)
//        assertEquals(makeRowPoint(startingRow), rowRulerStateMs.value.itemSelectRect.anchorPoint)
//    }
//
//    @Test
//    fun `makeMouseDragSelectionIfPossible on inactive SelectRect`() {
//        val endRow = 7
//        rowRulerActions.makeMouseDragSelectionIfPossible(
//            mousePosition = makeRowPoint(endRow),
//        )
//        assertFalse(rowRulerStateMs.value.itemSelectRect.isShow)
//        assertFalse(rowRulerStateMs.value.itemSelectRect.isActive)
//    }
//
//    @Test
//    fun `makeMouseDragSelectionIfPossible on active SelectRect`() {
//        val startingRow = 2
//        val endRow = 7
//        assertFalse(rowRulerStateMs.value.itemSelectRect.isShow)
//        assertFalse(rowRulerStateMs.value.itemSelectRect.isActive)
//        rowRulerActions.startDragSelection(
//            mousePosition = makeRowPoint(startingRow),
//        )
//        rowRulerActions.makeMouseDragSelectionIfPossible(
//            mousePosition = makeRowPoint(endRow),
//        )
//        assertTrue(rowRulerStateMs.value.itemSelectRect.isShow)
//        assertTrue(rowRulerStateMs.value.itemSelectRect.isActive)
//
//        assertEquals(RangeAddresses.wholeMultiRow(startingRow, endRow), wsState.cursorState.mainRange)
//    }
//
//
//    @Test
//    fun updateItemLayout() {
//        val oldSize = rowRulerStateMs.value.itemLayoutMap.size
//        rowRulerActions.updateItemLayout(100, mock())
//        assertEquals(oldSize + 1, rowRulerStateMs.value.itemLayoutMap.size)
//    }
//
//    @Test
//    fun updateRulerLayout() {
//        assertNull(rowRulerStateMs.value.rulerLayout)
//        rowRulerActions.updateRulerLayout(mock())
//        assertNotNull(rowRulerStateMs.value.rulerLayout)
//    }
}
