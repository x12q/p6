package com.qxdzbc.p6.composite_actions.range.range_to_clipboard

import com.qxdzbc.p6.common.utils.RseNav


interface RangeToClipboardAction {
    fun rangeToClipboard(request: RangeToClipboardRequest): RseNav<RangeToClipboardResponse2>
}

