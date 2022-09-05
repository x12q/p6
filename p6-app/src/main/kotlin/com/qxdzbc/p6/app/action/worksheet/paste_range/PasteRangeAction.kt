package com.qxdzbc.p6.app.action.worksheet.paste_range

import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.document.range.address.RangeAddress

interface PasteRangeAction {
    fun pasteRange(wbws: WbWs, ra: RangeAddress):Rse<Unit>
}
