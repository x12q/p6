package com.qxdzbc.p6.common.err

import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey

/**
 * Contain infor for finding the correct place to report an error
 */
interface WithReportNavInfo {
    val wbKey: WorkbookKey?
    val windowId: String?
    companion object{
        val default = object : com.qxdzbc.p6.common.err.WithReportNavInfo {
            override val wbKey: WorkbookKey? = null
            override val windowId: String? = null
        }
    }
}
