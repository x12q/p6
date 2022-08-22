package com.qxdzbc.p6.ui.app.action

import com.qxdzbc.p6.app.action.app.set_wbkey.SetWorkbookKeyAction

/**
 * Action for use in app view
 */
interface AppAction {
    fun exitApp()
    fun closeCodeEditor()
    fun openCodeEditor()
}
