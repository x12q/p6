package com.qxdzbc.p6.ui.document.worksheet.state

import androidx.compose.runtime.getValue
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper.Companion.replaceWith
import com.qxdzbc.p6.app.command.CommandStack
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.p6.ui.document.cell.state.CellState
import com.qxdzbc.p6.ui.document.cell.state.CellStateImp
import com.qxdzbc.p6.ui.document.cell.state.CellStates
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.document.worksheet.di.qualifiers.DefaultCellStateContainer
import com.qxdzbc.p6.ui.document.worksheet.di.qualifiers.DefaultColResizeBarStateMs
import com.qxdzbc.p6.ui.document.worksheet.di.qualifiers.DefaultRowResizeBarStateMs
import com.qxdzbc.p6.ui.document.worksheet.di.qualifiers.DefaultSelectRectStateMs
import com.qxdzbc.p6.ui.document.worksheet.di.comp.*
import com.qxdzbc.p6.ui.document.worksheet.di.qualifiers.*
import com.qxdzbc.p6.ui.document.worksheet.resize_bar.ResizeBarState
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerState
import com.qxdzbc.p6.ui.document.worksheet.select_rect.SelectRectState
import com.qxdzbc.p6.ui.document.worksheet.slider.GridSlider
import com.qxdzbc.p6.ui.format.CellFormatTable
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
    @ColRuler
    override val colRulerStateMs: Ms<RulerState>,
    @RowRuler
    override val rowRulerStateMs: Ms<RulerState>,
    override val cellLayoutCoorMapMs: Ms<Map<CellAddress, LayoutCoorWrapper>>,
    @CellGridLayoutMs
    override val cellGridLayoutCoorWrapperMs: Ms<LayoutCoorWrapper?>,
    @WsLayoutMs
    override val wsLayoutCoorWrapperMs: Ms<LayoutCoorWrapper?>,
    @DefaultCellStateContainer
    val cellStateContMs: Ms<CellStateContainer>,
    @DefaultSelectRectStateMs
    override val selectRectStateMs: Ms<SelectRectState>,
    @DefaultColResizeBarStateMs
    override val colResizeBarStateMs: Ms<ResizeBarState>,
    @DefaultRowResizeBarStateMs
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
) : BaseWorksheetState() {

    override val id: WorksheetId
        get() {
            return idMs.value
        }

    override fun addCellLayoutCoor(cellAddress: CellAddress, layoutCoor: LayoutCoorWrapper) {
        val oldLayout: LayoutCoorWrapper? = this.cellLayoutCoorMap[cellAddress]
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

    override fun setSliderAndRefreshDependentStates(i: GridSlider) {
        this.sliderMs.value = i
        this.removeAllCellLayoutCoor()

        colRulerStateMs.value = colRulerState
            .clearItemLayoutCoorsMap()
            .clearResizerLayoutCoorsMap()

        rowRulerStateMs.value = rowRulerState
            .clearItemLayoutCoorsMap()
            .clearResizerLayoutCoorsMap()
    }

    override val cellStateCont: CellStateContainer
        get() = cellStateContMs.value

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
        val cellStateMs = this.getCellStateMs(cellState.address)
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

    override val cellGridLayoutCoorWrapper: LayoutCoorWrapper? by this.cellGridLayoutCoorWrapperMs

    override fun setCellGridLayoutCoorWrapper(i: LayoutCoorWrapper) {
        this.cellGridLayoutCoorWrapperMs.value = i
    }

    override val wsLayoutCoorWrapper: LayoutCoorWrapper? by this.wsLayoutCoorWrapperMs

    override fun setWsLayoutCoorWrapper(i: LayoutCoorWrapper) {
        wsLayoutCoorWrapperMs.value = i
    }

    override val worksheet: Worksheet by wsMs
    override val idMs: Ms<WorksheetId> = worksheet.idMs

    override val name: String
        get() = this.idMs.value.wsName

}
