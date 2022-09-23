package com.qxdzbc.p6.ui.document.worksheet.state

import androidx.compose.ui.layout.LayoutCoordinates
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.ui.document.cell.state.CellState
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.document.worksheet.resize_bar.ResizeBarState
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerState
import com.qxdzbc.p6.ui.document.worksheet.select_rect.SelectRectState
import com.qxdzbc.p6.ui.document.worksheet.slider.GridSlider

/**
 * Worksheet + UI data
 * store + expose ms object of slider, cursor
 * provide method to lookup cell state + ms
 * store + expose ms object of cell state
 */
interface WorksheetState :WbWsSt {

    val idMs: Ms<WorksheetId>
    val id:WorksheetId

//    val topLeftCell:CellAddress
//    fun setTopLeftCell(c:CellAddress): WorksheetState

    val colResizeBarStateMs: Ms<ResizeBarState>
    val colResizeBarState: ResizeBarState get() = colResizeBarStateMs.value

    val rowResizeBarStateMs: Ms<ResizeBarState>
    val rowResizeBarState: ResizeBarState get() = rowResizeBarStateMs.value

    val colRulerStateMs: Ms<RulerState>
    val colRulerState: RulerState get() = colRulerStateMs.value
    val rowRulerStateMs: Ms<RulerState>
    val rowRulerState: RulerState get() = rowRulerStateMs.value

    /**
     * The layout coor of the cell grid
     */
    val cellGridLayoutCoorWrapperMs:Ms<LayoutCoorWrapper?>
    val cellGridLayoutCoorWrapper: LayoutCoorWrapper?
    val cellGridLayoutCoors: LayoutCoordinates? get()=cellGridLayoutCoorWrapper?.layout
    fun setCellGridLayoutCoorWrapper(i: LayoutCoorWrapper):WorksheetState

    /**
     * The layout coor of the whole worksheet (including the grid + the ruler)
     */
    val wsLayoutCoorWrapperMs:Ms<LayoutCoorWrapper?>
    val wsLayoutCoorWrapper: LayoutCoorWrapper?
    val wsLayoutCoors: LayoutCoordinates? get() = wsLayoutCoorWrapper?.layout
    fun setwsLayoutCoorWrapper(i: LayoutCoorWrapper):WorksheetState

    val wsMs: Ms<Worksheet>
    val worksheet: Worksheet
    val name: String

    /**
     * point this state to another worksheet, and update the state id
     */
    fun setWsMs(wsMs: Ms<Worksheet>):WorksheetState

    val cursorStateMs: Ms<CursorState>
    val cursorState: CursorState get() = cursorStateMs.value

    val sliderMs: Ms<GridSlider>
    val slider: GridSlider get() = sliderMs.value
    fun setSlider(i:GridSlider):WorksheetState

    val selectRectStateMs: Ms<SelectRectState>
    val selectRectState: SelectRectState get() = selectRectStateMs.value

    val cellStateCont: CellStateContainer
    fun removeCellState(vararg addresses: CellAddress): WorksheetState
    fun removeCellState(addresses: Collection<CellAddress>): WorksheetState
    fun addCellState(address: CellAddress, cellState: CellState): WorksheetState
    fun removeAllCellState(): WorksheetState

    fun getCellStateMs(colIndex: Int, rowIndex: Int): Ms<CellState>?
    fun getCellStateMs(cellAddress: CellAddress): Ms<CellState>?
    fun getCellState(cellAddress: CellAddress): CellState?
    fun getCellState(label:String): CellState?
    fun getCellState(colIndex: Int, rowIndex: Int): CellState?

    /**
     * col range
     */
    val colRange: IntRange
    val firstCol: Int get() = colRange.first
    val lastCol: Int get() = colRange.last

    /**
     * row range
     */
    val rowRange: IntRange
    val firstRow: Int get() = rowRange.first
    val lastRow: Int get() = rowRange.last

    val defaultColWidth: Int

    /**
     * @return column width or null if it is not available
     */
    fun getColumnWidth(colIndex: Int): Int?

    /**
     * @return column width if it is available, otherwise return a default value.
     */
    fun getColumnWidthOrDefault(colIndex: Int): Int
    fun addColumnWidth(colIndex: Int, colWidth: Int): WorksheetState
    fun restoreColumnWidthToDefault(colIndex: Int): WorksheetState
    fun changeColSize(colIndex: Int, sizeDiff: Float): WorksheetState

    val defaultRowHeight: Int
    fun getRowHeight(rowIndex: Int): Int?
    fun getRowHeightOrDefault(rowIndex: Int): Int
    fun addRowHeight(rowIndex: Int, rowHeight: Int): WorksheetState
    fun restoreRowHeightToDefault(rowIndex: Int): WorksheetState
    fun changeRowSize(rowIndex: Int, sizeDiff: Float): WorksheetState

    val cellLayoutCoorMapMs: Ms<Map<CellAddress, LayoutCoorWrapper>>
    val cellLayoutCoorMap: Map<CellAddress, LayoutCoorWrapper> get() = cellLayoutCoorMapMs.value
    fun addCellLayoutCoor(cellAddress: CellAddress, layoutCoor: LayoutCoorWrapper): WorksheetState
    fun removeCellLayoutCoor(cellAddress: CellAddress): WorksheetState
    fun removeAllCellLayoutCoor(): WorksheetState

    fun refreshCellState():WorksheetState
    fun refresh():WorksheetState
}


