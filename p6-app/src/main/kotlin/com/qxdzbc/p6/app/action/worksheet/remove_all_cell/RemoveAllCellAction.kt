package com.qxdzbc.p6.app.action.worksheet.remove_all_cell

import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt

interface RemoveAllCellAction {
    fun removeAllCell(wbWsSt: WbWsSt):Rse<Unit>
    fun removeAllCell(wbWs: WbWs):Rse<Unit>
}
