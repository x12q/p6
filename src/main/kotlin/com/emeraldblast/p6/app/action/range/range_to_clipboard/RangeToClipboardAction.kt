package com.emeraldblast.p6.app.action.range.range_to_clipboard

import com.emeraldblast.p6.app.common.RseNav


interface RangeToClipboardAction {
    fun rangeToClipboard(request: RangeToClipboardRequest):RseNav<RangeToClipboardResponse2>
}

