package com.emeraldblast.p6.app.action.worksheet.action2

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.LayoutCoordinates
import com.emeraldblast.p6.app.document.cell.address.CellAddress
import com.emeraldblast.p6.ui.document.worksheet.cursor.state.CursorState

interface WorksheetAction2Inside {


    fun scroll(x:Int,y:Int)

    fun clickOnCell(cellAddress: CellAddress)

    fun closeFormulaEditor()

    fun startDragSelection(mousePosition: Offset, offset: Offset = Offset(0F, 0F))
    fun makeMouseDragSelectionIfPossible(mousePosition: Offset, offset: Offset = Offset(0F, 0F))
    fun stopDragSelection()

    fun addCellLayoutCoor(cellAddress: CellAddress, layoutCoordinates: LayoutCoordinates)
    fun removeCellLayoutCoor(cellAddress: CellAddress)
    fun removeAllCellLayoutCoor()

    fun ctrlClickSelectCell(cellAddress: CellAddress)
    fun shiftClickSelectRange(cellAddress: CellAddress)

    fun updateCellGridLayoutCoors(newLayoutCoordinates: LayoutCoordinates)
    fun updateWsLayoutCoors(newLayoutCoordinates: LayoutCoordinates)

    /**
     * Compute the slider from the currently available space
     */
    fun determineSlider()
    fun makeSliderFollowCursor(newCursor: CursorState, )
}
