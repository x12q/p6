package com.emeraldblast.p6.app.action.worksheet.action2

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.LayoutCoordinates
import com.emeraldblast.p6.app.action.common_data_structure.WithWbWs
import com.emeraldblast.p6.app.action.worksheet.release_focus.RestoreWindowFocusState
import com.emeraldblast.p6.app.document.cell.address.CellAddress
import com.emeraldblast.p6.app.document.range.address.RangeAddresses
import com.emeraldblast.p6.di.state.app_state.AppStateMs
import com.emeraldblast.p6.ui.app.state.AppState
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.common.compose.wrap
import com.emeraldblast.p6.ui.document.worksheet.cursor.state.CursorState
import com.emeraldblast.p6.ui.document.worksheet.ruler.RulerState
import com.emeraldblast.p6.ui.document.worksheet.state.WorksheetState
import javax.inject.Inject

class WorksheetAction2Imp @Inject constructor(
    @AppStateMs private val appStateMs: Ms<AppState>,
    private val restoreWindowFocusState: RestoreWindowFocusState,
) : WorksheetAction2 {
    private var appState by appStateMs

    override fun makeSliderFollowCursor(
        newCursor: CursorState,
        wbws: WithWbWs,
    ) {
        val wsStateMs: Ms<WorksheetState>? = appState.getWsStateMs(wbws)
        if(wsStateMs!=null){

            val oldSlider = wsStateMs.value.slider
            val newSlider = oldSlider.move(newCursor)
            if (newSlider != oldSlider) {
                val colRulerStateMs: Ms<RulerState> = wsStateMs.value.colRulerStateMs
                val rowRulerStateMs: Ms<RulerState> = wsStateMs.value.rowRulerStateMs
                val wsState by wsStateMs
                val colRulerState by colRulerStateMs
                val rowRulerState by rowRulerStateMs

                wsStateMs.value = wsState.setTopLeftCell(newSlider.topLeftCell).setSlider(newSlider)
                // x: clear all cached layout coors whenever slider moves to prevent memory from overflowing.
                this.removeAllCellLayoutCoor(wsState)

                colRulerStateMs.value = colRulerState
                    .clearItemLayoutCoorsMap()
                    .clearResizerLayoutCoorsMap()

                rowRulerStateMs.value = rowRulerState
                    .clearItemLayoutCoorsMap()
                    .clearResizerLayoutCoorsMap()
            }
        }
    }

    override fun scroll(x: Int, y: Int, wsState: WorksheetState) {
        val wsStateMs = appState.getWsStateMs(
            wsState.wbKey, wsState.name
        )
        if (wsStateMs != null) {
            val sliderState = wsState.slider
            var newSlider = sliderState
            if (x != 0) {
                newSlider = newSlider.shiftRight(x)
            }
            if (y != 0) {
                newSlider = newSlider.shiftDown(y)
            }
            if (newSlider != sliderState) {
                wsStateMs.value = wsState.setTopLeftCell(newSlider.topLeftCell).setSlider(newSlider)
                wsState.cellLayoutCoorMapMs.value =
                    wsState.cellLayoutCoorMap.filter { (cellAddress, _) -> sliderState.containAddress(cellAddress) }
            }
        }
    }

    /**
     * when click on a cell, remove all previous selection, then assign the clicked cell as the new anchor cell
     */
    override fun clickOnCell(cellAddress: CellAddress, cursorStateMs: Ms<CursorState>) {
        var cursorState by cursorStateMs
        var cellEditorState by cursorState.cellEditorStateMs
        if (cellEditorState.isActive) {
            if (!cellEditorState.allowRangeSelector) {
                cellEditorState = cellEditorState.clearAllText().close()
                cursorState = cursorState
                    .removeMainRange()
                    .removeAllFragmentedCells()
                    .removeAllSelectedFragRange()
                    .setAnchorCell(cellAddress)
            } else {
                //do nothing
            }
        } else {
            cursorState = cursorState
                .removeMainRange()
                .removeAllFragmentedCells()
                .removeAllSelectedFragRange()
                .setAnchorCell(cellAddress)
        }
    }


    override fun stopDragSelection(wsState: WorksheetState) {
        var selectRect by wsState.selectRectStateMs
        selectRect = selectRect.hide().deactivate()
    }

    override fun makeMouseDragSelectionIfPossible(
        wsState: WorksheetState,
        currentCellMouseOn: CellAddress,
    ) {
        var cursorState by wsState.cursorStateMs
        val newRange = RangeAddresses.fromManyCells(listOf(currentCellMouseOn, cursorState.mainCell))
        val newCursorState = cursorState.setMainRange(newRange)
        cursorState = newCursorState
    }

    override fun makeMouseDragSelectionIfPossible(wsState: WorksheetState, mousePosition: Offset, offset: Offset) {
        var selectRect by wsState.selectRectStateMs
        var cursorState by wsState.cursorStateMs
        if (selectRect.isActive) {
            selectRect = selectRect.setMovingPoint(mousePosition).show()
            val currentCellMouseOn = wsState.cellLayoutCoorMap.entries.firstOrNull { (_, layout) ->
                val cellRect = layout.boundInWindow
                cellRect.contains(mousePosition)
            }?.key
            if (currentCellMouseOn != null) {
                makeMouseDragSelectionIfPossible(wsState,currentCellMouseOn)
            }
        }
    }

    override fun startDragSelection(
        wsState: WorksheetState,
        anchorCell: CellAddress
    ) {
        var cursorState by wsState.cursorStateMs
        val cellEditorState by cursorState.cellEditorStateMs
        restoreWindowFocusState.setFocusStateConsideringRangeSelector(wsState.wbKey)
        val c1 = cellEditorState.isActive && !cellEditorState.allowRangeSelector
        if (c1) {
            return
        } else {
            cursorState = cursorState
                .setAnchorCell(anchorCell)
                .removeMainRange()
                .removeAllSelectedFragRange()
                .removeAllFragmentedCells()
        }
    }

    override fun startDragSelection(
        wsState: WorksheetState,
        mousePosition: Offset, offset: Offset,

        ) {
        var selectRect by wsState.selectRectStateMs
        selectRect = selectRect
            .setAnchorPoint(mousePosition)
            .activate()
        for ((cellAddress, layout) in wsState.cellLayoutCoorMap) {
            val cellRect = layout.boundInWindow
            if (cellRect.contains(selectRect.anchorPoint)) {
                startDragSelection(wsState, cellAddress)
                break
            }
        }

    }

    override fun addCellLayoutCoor(
        cellAddress: CellAddress,
        layoutCoordinates: LayoutCoordinates,
        wsState: WorksheetState
    ) {
        val wsStateMs = appState.getWsStateMs(
            wsState.wbKey, wsState.name
        )
        if (wsStateMs != null) {
            wsStateMs.value = wsState
                .addCellLayoutCoor(cellAddress, layoutCoordinates.wrap())
        }
    }

    override fun removeCellLayoutCoor(cellAddress: CellAddress, wsState: WorksheetState) {
        val wsStateMs = appState.getWsStateMs(
            wsState.wbKey, wsState.name
        )
        wsStateMs?.also {
            wsStateMs.value = wsState
                .removeCellLayoutCoor(cellAddress)
        }

    }

    override fun removeAllCellLayoutCoor(wsState: WorksheetState) {
        val wsStateMs = appState.getWsStateMs(
            wsState.wbKey, wsState.name
        )
        wsStateMs?.also {
            it.value = wsState
                .removeAllCellLayoutCoor()
        }

    }


    override fun ctrlClickSelectCell(cellAddress: CellAddress, wsState: WorksheetState) {
        var cursorState by wsState.cursorStateMs
        if (cellAddress != cursorState.mainCell) {
            if (cursorState.isPointingTo(cellAddress)) {
                var newCursor = cursorState
                if (cellAddress in newCursor.fragmentedCells) {
                    newCursor = newCursor.removeFragCell(cellAddress)
                }
                if (newCursor.mainRange?.contains(cellAddress) == true) {
                    val brokenRanges = newCursor.mainRange?.removeCell(cellAddress) ?: emptyList()
                    newCursor = newCursor
                        .removeMainRange()
                        .addFragRanges(brokenRanges)
                }
                for (range in newCursor.fragmentedRanges) {
                    val brokenRanges = range.removeCell(cellAddress)
                    newCursor = newCursor
                        .removeFragRange(range)
                        .addFragRanges(brokenRanges)
                }
                cursorState = newCursor
            } else {
                cursorState = cursorState.addFragCell(cellAddress)
            }
        }
    }

    override fun shiftClickSelectRange(cellAddress: CellAddress, wsState: WorksheetState) {
        var cursorState by wsState.cursorStateMs
        if (cellAddress != cursorState.mainCell) {
            cursorState = cursorState.setMainRange(RangeAddresses.from2Cells(cellAddress, cursorState.mainCell))
        }
    }

    override fun updateCellGridLayoutCoors(newLayoutCoordinates: LayoutCoordinates, wsState: WorksheetState) {
        val wsStateMs = appState.getWsStateMs(
            wsState.wbKey, wsState.name
        )
        wsStateMs?.also {
            it.value = wsState
                .setCellGridLayoutCoorWrapper(newLayoutCoordinates.wrap())
        }

    }

    override fun updateWsLayoutCoors(newLayoutCoordinates: LayoutCoordinates, wsState: WorksheetState) {
        val wsStateMs = appState.getWsStateMs(
            wsState.wbKey, wsState.name
        )
        wsStateMs?.also {
            it.value = wsState
                .setwsLayoutCoorWrapper(newLayoutCoordinates.wrap())
        }

    }

    override fun determineSlider(wsState: WorksheetState) {
        val currentSlider = wsState.slider
        val availableSize = wsState.cellGridLayoutCoorWrapper?.size
        val newSlider = if (availableSize != null) {
            val limitWidth = availableSize.width.value
            val limitHeight = availableSize.height.value

            val fromCol = wsState.topLeftCell.colIndex
            var toCol = fromCol
            var accumWidth = 0F
            while (accumWidth < limitWidth) {
                accumWidth += wsState.getColumnWidthOrDefault(toCol)
                toCol += 1
            }

            val fromRow = wsState.topLeftCell.rowIndex
            var toRow = fromRow
            var accumHeight = 0F
            while (accumHeight < limitHeight) {
                accumHeight += wsState.getRowHeightOrDefault(toRow)
                toRow += 1
            }

            val newSlider = currentSlider
                .setVisibleRowRange(fromRow..maxOf(toRow - 1, fromRow))
                .setVisibleColRange(fromCol..maxOf(toCol - 1, fromCol))
            newSlider
        } else {
            null
        }
        if (newSlider != null) {
            wsState.sliderMs.value = newSlider
        }
    }
}
