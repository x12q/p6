package com.qxdzbc.p6.ui.document.worksheet.ruler.actions

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.LayoutCoordinates
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerState
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerType

interface RulerAction {
    fun clickRulerItem(itemIndex: Int, wbwsSt:WbWsSt,type:RulerType)
    fun changeColWidth(colIndex: Int, sizeDiff: Float, rulerState: RulerState)
    fun changeRowHeight(rowIndex: Int, sizeDiff: Float, rulerState: RulerState)
    fun showColResizeBarThumb(index: Int, rulerState: RulerState)
    fun hideColResizeBarThumb(rulerState: RulerState)
    fun startColResizing(currentPos: Offset, rulerState: RulerState)
    fun moveColResizer(currentPos: Offset, rulerState: RulerState)
    fun finishColResizing(colIndex: Int, rulerState: RulerState)
    fun showRowResizeBarThumb(index: Int, rulerState: RulerState)
    fun hideRowResizeBarThumb(rulerState: RulerState)
    fun startRowResizing(currentPos: Offset, rulerState: RulerState)
    fun moveRowResizer(currentPos: Offset, rulerState: RulerState)
    fun finishRowResizing(rowIndex: Int, rulerState: RulerState)
    fun startDragSelection(mousePosition: Offset, rulerState: RulerState)
//    fun startDragSelection(itemIndex:Int, rulerState: RulerState)
    fun makeMouseDragSelectionIfPossible(mousePosition: Offset, rulerState: RulerState)
    fun stopDragSelection(rulerState: RulerState)
    fun updateItemLayout(itemIndex: Int, itemLayout: LayoutCoorWrapper, rulerState: RulerState)
    fun updateRulerLayout(layout: LayoutCoordinates, rulerState: RulerState)
    fun shiftClick(itemIndex: Int, rulerState: RulerState)
    fun ctrlClick(itemIndex: Int, rulerState: RulerState)
    fun updateResizerLayout(itemIndex: Int, layout: LayoutCoordinates, rulerState: RulerState)
}

