package com.emeraldblast.p6.ui.document.worksheet.cursor.state

import androidx.compose.runtime.State
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.ui.common.compose.St
import com.emeraldblast.p6.ui.document.worksheet.state.WorksheetId

data class CursorIdImp(
    override val wsStateIDMs: State<WorksheetId>,
) : CursorStateId {
    override fun setWsStateIdSt(wsStateIDSt: State<WorksheetId>): CursorStateId {
        return this.copy(wsStateIDMs = wsStateIDSt)
    }

    override val wbKeySt: St<WorkbookKey>
        get() = wsStateIDMs.value.wbKeySt
    override val wsNameSt: St<String>
        get() = wsStateIDMs.value.wsNameMs
}
