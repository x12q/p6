package com.emeraldblast.p6.ui.document.worksheet.cursor.state

import androidx.compose.runtime.State
import com.emeraldblast.p6.app.action.common_data_structure.WbWsSt
import com.emeraldblast.p6.ui.document.worksheet.state.WorksheetId

/**
 * For identifying a cursor
 */
interface CursorStateId : WbWsSt{
    val wsStateIDMs: State<WorksheetId>
    fun setWsStateIdSt(wsStateIDSt: State<WorksheetId>):CursorStateId
    fun isSame(another:CursorStateId):Boolean{
        val c1=this.wbKey == another.wbKey
        val c2 = this.wsName == another.wsName
        return c1 && c2
    }
}
