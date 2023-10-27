package com.qxdzbc.p6.composite_actions.range.range_to_clipboard

import com.qxdzbc.p6.common.utils.RseNav

/**
 * Copy a worksheet range to the system clipboard
 */
interface RangeToClipboardAction {
    /**
     * Copy a worksheet range to the system clipboard
     */
    fun rangeToClipboard(request: RangeToClipboardRequest): RseNav<RangeToClipboardResponse2>
}

