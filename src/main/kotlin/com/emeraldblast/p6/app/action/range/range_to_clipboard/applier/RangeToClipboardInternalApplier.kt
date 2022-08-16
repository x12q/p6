package com.emeraldblast.p6.app.action.range.range_to_clipboard.applier

import com.emeraldblast.p6.app.action.range.RangeId

interface RangeToClipboardInternalApplier {
    fun apply(rangeId: RangeId, windowId:String?)
}
