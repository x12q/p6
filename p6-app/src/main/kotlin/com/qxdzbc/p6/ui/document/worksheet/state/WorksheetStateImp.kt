package com.qxdzbc.p6.ui.document.worksheet.state

import androidx.compose.runtime.getValue
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper.Companion.replaceWith
import com.qxdzbc.p6.app.command.CommandStack
import com.qxdzbc.p6.app.command.CommandStacks
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.p6.ui.common.P6R
import com.qxdzbc.p6.ui.document.cell.state.CellState
import com.qxdzbc.p6.ui.document.cell.state.CellStateImp
import com.qxdzbc.p6.ui.document.cell.state.CellStates
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.document.worksheet.resize_bar.ResizeBarState
import com.qxdzbc.p6.ui.document.worksheet.resize_bar.ResizeBarStateImp
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerState
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerType
import com.qxdzbc.p6.ui.document.worksheet.select_rect.SelectRectState
import com.qxdzbc.p6.ui.document.worksheet.select_rect.SelectRectStateImp
import com.qxdzbc.p6.ui.document.worksheet.slider.GridSlider
import com.qxdzbc.p6.ui.format.CellFormatTable
import com.qxdzbc.p6.ui.format.CellFormatTableImp
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


/**
 * a GridSlider + col/row limit
 */
data class WorksheetStateImp constructor(
    // ====Assisted inject properties ======//
    override val wsMs: Ms<Worksheet>,
    override val sliderMs: Ms<GridSlider>,
    override val cursorStateMs: Ms<CursorState>,
    override val colRulerStateMs: Ms<RulerState>,
    override val rowRulerStateMs: Ms<RulerState>,
    override val cellLayoutCoorMapMs: Ms<Map<CellAddress, LayoutCoorWrapper>>,

    //====Automatically injected properties====//
    override val cellGridLayoutCoorWrapperMs: Ms<LayoutCoorWrapper?> = ms(null),
    override val wsLayoutCoorWrapperMs: Ms<LayoutCoorWrapper?> = ms(null),
    val cellStateContMs: Ms<CellStateContainer> = CellStateContainers.immutable().toMs(),
    override val selectRectStateMs: Ms<SelectRectState> = ms(SelectRectStateImp()),
    override val colResizeBarStateMs: Ms<ResizeBarState> = ms(
        ResizeBarStateImp(
            dimen = RulerType.Col,
            size = P6R.size.value.defaultRowHeight
        )
    ),
    override val rowResizeBarStateMs: Ms<ResizeBarState> = ms(
        ResizeBarStateImp(
            dimen = RulerType.Row,
            size = P6R.size.value.rowRulerWidth
        )
    ),
    override val colRange: IntRange = P6R.worksheetValue.defaultColRange,
    override val rowRange: IntRange = P6R.worksheetValue.defaultRowRange,
    override val cellFormatTableMs: Ms<CellFormatTable> = ms(CellFormatTableImp()),
    override val undoStackMs: Ms<CommandStack>,
    override val redoStackMs: Ms<CommandStack>,
) : BaseWorksheetState() {

    @AssistedInject
    constructor(
        @Assisted("1") wsMs: Ms<Worksheet>,
        @Assisted("2") sliderMs: Ms<GridSlider>,
        @Assisted("3") cursorStateMs: Ms<CursorState>,
        @Assisted("4") colRulerStateMs: Ms<RulerState>,
        @Assisted("5") rowRulerStateMs: Ms<RulerState>,
        @Assisted("6") cellLayoutCoorMapMs: Ms<Map<CellAddress, LayoutCoorWrapper>>,
    ) : this(
        wsMs, sliderMs, cursorStateMs, colRulerStateMs, rowRulerStateMs, cellLayoutCoorMapMs,
        cellGridLayoutCoorWrapperMs = ms(null),
        wsLayoutCoorWrapperMs = ms(null),
        cellStateContMs = CellStateContainers.immutable().toMs(),
        selectRectStateMs = ms(SelectRectStateImp()),
        colResizeBarStateMs = ms(ResizeBarStateImp(dimen = RulerType.Col, size = P6R.size.value.defaultRowHeight)),
        rowResizeBarStateMs = ms(ResizeBarStateImp(dimen = RulerType.Row, size = P6R.size.value.rowRulerWidth)),
        colRange = P6R.worksheetValue.defaultColRange,
        rowRange = P6R.worksheetValue.defaultRowRange,
        cellFormatTableMs = ms(CellFormatTableImp()),
        undoStackMs = ms(CommandStacks.stdCommandStack()),
        redoStackMs = ms(CommandStacks.stdCommandStack()),
    )


    override val id: WorksheetId
        get() {
            return idMs.value
        }

    override fun addCellLayoutCoor(cellAddress: CellAddress, layoutCoor: LayoutCoorWrapper): WorksheetState {
        val oldLayout: LayoutCoorWrapper? = this.cellLayoutCoorMap[cellAddress]
        val newLayout = oldLayout.replaceWith(layoutCoor) ?: layoutCoor
        // TODO: keep the below commented code just in case the new code has bug.
//        val newLayout: LayoutCoorWrapper = if (oldLayout == null) {
//            layoutCoor
//        } else {
//            if (oldLayout.layout != layoutCoor.layout) {
//                layoutCoor
//            } else {
//                /*
//                Force refresh cell layout wrapper to force the app to redraw cell when col/row is resize. This is to fix the bug that cell cursor is not resized when col/row is resized
//                TODO this might be optimized further to reduce redrawing.
//                 */
//                layoutCoor.forceRefresh(!oldLayout.refreshVar)
//            }
//        }
        val newMap = this.cellLayoutCoorMap + (cellAddress to newLayout)
        this.cellLayoutCoorMapMs.value = newMap
        return this
    }

    override fun removeCellLayoutCoor(cellAddress: CellAddress): WorksheetState {
        if (cellAddress in this.cellLayoutCoorMap.keys) {
            this.cellLayoutCoorMapMs.value = this.cellLayoutCoorMap - (cellAddress)
        }
        return this
    }

    override fun removeAllCellLayoutCoor(): WorksheetState {
        if (this.cellLayoutCoorMap.isNotEmpty()) {
            this.cellLayoutCoorMapMs.value = emptyMap()
        }
        return this
    }

    /**
     * Refresh all cell states so that the resulting cell states in this worksheet state are either:
     *  - point to a valid cell
     *  - contain format data
     *  Cell states that don't point to a valid cell and containt no format data are removed.
     */
    override fun refreshCellState(): WorksheetState {
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
//                cellStateMs.value = cellState.removeDataCell()
//                newCellMsCont = newCellMsCont.set(addr, cellStateMs)
                newCellMsCont = newCellMsCont.remove(addr)
            }
        }

        cellStateContMs.value = newCellMsCont
        return this
    }

    override val wbKeySt: St<WorkbookKey>
        get() = this.id.wbKeySt
    override val wsNameSt: St<String>
        get() {
            return this.id.wsNameSt
        }

    override fun setSliderAndRefreshDependentStates(i: GridSlider): WorksheetState {
        this.sliderMs.value = i
        var wsState: WorksheetState = this
        wsState = wsState.removeAllCellLayoutCoor()

        colRulerStateMs.value = colRulerState
            .clearItemLayoutCoorsMap()
            .clearResizerLayoutCoorsMap()

        rowRulerStateMs.value = rowRulerState
            .clearItemLayoutCoorsMap()
            .clearResizerLayoutCoorsMap()
        return wsState
    }

    override val cellStateCont: CellStateContainer
        get() = cellStateContMs.value

    override fun removeCellState(vararg addresses: CellAddress): WorksheetState {
        val cont = addresses.fold(cellStateCont) { accCont: CellStateContainer, cellAddress ->
            accCont.remove(cellAddress)
        }
        cellStateContMs.value = cont
        return this
    }

    override fun removeCellState(addresses: Collection<CellAddress>): WorksheetState {
        val cont = addresses.fold(cellStateCont) { accCont, cellAddress ->
            accCont.remove(cellAddress)
        }
        cellStateContMs.value = cont
        return this
    }

    override fun createAndAddNewCellStateMs(cellState: CellState): WorksheetState {
        cellStateContMs.value = cellStateCont.set(cellState.address, ms(cellState))
        return this
    }

    override fun addOrOverwriteCellState(cellState: CellState): WorksheetState {
        val cellStateMs = this.getCellStateMs(cellState.address)
        if (cellStateMs != null) {
            cellStateMs.value = cellState
            return this
        } else {
            return this.createAndAddNewCellStateMs(cellState)
        }
    }

    override fun addBlankCellState(address: CellAddress): WorksheetState {
        val blankState = CellStates.blank(address)
        return createAndAddNewCellStateMs(blankState)
    }

    override fun addBlankCellState(label: String): WorksheetState {
        return addBlankCellState(CellAddress(label))
    }

    override fun removeAllCellState(): WorksheetState {
        cellStateContMs.value = cellStateCont.removeAll()
        return this
    }

    override fun getCellStateMs(cellAddress: CellAddress): Ms<CellState>? {
        val cellMs = cellStateCont.getElement(cellAddress)
        return cellMs
    }

    override fun getCellStateMs(label: String): Ms<CellState>? {
        return getCellStateMs(CellAddress(label))
    }

    override val wbKey: WorkbookKey
        get() = this.id.wbKey
    override val cellGridLayoutCoorWrapper: LayoutCoorWrapper? by this.cellGridLayoutCoorWrapperMs

    override fun setCellGridLayoutCoorWrapper(i: LayoutCoorWrapper): WorksheetState {
        this.cellGridLayoutCoorWrapperMs.value = i
        return this
    }

    override val wsLayoutCoorWrapper: LayoutCoorWrapper? by this.wsLayoutCoorWrapperMs

    override fun setwsLayoutCoorWrapper(i: LayoutCoorWrapper): WorksheetState {
        wsLayoutCoorWrapperMs.value = i
        return this
    }

    override val worksheet: Worksheet by wsMs
    override val idMs: Ms<WorksheetId> = worksheet.idMs

    override val name: String
        get() = this.idMs.value.wsName

    override fun setWsMs(wsMs: Ms<Worksheet>): WorksheetState {
        val newState = this.copy(wsMs = wsMs)
        val newWsStateIdSt = wsMs.value.idMs
        cursorState.id = cursorState.id.setWsStateIdSt(newWsStateIdSt)
        rowRulerStateMs.value = rowRulerState.setWsIdSt(newWsStateIdSt)
        colRulerStateMs.value = colRulerState.setWsIdSt(newWsStateIdSt)
        return newState
    }
}
