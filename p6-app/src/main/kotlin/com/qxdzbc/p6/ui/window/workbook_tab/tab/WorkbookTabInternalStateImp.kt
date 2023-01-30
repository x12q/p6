package com.qxdzbc.p6.ui.window.workbook_tab.tab

data class WorkbookTabInternalStateImp(
    override val mouseOnTab: Boolean = false,
    override val openAskToSaveDialog: Boolean = false
) : WorkbookTabInternalState {
    override fun setMouseOnTab(i: Boolean): WorkbookTabInternalStateImp {
        return this.copy(mouseOnTab = i)
    }

    override fun setOpenAskToSaveDialog(i: Boolean): WorkbookTabInternalStateImp {
        return this.copy(openAskToSaveDialog = i)
    }
}
