package com.qxdzbc.p6.app.action.range.range_to_clipboard.applier

import com.qxdzbc.p6.app.action.range.range_to_clipboard.RangeToClipboardResponse2
import com.qxdzbc.p6.app.common.utils.RseNav
import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class RangeToClipboardApplierImp @Inject constructor(
    val internalApplier: RangeToClipboardInternalApplier,
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
