package com.qxdzbc.p6.app.action.range.range_to_clipboard.applier

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.action.range.RangeId
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class RangeToClipboardInternalApplierImp @Inject constructor(
    val appStateMs: Ms<AppState>,
) : RangeToClipboardInternalApplier {
    var appState by appStateMs
    override fun apply(rangeId: RangeId, windowId: String?) {
        // find the correct cursor state ms
        val cursorStateMs = appState
            .getWbState(rangeId.wbKey)
            ?.getWsState(rangeId.wsName)
            ?.cursorStateMs
        if(cursorStateMs!=null){
            cursorStateMs.value =cursorStateMs.value.setClipboardRange(
                rangeId.rangeAddress
            )
        }
    }
}
