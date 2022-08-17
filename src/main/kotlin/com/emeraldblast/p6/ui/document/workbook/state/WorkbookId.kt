package com.emeraldblast.p6.ui.document.workbook.state

import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.ui.common.compose.Ms

/**
 * contain information that can be used to querying workbooks and workbook states from the central app state
 */
interface WorkbookId {
    val wbKeyMs:Ms<WorkbookKey>
    val wbKey: WorkbookKey
    fun setWbKey(key:WorkbookKey): WorkbookId
}
