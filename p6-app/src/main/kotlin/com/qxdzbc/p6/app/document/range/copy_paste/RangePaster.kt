package com.qxdzbc.p6.app.document.range.copy_paste

import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.app.action.range.RangeId
import com.qxdzbc.p6.app.action.worksheet.paste_range.RangeCopyDM
import com.qxdzbc.p6.app.document.cell.Cell
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.RangeCopy
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey

/**
 * paste whatever data in the clipboard into a range
 */
interface RangePaster {

    /**
     * paste whatever data in the clipboard into a range identified by [target].
     * This function does not mutate the app state, but return a new state. Consumer can use returned state to update the app state.
     * @return a [PasteResponse] containing a new [Result]<[Workbook]>, and the identity of the source range
     */
    fun paste(target: RangeId):PasteResponse
    fun readRangeCopyFromClipboard(wbKey: WorkbookKey, wsName: String): RangeCopy?
    fun readRangeCopyDMFromClipboard(): RangeCopyDM?

}
