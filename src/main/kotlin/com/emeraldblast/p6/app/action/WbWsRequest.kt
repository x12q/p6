package com.emeraldblast.p6.app.action

import com.emeraldblast.p6.app.common.err.WithNavInfoImp
import com.emeraldblast.p6.app.common.err.WithReportNavInfo
import com.emeraldblast.p6.app.document.workbook.WorkbookKey

/**
 * Request for deleting multiple cells and ranges at the same time
 */
open class WbWsRequest(
    override val wbKey: WorkbookKey,
    val wsName: String,
    override val windowId: String? = null
):WithReportNavInfo {

}
