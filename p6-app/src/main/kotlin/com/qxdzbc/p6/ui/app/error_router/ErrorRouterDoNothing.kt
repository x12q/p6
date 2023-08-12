package com.qxdzbc.p6.ui.app.error_router

import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.common.utils.RseNav
import com.qxdzbc.p6.common.err.ErrorReportWithNavInfo
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import com.qxdzbc.common.error.ErrorReport

class ErrorRouterDoNothing : ErrorRouter {
    override fun publishToApp(errorReport: ErrorReport?) {
        println("do nothing")
    }

    override fun publishToWindow(errorReport: ErrorReport?, windowId: String?) {
        println("do nothing")
    }

    override fun publishToWindow(errorReport: ErrorReport?, workbookKey: WorkbookKey?) {
        println("do nothing")
    }

    override fun publishToWindow(errorReport: ErrorReport?, windowId: String?, workbookKey: WorkbookKey?) {
        println("do nothing")
    }

    override fun publishToWindow(errorReport: ErrorReport?, windowId: String?, wbKeySt: St<WorkbookKey>?) {
        println("do nothing")
    }

    override fun publish(errorReport: com.qxdzbc.p6.common.err.ErrorReportWithNavInfo) {
        println("do nothing")
    }

    override fun <T> publishIfPossible(resNav: RseNav<T>) {
        TODO("Not yet implemented")
    }
}
