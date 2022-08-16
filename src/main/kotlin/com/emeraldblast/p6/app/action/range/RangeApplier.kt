package com.emeraldblast.p6.app.action.range

import com.emeraldblast.p6.app.action.range.paste_range.PasteRangeResponse

interface RangeApplier {
    fun applyPasteRange(res: PasteRangeResponse)
}
