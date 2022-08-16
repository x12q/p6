package com.emeraldblast.p6.ui.document.workbook.state

import androidx.compose.runtime.getValue
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.ui.common.compose.Ms

data class WorkbookStateIDImp constructor(
    override val wbKeyMs:Ms<WorkbookKey>,
) : WorkbookStateID {
    override val wbKey: WorkbookKey by wbKeyMs
    override fun setWbKey(key: WorkbookKey): WorkbookStateID {
        wbKeyMs.value = key
        return this
    }
}
