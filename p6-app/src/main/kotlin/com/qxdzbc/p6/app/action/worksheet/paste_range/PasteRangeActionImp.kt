package com.qxdzbc.p6.app.action.worksheet.paste_range

import androidx.compose.runtime.getValue
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.flatMap
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.range.RangeIdImp
import com.qxdzbc.p6.app.action.range.paste_range.PasteRangeRequest2
import com.qxdzbc.p6.app.action.range.paste_range.applier.PasteRangeApplier
import com.qxdzbc.p6.app.action.range.paste_range.rm.PasteRangeRM
import com.qxdzbc.p6.app.document.range.address.RangeAddress

import com.qxdzbc.p6.ui.app.state.StateContainer
import javax.inject.Inject


class PasteRangeActionImp @Inject constructor(
    private val stateContSt: St<@JvmSuppressWildcards StateContainer>,
    private val rangeRM: PasteRangeRM,
    private val rangeApplier: PasteRangeApplier,
) : PasteRangeAction {
    private val sc by stateContSt
    override fun pasteRange(wbws: WbWs, ra: RangeAddress) :Rse<Unit>{

        val rt = sc.getWbKeyStRs(wbws.wbKey).flatMap {wbkSt->
            sc.getWsNameStRs(wbws).flatMap { wsNameSt->
                val req = PasteRangeRequest2(
                    rangeId = RangeIdImp(
                        rangeAddress = ra,
                        wbKeySt = wbkSt,
                        wsNameSt = wsNameSt,
                    ),
                    windowId = sc.getWindowStateMsByWbKey(wbws.wbKey)?.value?.id
                )
                val out = rangeRM.pasteRange(req)
                out?.let {
                    rangeApplier.applyPasteRange(it)
                }
                Ok(Unit)
            }
        }
        return rt
    }
}
