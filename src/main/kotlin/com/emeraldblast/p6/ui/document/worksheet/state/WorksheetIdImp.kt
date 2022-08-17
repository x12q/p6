package com.emeraldblast.p6.ui.document.worksheet.state

import androidx.compose.runtime.getValue
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.common.compose.St

data class WorksheetIdImp(
    override val wsNameMs: Ms<String>,
    override val wbKeyMs: St<WorkbookKey>,
) : WorksheetId {
    override val wsName: String by wsNameMs
    override fun pointToWsNameMs(wsNameMs: Ms<String>): WorksheetId {
        return this.copy(wsNameMs = wsNameMs)
    }

    override fun pointToWbKeyMs(wbKeyMs: St<WorkbookKey>): WorksheetId {
        return this.copy(wbKeyMs = wbKeyMs)
    }

    override val wbKey: WorkbookKey
        get() = wbKeyMs.value
}
