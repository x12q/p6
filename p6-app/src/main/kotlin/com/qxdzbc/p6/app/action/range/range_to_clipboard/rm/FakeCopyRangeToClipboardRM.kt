package com.qxdzbc.p6.app.action.range.range_to_clipboard.rm

import com.qxdzbc.p6.app.action.range.range_to_clipboard.RangeToClipboardRequest
import com.qxdzbc.p6.app.action.range.range_to_clipboard.RangeToClipboardResponse2
import com.qxdzbc.p6.app.common.utils.RseNav
import com.qxdzbc.p6.di.Fake
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
@Fake
class FakeCopyRangeToClipboardRM @Inject constructor() : CopyRangeToClipboardRM {

    override fun copyRangeToClipboard2(request: RangeToClipboardRequest): RseNav<RangeToClipboardResponse2> {
        TODO("Not yet implemented")
    }

}
