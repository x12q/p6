package com.qxdzbc.p6.ui.window.dialog.ask_to_save_dialog

import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.document.workbook.WorkbookKey

interface AskSaveDialogAction {
    fun onCancel(windowId: String)
    fun onSave(windowId: String, wbKeySt: St<WorkbookKey>)
    fun onDontSave(windowId: String)

    /**
     * Open AskSaveDialog on window at [windowId]
     */
    fun open(windowId: String)

    /**
     * close AskSaveDialog on window at [windowId]
     */
    fun close(windowId: String)
}
