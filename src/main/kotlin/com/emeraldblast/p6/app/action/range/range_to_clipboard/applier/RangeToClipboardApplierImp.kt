package com.emeraldblast.p6.app.action.range.range_to_clipboard.applier

import com.emeraldblast.p6.app.action.applier.BaseApplier
import com.emeraldblast.p6.app.action.range.range_to_clipboard.RangeToClipboardResponse2
import com.emeraldblast.p6.app.common.utils.RseNav
import com.emeraldblast.p6.ui.app.ErrorRouter
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import javax.inject.Inject

class RangeToClipboardApplierImp @Inject constructor(
    val internalApplier: RangeToClipboardInternalApplier,
    private val baseApplier: BaseApplier,
    private val errorRouter: ErrorRouter,
) : RangeToClipboardApplier {

    override fun applyRes2(res: RseNav<RangeToClipboardResponse2>): RseNav<RangeToClipboardResponse2> {
        res.onFailure {
            errorRouter.publish(it)
        }.onSuccess {
            internalApplier.apply(it.range.rangeId,it.windowId)
        }
        return res
    }
}
