package com.qxdzbc.p6.composite_actions.cursor.thumb.drag_thumb_action

import androidx.compose.ui.geometry.Offset
import com.qxdzbc.p6.composite_actions.common_data_structure.WbWsSt
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress

/**
 * Action for the thumb of a ws cursor.
 */
interface DragThumbAction {
    fun startDrag_forTest(wbws: WbWsSt, cellAddress: CellAddress)
    fun startDrag(wbws: WbWsSt, mouseWindowOffset:Offset)

    fun drag_forTest(wbws: WbWsSt, cellAddress: CellAddress)
    fun drag(wbws: WbWsSt, mouseWindowOffset:Offset)

    fun endDrag_forTest(wbws: WbWsSt, cellAddress: CellAddress,isCtrPressed:Boolean)
    fun endDrag(wbws: WbWsSt, mouseWindowOffset:Offset, isCtrPressed:Boolean)
}
