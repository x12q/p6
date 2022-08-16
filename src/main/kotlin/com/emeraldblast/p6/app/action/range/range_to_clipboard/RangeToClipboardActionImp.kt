package com.emeraldblast.p6.app.action.range.range_to_clipboard

import com.emeraldblast.p6.app.action.range.range_to_clipboard.applier.RangeToClipboardApplier
import com.emeraldblast.p6.app.action.range.range_to_clipboard.rm.CopyRangeToClipboardRM
import com.emeraldblast.p6.app.common.RseNav
import javax.inject.Inject

class RangeToClipboardActionImp @Inject constructor(
    private val copyRangeRM: CopyRangeToClipboardRM,
    private val rangeToClipboardApplier: RangeToClipboardApplier,
) : RangeToClipboardAction {

    override fun rangeToClipboard(request: RangeToClipboardRequest): RseNav<RangeToClipboardResponse2> {
        val res = copyRangeRM.copyRangeToClipboard2(request)
        return rangeToClipboardApplier.applyRes2(res)
    }
}
