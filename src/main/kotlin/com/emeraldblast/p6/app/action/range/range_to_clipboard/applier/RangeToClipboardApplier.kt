package com.emeraldblast.p6.app.action.range.range_to_clipboard.applier

import com.emeraldblast.p6.app.action.applier.ResApplier
import com.emeraldblast.p6.app.action.range.range_to_clipboard.RangeToClipboardResponse
import com.emeraldblast.p6.app.action.range.range_to_clipboard.RangeToClipboardResponse2
import com.emeraldblast.p6.app.common.RseNav

interface RangeToClipboardApplier {
    @Deprecated("old acr")
    fun applyRes(res: RangeToClipboardResponse?)
    fun applyRes2(res: RseNav<RangeToClipboardResponse2>):RseNav<RangeToClipboardResponse2>
}
