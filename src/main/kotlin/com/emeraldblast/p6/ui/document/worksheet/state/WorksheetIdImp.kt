package com.emeraldblast.p6.ui.document.worksheet.state

import androidx.compose.runtime.getValue
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.common.compose.St

data class WorksheetIdImp(
    override val wsNameMs: Ms<String>,
    override val wbKeySt: St<WorkbookKey>,
) : WorksheetId {
    override val wsName: String by wsNameMs
    override val wsNameSt: St<String>
        get() = wsNameMs

    override fun pointToWsNameMs(wsNameMs: Ms<String>): WorksheetId {
        return this.copy(wsNameMs = wsNameMs)
    }

    override fun pointToWbKeySt(wbKeyMs: St<WorkbookKey>): WorksheetId {
        return this.copy(wbKeySt = wbKeyMs)
    }

    override val wbKey: WorkbookKey
        get() = wbKeySt.value
}
