package com.qxdzbc.p6.composite_actions.worksheet.remove_all_cell

import com.qxdzbc.common.Rse
import com.qxdzbc.p6.composite_actions.common_data_structure.WbWs
import com.qxdzbc.p6.composite_actions.common_data_structure.WbWsSt

interface RemoveAllCellAction {
    fun removeAllCell(wbWsSt: WbWsSt):Rse<Unit>
    fun removeAllCell(wbWs: WbWs):Rse<Unit>
}
