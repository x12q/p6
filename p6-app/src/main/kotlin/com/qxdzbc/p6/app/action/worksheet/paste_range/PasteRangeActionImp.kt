package com.qxdzbc.p6.app.action.worksheet.paste_range

import androidx.compose.runtime.getValue
import com.github.michaelbull.result.Ok
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.range.RangeIdImp
import com.qxdzbc.p6.app.action.range.paste_range.PasteRangeRequest2
import com.qxdzbc.p6.app.action.range.paste_range.applier.PasteRangeApplier
import com.qxdzbc.p6.app.action.range.paste_range.rm.PasteRangeRM
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.di.state.app_state.StateContainerSt
import com.qxdzbc.p6.ui.app.state.StateContainer
import javax.inject.Inject


class PasteRangeActionImp @Inject constructor(
    @StateContainerSt
    private val stateContSt: St<@JvmSuppressWildcards StateContainer>,
    private val rangeRM: PasteRangeRM,
    private val rangeApplier: PasteRangeApplier,
) : PasteRangeAction {
    private val stateCont by stateContSt
    override fun pasteRange(wbws: WbWs, ra: RangeAddress) :Rse<Unit>{
        val req = PasteRangeRequest2(
            rangeId = RangeIdImp(
                rangeAddress = ra,
                wbKey = wbws.wbKey,
                wsName = wbws.wsName,
            ),
            windowId = stateCont.getWindowStateMsByWbKey(wbws.wbKey)?.value?.id
        )
        val out = rangeRM.pasteRange(req)
        out?.let {
            rangeApplier.applyPasteRange(it)
        }
        return Ok(Unit)
    }
}
