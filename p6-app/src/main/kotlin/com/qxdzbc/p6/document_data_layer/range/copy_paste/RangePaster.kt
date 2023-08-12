package com.qxdzbc.p6.document_data_layer.range.copy_paste

import com.qxdzbc.p6.composite_actions.range.RangeId
import com.qxdzbc.p6.document_data_layer.range.RangeCopy
import com.qxdzbc.p6.document_data_layer.workbook.Workbook
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey

/**
 * paste whatever data in the clipboard into a range
 */
interface RangePaster {

    /**
     * paste whatever data in the clipboard into a range identified by [target].
     * This function does not mutate the app state, but return a new state. Consumer can use returned state to update the app state.
     * @return a [PasteResponse] containing a new [Result]<[Workbook]>, and the identity of the source range
     */
    @Deprecated("don't use, kept just in case")
    fun paste(target: RangeId): PasteResponse
    fun readDataFromClipboard(wbKey: WorkbookKey, wsName: String): RangeCopy?

}
