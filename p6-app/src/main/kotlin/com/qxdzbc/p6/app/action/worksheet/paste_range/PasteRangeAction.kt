package com.qxdzbc.p6.app.action.worksheet.paste_range

import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.document.range.address.RangeAddress

/**
 * Paste a range from the clipboard to a worksheet
 */
interface PasteRangeAction {
    /**
     * Paste a range from the clipboard to a worksheet at [wbwsSt], shift the range for any included formula if it is needed
     * @param wbwsSt target worksheet
     * @param rangeAddress target range address
     */
    fun pasteRange(wbwsSt: WbWsSt, rangeAddress: RangeAddress,undo:Boolean):Rse<Unit>

    /**
     * Paste a range from the clipboard to a worksheet at [wbws], shift the range for any included formula if it is needed
     * @param wbws  target worksheet
     * @param rangeAddress target range address
     */
    fun pasteRange(wbws: WbWs, rangeAddress: RangeAddress,undo:Boolean):Rse<Unit>
}
