package com.qxdzbc.p6.composite_actions.cursor.thumb.drag_thumb_action

import com.qxdzbc.p6.composite_actions.common_data_structure.WbWsSt
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress

/**
 * Action to perform when end dragging on a ws cursor thumb.
 */
interface EndThumbDragAction{
    fun onEndDrag(wbws: WbWsSt, startCell: CellAddress, endCell: CellAddress, isCtrPressed: Boolean)
}
