package com.emeraldblast.p6.ui.document.worksheet.state

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import com.emeraldblast.p6.app.document.cell.address.CellAddress
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.app.document.worksheet.Worksheet
import com.emeraldblast.p6.di.state.ws.*
import com.emeraldblast.p6.ui.common.R
import com.emeraldblast.p6.ui.common.compose.layout_coor_wrapper.LayoutCoorWrapper
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.common.compose.St
import com.emeraldblast.p6.ui.common.compose.StateUtils.ms
import com.emeraldblast.p6.ui.document.cell.state.CellState
import com.emeraldblast.p6.ui.document.cell.state.CellStateImp
import com.emeraldblast.p6.ui.document.worksheet.cursor.state.CursorState
import com.emeraldblast.p6.ui.document.worksheet.resize_bar.ResizeBarState
import com.emeraldblast.p6.ui.document.worksheet.ruler.RulerState
import com.emeraldblast.p6.ui.document.worksheet.select_rect.SelectRectState
import com.emeraldblast.p6.ui.document.worksheet.slider.GridSlider
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


/**
 * a GridSlider + col/row limit
 */
data class WorksheetStateImp @AssistedInject constructor(
    @Assisted("1") override val wsMs: Ms<Worksheet>,
    @Assisted("2") override val sliderMs: Ms<GridSlider>,
    @Assisted("3") override val cursorStateMs: Ms<CursorState>,
    @Assisted("4")
    override val colRulerStateMs: Ms<RulerState>,
    @Assisted("5")
    override val rowRulerStateMs: Ms<RulerState>,


    //==========================================//

    @DefaultCellLayoutMap
    override val cellLayoutCoorMapMs: Ms<Map<CellAddress, LayoutCoorWrapper>>,
    @DefaultLayoutCoorMs
    override val cellGridLayoutCoorWrapperMs: Ms<LayoutCoorWrapper?>,
    @DefaultLayoutCoorMs
    override val wsLayoutCoorWrapperMs: Ms<LayoutCoorWrapper?>,
    @DefaultTopLeftCellAddress
    override val topLeftCell: CellAddress = CellAddress(1, 1),
    @DefaultCellStateContainer
    val cellStateContMs: Ms<CellStateContainer>,
    @DefaultSelectRectStateMs
    override val selectRectStateMs: Ms<SelectRectState>,
    @DefaultColResizeBarStateMs
    override val colResizeBarStateMs: Ms<ResizeBarState>,
    @DefaultRowResizeBarStateMs
    override val rowResizeBarStateMs: Ms<ResizeBarState>,
    @DefaultColRangeQualifier
    override val colRange: IntRange = R.worksheetValue.defaultColRange,
    @DefaultRowRangeQualifier
    override val rowRange: IntRange = R.worksheetValue.defaultRowRange,
) : AbstractWorksheetState() {

    override val idMs: St<WorksheetId> = derivedStateOf<WorksheetId> {
        WorksheetIdImp(
            wsNameMs = worksheet.nameMs,
            wbKeySt = worksheet.wbKeySt
        )
    }
    override val id: WorksheetId by idMs

    override fun addCellLayoutCoor(cellAddress: CellAddress, layoutCoor: LayoutCoorWrapper): WorksheetState {
        if (this.cellLayoutCoorMap[cellAddress] != layoutCoor) {
            val newMap = this.cellLayoutCoorMap + (cellAddress to layoutCoor)
            this.cellLayoutCoorMapMs.value = newMap
        }
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
        get() = this.id.wsNameSt

    override fun setSlider(i: GridSlider): WorksheetState {
        this.sliderMs.value = i
        return this
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

    override fun setTopLeftCell(c: CellAddress): WorksheetState {
        if (c == this.topLeftCell) {
            return this
        } else {
            return this.copy(topLeftCell = c)
        }
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

    override val name: String
        get() = this.idMs.value.wsName

    override fun setWsMs(wsMs: Ms<Worksheet>): WorksheetState {
        val newState= this.copy(wsMs = wsMs)
        val newWsStateIdSt = wsMs.value.idMs
        cursorState.id = cursorState.id.setWsStateIdSt(newWsStateIdSt)
        rowRulerStateMs.value = rowRulerState.setWsIdSt(newWsStateIdSt)
        colRulerStateMs.value = colRulerState.setWsIdSt(newWsStateIdSt)
        return newState
    }
}
