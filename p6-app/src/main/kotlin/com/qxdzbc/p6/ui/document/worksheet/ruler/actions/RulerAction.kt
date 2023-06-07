package com.qxdzbc.p6.ui.document.worksheet.ruler.actions

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.LayoutCoordinates
import com.qxdzbc.common.compose.density_converter.FloatToDpConverter
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.action.worksheet.ruler.change_col_row_size.ChangeRowAndColumnSizeAction
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerSig
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerState

interface RulerAction : ChangeRowAndColumnSizeAction {
    fun clickRulerItem(itemIndex: Int, rulerSig: RulerSig)
    fun showColResizeBarThumb(index: Int, wbwsSt: WbWsSt)
    fun hideColResizeBarThumb(wbwsSt: WbWsSt)
    fun startColResizing(currentPos: Offset, wbwsSt: WbWsSt)
    fun moveColResizer(currentPos: Offset, wbwsSt: WbWsSt)
    fun finishColResizing(colIndex: Int, wbwsSt: WbWsSt,converter: FloatToDpConverter)
    fun showRowResizeBarThumb(index: Int, wbwsSt: WbWsSt)
    fun hideRowResizeBarThumb(wbwsSt: WbWsSt)
    fun startRowResizing(currentPos: Offset, wbwsSt: WbWsSt)
    fun moveRowResizer(currentPos: Offset, wbwsSt: WbWsSt)
    fun finishRowResizing(rowIndex: Int, wbwsSt: WbWsSt,converter: FloatToDpConverter)
    fun startDragSelection(mousePosition: Offset, rulerSig: RulerSig)
//    fun startDragSelection(itemIndex:Int, rulerState: RulerState)
    fun makeMouseDragSelectionIfPossible(mousePosition: Offset, rulerSig: RulerSig)
    fun stopDragSelection(rulerSig: RulerSig)
    fun updateItemLayout(itemIndex: Int, itemLayout: LayoutCoorWrapper, rulerSig: RulerSig)
    fun updateRulerLayout(layout: LayoutCoordinates, rulerSig: RulerSig)
    fun shiftClick(itemIndex: Int, rulerSig: RulerSig)
    fun ctrlClick(itemIndex: Int, rulerSig: RulerSig)
    fun updateResizerLayout(itemIndex: Int, layout: LayoutCoordinates, rulerState: RulerState)
}

