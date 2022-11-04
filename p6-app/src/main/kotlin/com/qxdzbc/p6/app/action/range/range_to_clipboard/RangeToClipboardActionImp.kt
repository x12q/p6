package com.qxdzbc.p6.app.action.range.range_to_clipboard

import com.qxdzbc.p6.app.action.range.range_to_clipboard.applier.RangeToClipboardApplier
import com.qxdzbc.p6.app.action.range.range_to_clipboard.rm.CopyRangeToClipboardRM
import com.qxdzbc.p6.app.common.utils.RseNav
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class RangeToClipboardActionImp @Inject constructor(
    private val copyRangeRM: CopyRangeToClipboardRM,
    private val rangeToClipboardApplier: RangeToClipboardApplier,
) : RangeToClipboardAction {

    override fun rangeToClipboard(request: RangeToClipboardRequest): RseNav<RangeToClipboardResponse2> {
        val res = copyRangeRM.copyRangeToClipboard2(request)
        return rangeToClipboardApplier.applyRes2(res)
    }
}
