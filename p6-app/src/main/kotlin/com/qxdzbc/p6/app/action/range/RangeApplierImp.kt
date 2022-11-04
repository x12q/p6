package com.qxdzbc.p6.app.action.range

import com.qxdzbc.p6.app.action.range.paste_range.applier.PasteRangeApplier
import com.qxdzbc.p6.app.action.range.paste_range.PasteRangeResponse
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class RangeApplierImp @Inject constructor(
    private val pasteRangeApplier: PasteRangeApplier
) : RangeApplier {
    override fun applyPasteRange(res: PasteRangeResponse) {
        pasteRangeApplier.applyPasteRange(res)
    }
}
