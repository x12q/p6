package com.qxdzbc.p6.ui.document.worksheet.cursor.thumb

import androidx.compose.ui.geometry.Offset
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.document.cell.address.CellAddress

interface DragThumbAction {
    fun startDrag(wbws: WbWs, cellAddress: CellAddress)
    fun startDrag(wbws: WbWs, mouseWindowOffset:Offset)

    fun drag(wbws: WbWs, cellAddress: CellAddress)
    fun drag(wbws: WbWs, mouseWindowOffset:Offset)

    fun endDrag(wbws: WbWs, cellAddress: CellAddress)
    fun endDrag(wbws: WbWs, mouseWindowOffset:Offset)
}
