package com.emeraldblast.p6.ui.document.worksheet.state

import androidx.compose.runtime.getValue
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.common.compose.St

data class WorksheetStateIdImp(
    override val wsNameMs: St<String>,
    override val wbKeyMs: St<WorkbookKey>,
) : WorksheetStateId {
    override val wsName: String by wsNameMs
    override fun pointToWsNameMs(wsNameMs: St<String>): WorksheetStateId {
        return this.copy(wsNameMs = wsNameMs)
    }

    override fun pointToWbKeyMs(wbKeyMs: St<WorkbookKey>): WorksheetStateId {
        return this.copy(wbKeyMs = wbKeyMs)
    }

    override val wbKey: WorkbookKey
        get() = wbKeyMs.value
}
