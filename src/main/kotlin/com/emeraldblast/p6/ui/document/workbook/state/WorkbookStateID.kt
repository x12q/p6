package com.emeraldblast.p6.ui.document.workbook.state

import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.ui.common.compose.Ms

/**
 * TODO the use of this class can be retired
 */
interface WorkbookStateID {
    val wbKeyMs:Ms<WorkbookKey>
    val wbKey: WorkbookKey
    fun setWbKey(key:WorkbookKey): WorkbookStateID
}
