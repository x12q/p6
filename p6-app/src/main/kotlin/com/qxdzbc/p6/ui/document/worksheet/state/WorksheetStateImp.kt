package com.qxdzbc.p6.ui.document.worksheet.state

import androidx.compose.runtime.getValue
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.p6.di.state.ws.*
import com.qxdzbc.p6.ui.common.P6R
import com.qxdzbc.p6.ui.document.cell.state.CellState
import com.qxdzbc.p6.ui.document.cell.state.CellStateImp
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.document.worksheet.resize_bar.ResizeBarState
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerState
import com.qxdzbc.p6.ui.document.worksheet.select_rect.SelectRectState
import com.qxdzbc.p6.ui.document.worksheet.slider.GridSlider
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


/**
 * a GridSlider + col/row limit
 */
data class WorksheetStateImp @AssistedInject constructor(
    // ====Assisted inject properties ======//
    @Assisted("1") override val wsMs: Ms<Worksheet>,
    @Assisted("2") override val sliderMs: Ms<GridSlider>,
    @Assisted("3") override val cursorStateMs: Ms<CursorState>,
    @Assisted("4") override val colRulerStateMs: Ms<RulerState>,
    @Assisted("5") override val rowRulerStateMs: Ms<RulerState>,

    //====Automatically injected properties====//
    @DefaultCellLayoutMap
    override val cellLayoutCoorMapMs: Ms<Map<CellAddress, LayoutCoorWrapper>>,
    @DefaultLayoutCoorMs
    override val cellGridLayoutCoorWrapperMs: Ms<LayoutCoorWrapper?>,
    @DefaultLayoutCoorMs
    override val wsLayoutCoorWrapperMs: Ms<LayoutCoorWrapper?>,
    @DefaultCellStateContainer
    val cellStateContMs: Ms<CellStateContainer>,
    @DefaultSelectRectStateMs
    override val selectRectStateMs: Ms<SelectRectState>,
    @DefaultColResizeBarStateMs
    override val colResizeBarStateMs: Ms<ResizeBarState>,
    @DefaultRowResizeBarStateMs
    override val rowResizeBarStateMs: Ms<ResizeBarState>,
    @DefaultColRangeQualifier
    override val colRange: IntRange = P6R.worksheetValue.defaultColRange,
    @DefaultRowRangeQualifier
    override val rowRange: IntRange = P6R.worksheetValue.defaultRowRange,
) : BaseWorksheetState() {

    override val id: WorksheetId
        get() {
            return idMs.value
        }

    override fun addCellLayoutCoor(cellAddress: CellAddress, layoutCoor: LayoutCoorWrapper): WorksheetState {
        val oldLayout: LayoutCoorWrapper? = this.cellLayoutCoorMap[cellAddress]
        val newLayout: LayoutCoorWrapper = if(oldLayout== null){
            layoutCoor
        }else if (oldLayout?.layout != layoutCoor.layout) {
            layoutCoor
        } else {
            layoutCoor.forceRefresh(!oldLayout.refreshVar)
        }
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

    override fun refreshCellState(): WorksheetState {
        var newCellMsCont = CellStateContainers.immutable()
        val availableCells = this.worksheet.cellMsList
        val checkedCells = availableCells.map { it.value.address }.toSet()
        for (cellMs in availableCells) {
            val cellAddress = cellMs.value.address
            val currentCellStateMs = this.getCellStateMs(cellAddress)
            if (currentCellStateMs != null) {
                currentCellStateMs.value = currentCellStateMs.value.setCellMs(cellMs)
                newCellMsCont = newCellMsCont.set(cellAddress, currentCellStateMs)
            } else {
                val newCellState: Ms<CellState> = ms(CellStateImp(cellAddress, cellMs))
                newCellMsCont = newCellMsCont.set(cellAddress, newCellState)
            }
        }

        val oldContainer = this.cellStateCont
        // x: remove cellMs from cell state for empty cells
        for (cellStateMs in oldContainer.allElements) {
            val addr = cellStateMs.value.address
            if (addr !in checkedCells) {
                cellStateMs.value = cellStateMs.value.removeDataCell()
                newCellMsCont = newCellMsCont.set(addr, cellStateMs)
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
        var wsState:WorksheetState = this
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

    override fun addCellState(address: CellAddress, cellState: CellState): WorksheetState {
        cellStateContMs.value = cellStateCont.set(address, ms(cellState))
        return this
    }

    override fun removeAllCellState(): WorksheetState {
        cellStateContMs.value = cellStateCont.removeAll()
        return this
    }

    override fun getCellStateMs(cellAddress: CellAddress): Ms<CellState>? {
        val cellMs = cellStateCont.getElement(cellAddress)
        return cellMs
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
