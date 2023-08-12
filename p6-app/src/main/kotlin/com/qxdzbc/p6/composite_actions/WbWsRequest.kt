package com.qxdzbc.p6.composite_actions

import com.qxdzbc.p6.common.err.WithReportNavInfo
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey

/**
 * Request for deleting multiple cells and ranges at the same time
 */
open class WbWsRequest(
    override val wbKey: WorkbookKey,
    val wsName: String,
    override val windowId: String? = null
): WithReportNavInfo {

}
