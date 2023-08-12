package com.qxdzbc.p6.ui.window.workbook_tab.tab

import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey

interface WorkbookTabState {
    val wbKey:WorkbookKey
    val isSelected:Boolean
    val needSave:Boolean
    val tabName:String
}
