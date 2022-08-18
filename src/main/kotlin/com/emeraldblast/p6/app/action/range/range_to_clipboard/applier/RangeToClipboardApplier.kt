package com.emeraldblast.p6.app.action.range.range_to_clipboard.applier

import com.emeraldblast.p6.app.action.range.range_to_clipboard.RangeToClipboardResponse2
import com.emeraldblast.p6.app.common.utils.RseNav

interface RangeToClipboardApplier {
    fun applyRes2(res: RseNav<RangeToClipboardResponse2>): RseNav<RangeToClipboardResponse2>
}
