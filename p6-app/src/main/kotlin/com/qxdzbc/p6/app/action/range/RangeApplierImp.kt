package com.qxdzbc.p6.app.action.range

import com.qxdzbc.p6.app.action.range.paste_range.applier.PasteRangeApplier
import com.qxdzbc.p6.app.action.range.paste_range.PasteRangeResponse
import javax.inject.Inject

class RangeApplierImp @Inject constructor(
    private val pasteRangeApplier: PasteRangeApplier
) : RangeApplier {
    override fun applyPasteRange(res: PasteRangeResponse) {
        pasteRangeApplier.applyPasteRange(res)
    }
}
