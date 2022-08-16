package com.emeraldblast.p6.ui.app

import com.emeraldblast.p6.app.common.RseNav
import com.emeraldblast.p6.app.common.err.ErrorReportWithNavInfo
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.common.exception.error.ErrorReport

class ErrorRouterDoNothing : ErrorRouter {
    override fun toApp(errorReport: ErrorReport?) {
        println("do nothing")
    }

    override fun toScriptWindow(errorReport: ErrorReport?) {
        println("do nothing")
    }

    override fun toWindow(errorReport: ErrorReport?, windowId: String?) {
        println("do nothing")
    }

    override fun toWindow(errorReport: ErrorReport?, workbookKey: WorkbookKey?) {
        println("do nothing")
    }

    override fun toWindow(errorReport: ErrorReport?, windowId: String?, workbookKey: WorkbookKey?) {
        println("do nothing")
    }

    override fun publish(errorReport: ErrorReportWithNavInfo) {
        println("do nothing")
    }

    override fun <T> publishIfPossible(resNav: RseNav<T>) {
        TODO("Not yet implemented")
    }
}
