package com.qxdzbc.p6.composite_actions.window.close_wb_ui

import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey

/**
 * Check for workbook needSave status and open ask save prompt if needed
 */

interface CheckNeedSaveUIAction {
    fun checkNeedSave(wbKeySt:St<WorkbookKey>):Boolean
    fun checkNeedSave(wbKey:WorkbookKey):Boolean
}
