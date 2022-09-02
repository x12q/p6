package com.qxdzbc.p6.app.action.range.range_to_clipboard.applier

import com.qxdzbc.p6.app.action.range.RangeId
import com.qxdzbc.p6.app.action.range.RangeIdImp

interface RangeToClipboardInternalApplier {
    fun apply(rangeId: RangeId, windowId:String?)
}
