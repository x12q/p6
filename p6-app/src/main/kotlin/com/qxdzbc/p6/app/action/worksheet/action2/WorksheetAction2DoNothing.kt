package com.qxdzbc.p6.app.action.worksheet.action2

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.LayoutCoordinates
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.ui.common.compose.Ms
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerState
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetState

//open class WorksheetAction2DoNothing : WorksheetAction2 {
//    override fun scroll(x: Int, y: Int, wsState: WorksheetState) {
//        TODO("Not yet implemented")
//    }
//
//
//    override fun clickOnCell(cellAddress: CellAddress) {
//        println("Do nothing")
//    }
//
//    override fun closeFormulaEditor() {
//        println("Do nothing")
//    }
//
//    override fun startDragSelection(mousePosition: Offset, offset: Offset) {
//        println("Do nothing")
//    }
//
//    override fun makeMouseDragSelectionIfPossible(mousePosition: Offset, offset: Offset) {
//        println("Do nothing")
//    }
//
//    override fun stopDragSelection() {
//        println("Do nothing")
//    }
//
//    override fun addCellLayoutCoor(cellAddress: CellAddress, layoutCoordinates: LayoutCoordinates) {
//        println("Do nothing")
//    }
//
//    override fun removeCellLayoutCoor(cellAddress: CellAddress) {
//        println("Do nothing")
//    }
//
//    override fun removeAllCellLayoutCoor() {
//        println("Do nothing")
//    }
//
//    override fun ctrlClickSelectCell(cellAddress: CellAddress) {
//        println("Do nothing")
//    }
//
//    override fun shiftClickSelectRange(cellAddress: CellAddress) {
//        println("Do nothing")
//    }
//
//    override fun updateCellGridLayoutCoors(newLayoutCoordinates: LayoutCoordinates) {
//        println("Do nothing")
//    }
//
//    override fun updateWsLayoutCoors(newLayoutCoordinates: LayoutCoordinates) {
//        println("Do nothing")
//    }
//
//    override fun determineSlider() {
//        println("Do nothing")
//    }
//
//    override fun makeSliderFollowCursor(
//        newCursor: CursorState,
//        wsStateMs: Ms<WorksheetState>,
//        colRulerStateMs: Ms<RulerState>,
//        rowRulerStateMs: Ms<RulerState>
//    ) {
//        TODO("Not yet implemented")
//    }
//}
