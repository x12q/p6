package com.qxdzbc.p6.app.action.range.range_to_clipboard.rm

import com.qxdzbc.p6.app.action.range.range_to_clipboard.RangeToClipboardRequest
import com.qxdzbc.p6.app.action.range.range_to_clipboard.RangeToClipboardResponse2
import com.qxdzbc.p6.app.common.utils.RseNav

interface CopyRangeToClipboardRM {
    fun copyRangeToClipboard2(request: RangeToClipboardRequest): RseNav<RangeToClipboardResponse2>

}
