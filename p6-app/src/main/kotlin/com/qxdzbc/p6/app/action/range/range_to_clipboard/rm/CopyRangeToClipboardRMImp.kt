package com.qxdzbc.p6.app.action.range.range_to_clipboard.rm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.app.action.range.range_to_clipboard.RangeToClipboardRequest
import com.qxdzbc.p6.app.action.range.range_to_clipboard.RangeToClipboardResponse2
import com.qxdzbc.p6.app.common.utils.RseNav
import com.qxdzbc.p6.app.common.err.ErrorReportWithNavInfo.Companion.withNav

import com.qxdzbc.p6.app.document.range.copy_paste.RangeCopier
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.common.compose.Ms
import com.github.michaelbull.result.*
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.DocumentContainer
import com.qxdzbc.p6.ui.app.state.SubAppStateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class CopyRangeToClipboardRMImp @Inject constructor(
    private val copier: RangeCopier,
    val stateContMs: Ms<SubAppStateContainer>,
    val docContMs:Ms<DocumentContainer>,
) : CopyRangeToClipboardRM {

    private var dc by docContMs

    override fun copyRangeToClipboard2(request: RangeToClipboardRequest): RseNav<RangeToClipboardResponse2> {
        val rt = dc
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
