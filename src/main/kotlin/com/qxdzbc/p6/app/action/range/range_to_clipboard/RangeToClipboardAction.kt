package com.qxdzbc.p6.app.action.range.range_to_clipboard

import com.qxdzbc.p6.app.common.utils.RseNav


interface RangeToClipboardAction {
    fun rangeToClipboard(request: RangeToClipboardRequest): RseNav<RangeToClipboardResponse2>
}

