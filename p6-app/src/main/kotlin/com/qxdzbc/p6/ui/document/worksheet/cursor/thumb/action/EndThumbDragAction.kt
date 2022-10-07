package com.qxdzbc.p6.ui.document.worksheet.cursor.thumb.action

import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.document.cell.address.CellAddress

/**
 * Action to perform when ending thumb drag
 */
interface EndThumbDragAction{
    fun invokeSuitableAction(wbws: WbWsSt, startCell: CellAddress, endCell: CellAddress, isCtrPressed: Boolean)
}
