package com.emeraldblast.p6.ui.app

import com.emeraldblast.p6.app.common.RseNav
import com.emeraldblast.p6.app.common.err.ErrorReportWithNavInfo
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.common.exception.error.ErrorReport

/**
 * moves errors to the correct place.
 */
interface ErrorRouter {
    /**
     * move an error to app
     */
    fun toApp(errorReport: ErrorReport?)

    /**
     * route an error report to script editor window
     */
    fun toScriptWindow(errorReport: ErrorReport?)

    /**
     * attempt to move an error to window
     */
    fun toWindow(errorReport: ErrorReport?, windowId:String?)
    fun toWindow(errorReport: ErrorReport?, workbookKey:WorkbookKey?)

    /**
     * Attempt to search for window state obj using both window id and workbook key
     */
    fun toWindow(errorReport: ErrorReport?, windowId:String?,workbookKey:WorkbookKey?)
    fun publish(errorReport:ErrorReportWithNavInfo)
    fun <T> publishIfPossible(resNav:RseNav<T>)
}
