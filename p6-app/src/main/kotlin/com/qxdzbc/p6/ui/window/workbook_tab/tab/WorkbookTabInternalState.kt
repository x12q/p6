package com.qxdzbc.p6.ui.window.workbook_tab.tab

interface WorkbookTabInternalState {
    val mouseOnTab: Boolean
    fun setMouseOnTab(i: Boolean): WorkbookTabInternalState
    val openAskToSaveDialog: Boolean
    fun setOpenAskToSaveDialog(i: Boolean): WorkbookTabInternalState
}
