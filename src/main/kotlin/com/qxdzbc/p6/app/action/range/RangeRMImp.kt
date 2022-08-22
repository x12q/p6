package com.qxdzbc.p6.app.action.range

import com.qxdzbc.p6.app.action.range.paste_range.rm.PasteRangeRM
import com.qxdzbc.p6.app.action.range.range_to_clipboard.rm.CopyRangeToClipboardRM

import javax.inject.Inject

class RangeRMImp @Inject constructor(

    private val copyRangeToClipboardRM: CopyRangeToClipboardRM,

    private val pasteRangeRM : PasteRangeRM,
) : RangeRM, PasteRangeRM by pasteRangeRM, CopyRangeToClipboardRM by copyRangeToClipboardRM
