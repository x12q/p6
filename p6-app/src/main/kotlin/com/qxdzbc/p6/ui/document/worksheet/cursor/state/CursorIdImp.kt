package com.qxdzbc.p6.ui.document.worksheet.cursor.state

import androidx.compose.runtime.State
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetId

data class CursorIdImp(
    override val wsStateIDMs: State<WorksheetId>,
) : CursorId {
    override fun setWsStateIdSt(wsStateIDSt: State<WorksheetId>): CursorId {
        return this.copy(wsStateIDMs = wsStateIDSt)
    }

    override val wbKeySt: St<WorkbookKey>
        get() = wsStateIDMs.value.wbKeySt
    override val wsNameSt: St<String>
        get() = wsStateIDMs.value.wsNameMs
}
