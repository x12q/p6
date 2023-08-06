package com.qxdzbc.p6.ui.app.error_router

import com.qxdzbc.p6.app.common.utils.RseNav
import com.qxdzbc.p6.app.common.err.ErrorReportWithNavInfo

import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.common.compose.Ms
import com.github.michaelbull.result.Err
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.app.err.ErrorContainer
import com.qxdzbc.p6.di.state.app_state.qualifiers.AppErrorContMs
import com.qxdzbc.p6.ui.app.state.StateContainer
import javax.inject.Inject

class ErrorRouterImp @Inject constructor(
    private val sc:StateContainer,
    @AppErrorContMs
    val errorContainerMs: Ms<ErrorContainer>,
) : ErrorRouter {

    override fun publishToApp(errorReport: ErrorReport?) {
        errorContainerMs.value = errorContainerMs.value.addErrorReport(errorReport)
    }

    override fun publishToWindow(errorReport: ErrorReport?, windowId: String?) {
        if (windowId != null) {
            val windowState = sc.getWindowStateMsById(windowId)
            if (windowState != null) {
//            val stackTrace = errorReport?.toException()?.stackTraceToString()?:""
                val er2 = errorReport

                windowState.errorContainer =
                    windowState.errorContainer.addErrorReport(er2)
            } else {
                publishToApp(errorReport)
            }
        } else {
            publishToApp(errorReport)
        }

    }

    override fun publishToWindow(errorReport: ErrorReport?, workbookKey: WorkbookKey?) {
        if (workbookKey != null) {
            val windowState = sc.getWindowStateMsByWbKey(workbookKey)
            if (windowState != null) {
//                val ne = errorReport?.toException()?.stackTrace.toString()


                windowState.errorContainer =
                    windowState.errorContainer.addErrorReport(errorReport)
            } else {
                publishToApp(errorReport)
            }
        } else {
            publishToApp(errorReport)
        }
    }

    override fun publishToWindow(errorReport: ErrorReport?, windowId: String?, workbookKey: WorkbookKey?) {
        val windowState = windowId?.let { sc.getWindowStateMsById(it) }
            ?: workbookKey?.let { sc.getWindowStateMsByWbKey(it) }
        if (windowState != null) {
            windowState.errorContainer =
                windowState.errorContainer.addErrorReport(errorReport)
        } else {
            publishToApp(errorReport)
        }
    }

    override fun publishToWindow(errorReport: ErrorReport?, windowId: String?, wbKeySt: St<WorkbookKey>?) {
        TODO("Not yet implemented")
    }

    override fun publish(errorReport: ErrorReportWithNavInfo) {
        this.publishToWindow(errorReport.errorReport, errorReport.windowId, errorReport.wbKey)
    }

    override fun <T> publishIfPossible(resNav: RseNav<T>) {
        if (resNav is Err) {
            this.publish(resNav.error)
        }
    }
}
