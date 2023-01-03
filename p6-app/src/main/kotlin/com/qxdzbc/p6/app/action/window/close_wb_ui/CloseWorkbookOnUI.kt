package com.qxdzbc.p6.app.action.window.close_wb_ui

import androidx.compose.runtime.Composable
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.document.workbook.WorkbookKey

/**
 * Check for workbook needSave status and open ask save prompt if needed
 */

interface CheckNeedSaveUIAction {
    fun checkNeedSave(wbKeySt:St<WorkbookKey>):Boolean
    fun checkNeedSave(wbKey:WorkbookKey):Boolean
}
