package com.emeraldblast.p6.ui.window.workbook_tab.tab

import com.emeraldblast.p6.app.document.workbook.WorkbookKey

interface WorkbookTabState {
    val wbKey:WorkbookKey
    val isSelected:Boolean
    val needSave:Boolean
    val tabName:String
}
