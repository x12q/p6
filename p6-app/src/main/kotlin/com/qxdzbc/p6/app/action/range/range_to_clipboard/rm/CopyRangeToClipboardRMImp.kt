package com.qxdzbc.p6.app.action.range.range_to_clipboard.rm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.app.action.range.range_to_clipboard.RangeToClipboardRequest
import com.qxdzbc.p6.app.action.range.range_to_clipboard.RangeToClipboardResponse2
import com.qxdzbc.p6.app.common.utils.RseNav
import com.qxdzbc.p6.app.common.err.ErrorReportWithNavInfo.Companion.withNav
import com.qxdzbc.p6.di.state.app_state.AppStateMs
import com.qxdzbc.p6.app.document.range.copy_paste.RangeCopier
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.common.compose.Ms
import com.github.michaelbull.result.*
import javax.inject.Inject

class CopyRangeToClipboardRMImp @Inject constructor(
    @AppStateMs private val appStateMs: Ms<AppState>,
    private val copier: RangeCopier,
) : CopyRangeToClipboardRM {

    private var appState by appStateMs

    override fun copyRangeToClipboard2(request: RangeToClipboardRequest): RseNav<RangeToClipboardResponse2> {
        val rt = appState
            .getRangeRs(request.rangeId)
            .flatMap { range ->
                val copyRs = this.copier.copyRange(range)
                copyRs
            }.map {
                RangeToClipboardResponse2(
                    range = it,
                    windowId = request.windowId
                )
            }.mapError {
                it.withNav(request)
            }
        return rt
    }

}
