package com.qxdzbc.p6.ui.app

import com.qxdzbc.p6.app.common.utils.RseNav
import com.qxdzbc.p6.app.common.err.ErrorReportWithNavInfo
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.common.error.ErrorReport

/**
 * moves errors to the correct place.
 */
interface ErrorRouter {
    /**
     * move an error to app
     */
    fun publishToApp(errorReport: ErrorReport?)

    /**
     * route an error report to script editor window
     */
    fun publishToScriptWindow(errorReport: ErrorReport?)

    /**
     * attempt to move an error to window
     */
    fun publishToWindow(errorReport: ErrorReport?, windowId:String?)
    fun publishToWindow(errorReport: ErrorReport?, workbookKey:WorkbookKey?)

    /**
     * Attempt to search for window state obj using both window id and workbook key
     */
    fun publishToWindow(errorReport: ErrorReport?, windowId:String?, workbookKey:WorkbookKey?)
    fun publish(errorReport: ErrorReportWithNavInfo)
    fun <T> publishIfPossible(resNav: RseNav<T>)
}
