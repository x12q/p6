package com.emeraldblast.p6.app.action.range.range_to_clipboard.rm

import com.emeraldblast.p6.app.action.range.range_to_clipboard.RangeToClipboardRequest
import com.emeraldblast.p6.app.action.range.range_to_clipboard.RangeToClipboardResponse2
import com.emeraldblast.p6.app.common.utils.RseNav
import javax.inject.Inject

class FakeCopyRangeToClipboardRM @Inject constructor() : CopyRangeToClipboardRM {

    override fun copyRangeToClipboard2(request: RangeToClipboardRequest): RseNav<RangeToClipboardResponse2> {
        TODO("Not yet implemented")
    }

}
