package com.qxdzbc.p6.document_data_layer.range.copy_paste

import com.qxdzbc.common.Rse
import com.qxdzbc.p6.composite_actions.worksheet.paste_range.RangeCopyDM
import com.qxdzbc.p6.document_data_layer.range.RangeCopy
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey

/**
 * Read and parse data from clipboard
 */
interface ClipboardReader {
    /**
     * Read data from the clipboard, and try to parse it into a [RangeCopy] that is associated with [wbKey] and [wsName]
     */
    fun readDataFromClipboard(wbKey: WorkbookKey, wsName: String): RangeCopy?

    /**
     * Read data from the clipboard, and try to parse it into a [RangeCopy] that is associated with [wbKey] and [wsName]
     */
    fun readDataFromClipboardRs(wbKey: WorkbookKey, wsName: String): Rse<RangeCopy>

    /**
     * Read data from the clipboard, and try to parse it into a [RangeCopyDM]
     */
    fun readRangeCopyDMFromClipboard(): RangeCopyDM?

    /**
     * Read data from the clipboard, and try to parse it into a [RangeCopyDM]
     */
    fun readRangeCopyDMFromClipboardRs(): Rse<RangeCopyDM>
}
