package com.qxdzbc.p6.ui.worksheet.state

import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.unit.Dp
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.layout_coor_wrapper.P6LayoutCoor
import com.qxdzbc.p6.composite_actions.common_data_structure.WbWsSt
import com.qxdzbc.p6.command.Command
import com.qxdzbc.p6.command.CommandStack
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.document_data_layer.worksheet.Worksheet
import com.qxdzbc.p6.proto.DocProtos.WorksheetProto
import com.qxdzbc.p6.ui.cell.state.CellState
import com.qxdzbc.p6.ui.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.worksheet.resize_bar.ResizeBarState
import com.qxdzbc.p6.ui.worksheet.ruler.RulerState
import com.qxdzbc.p6.ui.worksheet.ruler.RulerType
import com.qxdzbc.p6.ui.worksheet.select_rect.SelectRectState
import com.qxdzbc.p6.ui.worksheet.slider.GridSlider
import com.qxdzbc.p6.ui.format.CellFormatTable
import com.qxdzbc.p6.ui.worksheet.slider.edge_slider.state.EdgeSliderState

/**
 * Worksheet + UI data
 * store + expose ms object of slider, cursor
 * provide method to lookup cell state + ms
 * store + expose ms object of cell state
 *
 * See [WorksheetStateImp] for implementation.
 *
 * To create a [WorksheetState], use [WorksheetStateFactory].
 *
 * [WorksheetStateFactory] is available as a singleton in the global DI container.
 */
interface WorksheetState : WbWsSt {


    /**
     * TODO Reconsider this id obj. This contains duplicated information.
     */
    val idMs: Ms<WorksheetId>
    val id: WorksheetId

    /**
     * Produce a [WorksheetProto] from this worksheet
     */
    fun toProto():WorksheetProto

    /**
     * A stack of [Command], for undoing actions
     */
    val undoStackMs: Ms<CommandStack>
    val undoStack: CommandStack

    /**
     * A stack of [Command], for re-doing actions
     */
    val redoStackMs: Ms<CommandStack>
    val redoStack: CommandStack

    /**
     * contain format information for all cells in this worksheet
     */
    val cellFormatTableMs: Ms<CellFormatTable>
    val cellFormatTable: CellFormatTable


    val verticalEdgeSliderState:EdgeSliderState


    /**
     * State of the resizing bar for resizing column
     */
    val colResizeBarStateMs: Ms<ResizeBarState>
    val colResizeBarState: ResizeBarState get() = colResizeBarStateMs.value

    /**
     * State of the resizing bar for resizing row
     */
    val rowResizeBarStateMs: Ms<ResizeBarState>
    val rowResizeBarState: ResizeBarState get() = rowResizeBarStateMs.value

    /**
     * state for the ruler of column
     */
    val colRulerStateMs: Ms<RulerState>
    val colRulerState: RulerState get() = colRulerStateMs.value

    /**
     * state for the ruler of row
     */
    val rowRulerStateMs: Ms<RulerState>
    val rowRulerState: RulerState get() = rowRulerStateMs.value

    /**
     * The layout coor of the cell grid
     */
    val cellGridLayoutCoorWrapperMs: Ms<P6LayoutCoor?>
    val cellGridLayoutCoorWrapper: P6LayoutCoor?
    val cellGridLayoutCoors: LayoutCoordinates? get() = cellGridLayoutCoorWrapper?.layout
    fun setCellGridLayoutCoorWrapper(i: P6LayoutCoor)

    /**
     * The layout coor of the whole worksheet (including the grid + the ruler)
     */
    val wsLayoutCoorWrapperMs: Ms<P6LayoutCoor?>
    val wsLayoutCoorWrapper: P6LayoutCoor?
    val wsLayoutCoors: LayoutCoordinates? get() = wsLayoutCoorWrapper?.layout
    fun setWsLayoutCoorWrapper(i: P6LayoutCoor)

    val wsMs: Ms<Worksheet>
    val worksheet: Worksheet
    val name: String

    val cursorStateMs: Ms<CursorState>
    val cursorState: CursorState get() = cursorStateMs.value

    val sliderMs: Ms<GridSlider>
    val slider: GridSlider get() = sliderMs.value

    /**
     * Set new slider, and refresh states that are affected by this, including:
     *  - ruler states
     *  - cell layouts
     */
    fun setSliderAndRefreshDependentStates(i: GridSlider)

    /**
     * State of the select rectangle used for selecting multiple cells at once by dragging the mouse on this worksheet
     */
    val selectRectStateMs: Ms<SelectRectState>
    val selectRectState: SelectRectState get() = selectRectStateMs.value

    val cellStateCont: CellStateContainer

    /**
     * Remove cell state obj for cells at [addresses]
     */
    fun removeCellState(vararg addresses: CellAddress)
    fun removeCellState(addresses: Collection<CellAddress>)

    /**
     * create a new Ms<CellState> from [cellState], and add it.
     * Beware this will overwrite old ms obj if it exists
     */
    fun createAndAddNewCellStateMs(cellState: CellState)

    /**
     * Add or overwrite a cell state if one already exist
     */
    fun addOrOverwriteCellState(cellState: CellState)

    /**
     * Add a blank cell state object for cell at [address]
     */
    fun addBlankCellState(address: CellAddress)

    /**
     * Add a blank cell state object for cell at [label]
     */
    fun addBlankCellState(label: String)

    /**
     * Remove all cell states
     */
    fun removeAllCellState()

    fun getCellStateMs(colIndex: Int, rowIndex: Int): Ms<CellState>?
    fun getCellStateMs(cellAddress: CellAddress): Ms<CellState>?
    fun getCellStateMs(label: String): Ms<CellState>?
    fun getCellState(cellAddress: CellAddress): CellState?
    fun getCellState(label: String): CellState?
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

    val defaultColWidth: Dp

    /**
     * A map of column width.
     */
    val columnWidthMap: Map<Int, Dp>

    /**
     * @return width of column at [colIndex], or null if there isn't a record for that width value.
     */
    fun getColumnWidth(colIndex: Int): Dp?

    /**
     * @return width of the column at [colIndex], otherwise return [defaultColWidth].
     */
    fun getColumnWidthOrDefault(colIndex: Int): Dp
    fun addColumnWidth(colIndex: Int, colWidth: Dp)
    fun restoreColumnWidthToDefault(colIndex: Int)
    fun changeColWidth(colIndex: Int, sizeDiff: Dp)

    /**
     * A map of row height.
     */
    val rowHeightMap: Map<Int, Dp>
    val defaultRowHeight: Dp

    /**
     * @return height of row at [rowIndex] or null, if there isn't a record for that height value.
     */
    fun getRowHeight(rowIndex: Int): Dp?

    /**
     * @return height of the row at [rowIndex], or [defaultRowHeight] if there isn't a record for that height value.
     */
    fun getRowHeightOrDefault(rowIndex: Int): Dp
    fun addRowHeight(rowIndex: Int, rowHeight: Dp)
    fun restoreRowHeightToDefault(rowIndex: Int)

    /**
     * change size of the row at [rowIndex] by adding [sizeDiff] to the current size.
     */
    fun changeRowHeight(rowIndex: Int, sizeDiff: Dp)

    val cellLayoutCoorMapMs: Ms<Map<CellAddress, P6LayoutCoor>>
    val cellLayoutCoorMap: Map<CellAddress, P6LayoutCoor> get() = cellLayoutCoorMapMs.value
    fun addCellLayoutCoor(cellAddress: CellAddress, layoutCoor: P6LayoutCoor)
    fun removeCellLayoutCoor(cellAddress: CellAddress)
    fun removeAllCellLayoutCoor()

    fun refreshCellState()
    fun refresh()
    fun getRulerState(rulerType: RulerType): RulerState
}


