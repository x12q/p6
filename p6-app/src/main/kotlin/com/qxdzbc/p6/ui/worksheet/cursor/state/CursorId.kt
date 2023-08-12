package com.qxdzbc.p6.ui.worksheet.cursor.state

import androidx.compose.runtime.State
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.composite_actions.common_data_structure.WbWsSt
import com.qxdzbc.p6.ui.worksheet.state.WorksheetId

/**
 * For identifying a cursor
 */
interface CursorId : WbWsSt{
    val wsStateIDMs: St<WorksheetId>
    fun setWsStateIdSt(wsStateIDSt: State<WorksheetId>):CursorId
}
