package com.emeraldblast.p6.ui.app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.emeraldblast.p6.app.common.RseNav
import com.emeraldblast.p6.app.common.err.ErrorReportWithNavInfo
import com.emeraldblast.p6.di.state.app_state.AppStateMs
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.emeraldblast.p6.ui.app.state.AppState
import com.emeraldblast.p6.ui.common.compose.Ms
import com.github.michaelbull.result.Err
import javax.inject.Inject

class ErrorRouterImp @Inject constructor(
    @AppStateMs private val appStateMs: Ms<AppState>
) : ErrorRouter {
    private var appState by appStateMs
    private var codeEditorState by appState.codeEditorStateMs
    private var oddityContInCodeEditor by codeEditorState.oddityContainerMs

    override fun publishToApp(errorReport: ErrorReport?) {
        appState.oddityContainer = appState.oddityContainer.addErrorReport(errorReport)
    }

    override fun publishToScriptWindow(errorReport: ErrorReport?) {
        oddityContInCodeEditor = oddityContInCodeEditor.addErrorReport(errorReport)
    }

    override fun publishToWindow(errorReport: ErrorReport?, windowId: String?) {
        if (windowId != null) {
            val windowStateMs = appState.getWindowStateMsById(windowId)
            if (windowStateMs != null) {
//            val stackTrace = errorReport?.toException()?.stackTraceToString()?:""
                val er2 = errorReport

                windowStateMs.value.oddityContainer =
                    windowStateMs.value.oddityContainer.addErrorReport(er2)
            } else {
                publishToApp(errorReport)
            }
        } else {
            publishToApp(errorReport)
        }

    }

    override fun publishToWindow(errorReport: ErrorReport?, workbookKey: WorkbookKey?) {
        if (workbookKey != null) {
            val windowStateMs = appState.getWindowStateMsByWbKey(workbookKey)
            if (windowStateMs != null) {
                val ne = errorReport?.toException()?.stackTrace.toString()


                windowStateMs.value.oddityContainer =
                    windowStateMs.value.oddityContainer.addErrorReport(errorReport)
            } else {
                publishToApp(errorReport)
            }
        } else {
            publishToApp(errorReport)
        }
    }

    override fun publishToWindow(errorReport: ErrorReport?, windowId: String?, workbookKey: WorkbookKey?) {
        val windowStateMs = windowId?.let { appState.getWindowStateMsById(it) }
            ?: workbookKey?.let { appState.getWindowStateMsByWbKey(it) }
        if (windowStateMs != null) {
            windowStateMs.value.oddityContainer =
                windowStateMs.value.oddityContainer.addErrorReport(errorReport)
        } else {
            publishToApp(errorReport)
        }
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
