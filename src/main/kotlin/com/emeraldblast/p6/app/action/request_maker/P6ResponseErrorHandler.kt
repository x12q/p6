package com.emeraldblast.p6.app.action.request_maker

import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.message.api.connection.service.zmq_services.msg.P6Response

/**
 * An error handler for [P6Response] specifically.
 * This class simply extracts error report from a [P6Response], then publishes it
 */
interface P6ResponseErrorHandler {

    fun publishErrResponseOnScriptWindow(p6Response: P6Response)
    /**
     * handle the error on a window that can be queried using [workbookKey]
     */
    fun publishErrResponseOnWindow(p6Res: P6Response, workbookKey: WorkbookKey?)

    /**
     * handle the error on a window that can be queried using either [windowId] or [workbookKey]
     */
    fun publishErrResponseOnWindow(p6Res: P6Response, windowId: String?, workbookKey: WorkbookKey?)

    /**
     * handle the error on a window that can be queried using [windowId]
     */
    fun publishErrResponseOnWindow(p6Res: P6Response, windowId: String?)

    /**
     * handle the error on the app
     */
    fun publishErrResponseOnApp(p6Res: P6Response)

}

