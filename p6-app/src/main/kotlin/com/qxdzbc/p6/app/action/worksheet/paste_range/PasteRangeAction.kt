package com.qxdzbc.p6.app.action.worksheet.paste_range

import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.document.range.address.RangeAddress

/**
 * Paste a range from the clipboard to a worksheet at [wbws], shift the range for any included formula if it is needed
 * TODO add a function that use wbwsSt instead of wbws to improve performance
 */
interface PasteRangeAction {
    /**
     * Paste a range from the clipboard to a worksheet at [wbws], shift the range for any included formula if it is needed
     * @param wbws for identifying the target worksheet
     * @param ra for identifying the target range
     */
    fun pasteRange(wbws: WbWs, ra: RangeAddress):Rse<Unit>
}
