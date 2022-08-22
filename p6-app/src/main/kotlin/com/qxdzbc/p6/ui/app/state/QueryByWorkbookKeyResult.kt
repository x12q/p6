package com.qxdzbc.p6.ui.app.state

import com.qxdzbc.p6.app.oddity.OddityContainer
import com.qxdzbc.p6.app.oddity.addError
import com.qxdzbc.p6.common.exception.error.ErrorReport
import com.qxdzbc.p6.ui.common.compose.Ms
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState
import com.qxdzbc.p6.ui.window.state.WindowState
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.contracts.contract
class QueryByWorkbookKeyResult2(
    val windowStateMs: Ms<WindowState>,
    val workbookStateMs: Ms<WorkbookState>,
) {
}

/**
 * This class is an encapsulation that contains information related to a workbook key.
 * [workbookStateMs] is the workbook the key is pointing to, null if the key is invalid.
 * [windowStateMs] the window in which the workbook locates, null if the key is invalid.
 * [oddityContainerMs] the relavant oddity container. It is the window's oddity container if the key is valid; otherwise it is the app's oddity container.
 * [ErrorReport] gives detail the error (if any) when doing query.
 */
class QueryByWorkbookKeyResult(
    val windowStateOrNull: Ms<WindowState>? = null,
    val workbookStateMsOrNull: Ms<WorkbookState>? = null,
    val oddityContainerMs: Ms<OddityContainer>,
    private val _errorReport: ErrorReport? = null,
) {
    val windowStateMs get() = this.windowStateOrNull!!
    val windowState get() = windowStateMs.value
    val workbookStateMs get() = this.workbookStateMsOrNull!!
    val workbookState get()=workbookStateMs.value
    val errorReport get() = this._errorReport!!
    val isOk: Boolean get() = this._errorReport == null && windowStateOrNull != null && workbookStateMsOrNull != null

    /**
     * Add the query error into the oddity container of this result
     */
    fun publishError() {
        if (!this.isOk) {
            this.oddityContainerMs.addError(this.errorReport)
        }
    }

    /**
     * Execute [f] if this result is ok, publish the error otherwise
     */
    fun ifOk(f:(QueryByWorkbookKeyResult)->Unit):QueryByWorkbookKeyResult {
        if (this.isOk) {
            f(this)
        } else {
            this.publishError()
        }
        return this
    }
}
