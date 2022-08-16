package com.emeraldblast.p6.app.action.range

import com.emeraldblast.p6.app.action.range.paste_range.rm.PasteRangeRM
import com.emeraldblast.p6.app.action.range.range_to_clipboard.rm.CopyRangeToClipboardRM

interface RangeRM: PasteRangeRM, CopyRangeToClipboardRM {
//    abstract fun copyRangeToClipboard(rangeToClipboardRequest: RangeToClipboardRequest): RangeToClipboardResponse?

}
