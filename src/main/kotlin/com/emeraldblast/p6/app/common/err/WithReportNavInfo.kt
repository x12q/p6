package com.emeraldblast.p6.app.common.err

import com.emeraldblast.p6.app.document.workbook.WorkbookKey

/**
 * Contain infor for finding the correct place to report an error
 */
interface WithReportNavInfo {
    val wbKey: WorkbookKey?
    val windowId: String?
    companion object{
        val default = object : WithReportNavInfo {
            override val wbKey: WorkbookKey? = null
            override val windowId: String? = null
        }
    }
}
