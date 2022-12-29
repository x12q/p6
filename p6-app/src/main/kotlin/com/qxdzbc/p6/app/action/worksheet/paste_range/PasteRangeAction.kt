package com.qxdzbc.p6.app.action.worksheet.paste_range

import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.document.range.address.RangeAddress

/**
 * Paste a range from the clipboard to a worksheet at [wbws], shift the range for any included formula if it is needed
 */
interface PasteRangeAction {
    fun pasteRange(wbwsSt: WbWsSt, rangeAddress: RangeAddress,undo:Boolean=true):Rse<Unit>

    /**
     * Paste a range from the clipboard to a worksheet at [wbws], shift the range for any included formula if it is needed
     * @param wbws for identifying the target worksheet
     * @param rangeAddress for identifying the target range
     */
    fun pasteRange(wbws: WbWs, rangeAddress: RangeAddress,undo:Boolean=true):Rse<Unit>
}
