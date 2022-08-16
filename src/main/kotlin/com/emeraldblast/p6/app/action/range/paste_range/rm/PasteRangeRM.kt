package com.emeraldblast.p6.app.action.range.paste_range.rm

import com.emeraldblast.p6.app.action.range.paste_range.PasteRangeRequest
import com.emeraldblast.p6.app.action.range.paste_range.PasteRangeRequest2
import com.emeraldblast.p6.app.action.range.paste_range.PasteRangeResponse

interface PasteRangeRM {
    fun pasteRange(request: PasteRangeRequest): PasteRangeResponse?
    fun pasteRange2(request: PasteRangeRequest2): PasteRangeResponse?
}
