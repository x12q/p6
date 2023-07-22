package com.qxdzbc.p6.ui.window.workbook_tab.tab

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms

data class WorkbookTabInternalStateImp(
    val mouseOnTabMs: Ms<Boolean> = ms(false),
    val openAskToSaveDialogMs: Ms<Boolean> = ms(false),
) : WorkbookTabInternalState {

    override var mouseOnTab: Boolean by mouseOnTabMs
    override var openAskToSaveDialog: Boolean by openAskToSaveDialogMs
}
