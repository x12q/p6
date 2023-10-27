package com.qxdzbc.p6.ui.app.error_router

import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.common.utils.RseNav
import com.qxdzbc.p6.common.err.ErrorReportWithNavInfo
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
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
     * attempt to publish an error to window, if no window is available, publish the err to app
     */
    fun publishToWindow(errorReport: ErrorReport?, windowId:String?)
    fun publishToWindow(errorReport: ErrorReport?, workbookKey:WorkbookKey?)

    /**
     * Attempt to search for window state obj using both window id and workbook key
     */
    fun publishToWindow(errorReport: ErrorReport?, windowId:String?, workbookKey:WorkbookKey?)
    fun publishToWindow(errorReport: ErrorReport?, windowId:String?, wbKeySt:St<WorkbookKey>?)

    fun publish(errorReport: ErrorReportWithNavInfo)
    fun <T> publishIfPossible(resNav: RseNav<T>)
}
