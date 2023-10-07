package com.qxdzbc.p6.ui.worksheet.state

import androidx.compose.runtime.getValue
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.common.compose.layout_coor_wrapper.P6Layout
import com.qxdzbc.common.compose.layout_coor_wrapper.P6Layout.Companion.replaceWith
import com.qxdzbc.p6.command.CommandStack
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import com.qxdzbc.p6.document_data_layer.worksheet.Worksheet
import com.qxdzbc.p6.ui.cell.state.CellState
import com.qxdzbc.p6.ui.cell.state.CellStateImp
import com.qxdzbc.p6.ui.cell.state.CellStates
import com.qxdzbc.p6.ui.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.worksheet.di.qualifiers.DefaultCellStateContainer
import com.qxdzbc.p6.ui.worksheet.di.qualifiers.DefaultSelectRectStateMs
import com.qxdzbc.p6.ui.worksheet.di.qualifiers.*
import com.qxdzbc.p6.ui.worksheet.resize_bar.ResizeBarState
import com.qxdzbc.p6.ui.worksheet.ruler.RulerState
import com.qxdzbc.p6.ui.worksheet.select_rect.SelectRectState
import com.qxdzbc.p6.ui.worksheet.slider.GridSlider
import com.qxdzbc.p6.ui.format.CellFormatTable
import com.qxdzbc.p6.ui.worksheet.action.WorksheetLocalActions
import com.qxdzbc.p6.ui.worksheet.di.WsAnvilScope
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.di.qualifiers.ForHorizontalWsEdgeSlider
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.di.qualifiers.ForVerticalWsEdgeSlider
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.state.ScrollBarState
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject


/**
 * a GridSlider + col/row limit
 */
@ContributesBinding(WsAnvilScope::class, boundType = WorksheetState::class)
data class WorksheetStateImp @Inject constructor(
    override val wsMs: Ms<Worksheet>,
    override val sliderMs: Ms<GridSlider>,
    override val cursorStateMs: Ms<CursorState>,
    @ForCol
    override val colRulerStateMs: Ms<RulerState>,
    @ForRow
    override val rowRulerStateMs: Ms<RulerState>,
    override val cellLayoutCoorMapMs: Ms<Map<CellAddress, P6Layout>>,
    @CellGridLayoutMs
    override val cellGridLayoutCoorWrapperMs: Ms<P6Layout?>,
    @WsLayoutMs
    override val wsLayoutCoorWrapperMs: Ms<P6Layout?>,
    @DefaultCellStateContainer
    val cellStateContMs: Ms<CellStateContainer>,
    @DefaultSelectRectStateMs
    override val selectRectStateMs: Ms<SelectRectState>,
    @ForCol
    override val colResizeBarStateMs: Ms<ResizeBarState>,
    @ForRow
    override val rowResizeBarStateMs: Ms<ResizeBarState>,
    @Init_ColRange
    override val colRange: IntRange,
    @Init_RowRange
    override val rowRange: IntRange,
    override val cellFormatTableMs: Ms<CellFormatTable>,
    @WsUndoStack
    override val undoStackMs: Ms<CommandStack>,
    @WsRedoStack
    override val redoStackMs: Ms<CommandStack>,
    @ForVerticalWsEdgeSlider
    override val verticalScrollBarState: ScrollBarState,
    @ForHorizontalWsEdgeSlider
    override val horizontalScrollBarState: ScrollBarState,
    override val localAction: WorksheetLocalActions,
) : BaseWorksheetState() {

    override val worksheet: Worksheet by wsMs

    override val idMs: Ms<WorksheetId> = worksheet.idMs

    override val id: WorksheetId by idMs

    override val name: String
        get() = idMs.value.wsName

    //----- implement new functions below this line -----//

    override fun addCellLayoutCoor(cellAddress: CellAddress, layoutCoor: P6Layout) {
        val oldLayout: P6Layout? = this.cellLayoutCoorMap[cellAddress]
        val newLayout = oldLayout.replaceWith(layoutCoor) ?: layoutCoor
        val newMap = this.cellLayoutCoorMap + (cellAddress to newLayout)
        this.cellLayoutCoorMapMs.value = newMap
    }

    override fun removeCellLayoutCoor(cellAddress: CellAddress) {
        if (cellAddress in this.cellLayoutCoorMap.keys) {
            this.cellLayoutCoorMapMs.value = this.cellLayoutCoorMap - (cellAddress)
        }
    }

    override fun removeAllCellLayoutCoor() {
        if (this.cellLayoutCoorMap.isNotEmpty()) {
            this.cellLayoutCoorMapMs.value = emptyMap()
        }
    }

    /**
     * Refresh all cell states so that the resulting cell states in this worksheet state are either:
     *  - point to a valid cell
     *  - contain format data
     *  Cell states that don't point to a valid cell and containt no format data are removed.
     */
    override fun refreshCellState() {
        var newCellMsCont = CellStateContainers.immutable()
        val existingCells = this.worksheet.cellMsList
        val existingCellAddresses = existingCells.map { it.value.address }.toSet()
        /*
        Update all cell state ms with the latest cell.
        Update cell state container too.
        Create missing cell state for new data cell.
         */
        for (cellMs in existingCells) {
            val cellAddress = cellMs.value.address
            val cellStateMs = this.getCellStateMs(cellAddress)
            if (cellStateMs != null) {
                cellStateMs.value = cellStateMs.value.setCellMs(cellMs)
                newCellMsCont = newCellMsCont.set(cellAddress, cellStateMs)
            } else {
                val newCellState: Ms<CellState> = ms(CellStateImp(cellAddress, cellMs))
                newCellMsCont = newCellMsCont.set(cellAddress, newCellState)
            }
        }

        val currentCellStateContainer = this.cellStateCont
        // x: remove cell state if the cell is not in the current cell container
        for (cellStateMs in currentCellStateContainer.allElements) {
            val cellState = cellStateMs.value
            val addr = cellState.address
            if (addr !in existingCellAddresses) {
                newCellMsCont = newCellMsCont.remove(addr)
            }
        }

        cellStateContMs.value = newCellMsCont
    }

    override val wbKeySt: St<WorkbookKey>
        get() = this.id.wbKeySt

    override val wsNameSt: St<String>
        get() {
            return this.id.wsNameSt
        }

    private fun reScanCellLayout(){
        cellLayoutCoorMapMs.value = cellLayoutCoorMap
            .filter { (cellAddress, _) ->
                slider.containAddressInVisibleRange(cellAddress)
            }
    }
    override fun updateSliderAndRefreshDependentStates(i: GridSlider) {
        sliderMs.value = i
//        removeAllCellLayoutCoor()
        reScanCellLayout()

        colRulerStateMs.value = colRulerState
            .clearItemLayoutCoorsMap()
            .clearResizerLayoutCoorsMap()

        rowRulerStateMs.value = rowRulerState
            .clearItemLayoutCoorsMap()
            .clearResizerLayoutCoorsMap()

    }

    override val cellStateCont: CellStateContainer by cellStateContMs

    override fun removeCellState(vararg addresses: CellAddress) {
        val cont = addresses.fold(cellStateCont) { accCont: CellStateContainer, cellAddress ->
            accCont.remove(cellAddress)
        }
        cellStateContMs.value = cont
    }

    override fun removeCellState(addresses: Collection<CellAddress>) {
        val cont = addresses.fold(cellStateCont) { accCont, cellAddress ->
            accCont.remove(cellAddress)
        }
        cellStateContMs.value = cont
    }

    override fun createAndAddNewCellStateMs(cellState: CellState) {
        cellStateContMs.value = cellStateCont.set(cellState.address, ms(cellState))
    }

    override fun addOrOverwriteCellState(cellState: CellState) {
        val cellStateMs = getCellStateMs(cellState.address)
        if (cellStateMs != null) {
            cellStateMs.value = cellState
        } else {
            this.createAndAddNewCellStateMs(cellState)
        }
    }

    override fun addBlankCellState(address: CellAddress) {
        val blankState = CellStates.blank(address)
        createAndAddNewCellStateMs(blankState)
    }

    override fun addBlankCellState(label: String) {
        addBlankCellState(CellAddress(label))
    }

    override fun removeAllCellState() {
        cellStateContMs.value = cellStateCont.removeAll()
    }

    override fun getCellStateMs(cellAddress: CellAddress): Ms<CellState>? {
        val cellMs = cellStateCont.getElement(cellAddress)
        return cellMs
    }

    override fun getCellStateMs(label: String): Ms<CellState>? {
        return getCellStateMs(CellAddress(label))
    }

    override val wbKey: WorkbookKey get() = this.id.wbKey

    override val cellGridLayoutCoorWrapper: P6Layout? by cellGridLayoutCoorWrapperMs

    override fun setCellGridLayoutCoorWrapper(i: P6Layout) {
        this.cellGridLayoutCoorWrapperMs.value = i
    }

    override val wsLayoutCoorWrapper: P6Layout? by wsLayoutCoorWrapperMs

    override fun setWsLayoutCoorWrapper(i: P6Layout) {
        wsLayoutCoorWrapperMs.value = i
    }

}
