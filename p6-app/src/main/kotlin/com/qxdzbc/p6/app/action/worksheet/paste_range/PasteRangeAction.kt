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
     * Paste a range from the clipboard to a worksheet at [targetWbWsSt], shift the range for any included formula if it is needed
     * @param targetWbWsSt target worksheet address
     * @param targetRangeAddress target range address
     */
    fun pasteRange(targetWbWsSt: WbWsSt, targetRangeAddress: RangeAddress, undoable:Boolean):Rse<Unit>

    /**
     * Paste a range from the clipboard to a worksheet at [wbws], shift the range for any included formula if it is needed
     * @param wbws  target worksheet
     * @param rangeAddress target range address
     */
    fun pasteRange(wbws: WbWs, rangeAddress: RangeAddress,undo:Boolean):Rse<Unit>
}
