package com.qxdzbc.p6.common.err

import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import com.github.michaelbull.result.Err
import com.qxdzbc.common.error.ErrorReport

class ErrorReportWithNavInfo(
    val errorReport: ErrorReport,
    override val wbKey: WorkbookKey? = null,
    override val windowId: String? = null
) : com.qxdzbc.p6.common.err.WithReportNavInfo {
    companion object {
        fun ErrorReport.withNav(
            wbKey: WorkbookKey? = null,
            windowId: String? = null
        ): com.qxdzbc.p6.common.err.ErrorReportWithNavInfo {
            return com.qxdzbc.p6.common.err.ErrorReportWithNavInfo(this, wbKey, windowId)
        }

        fun com.qxdzbc.p6.common.err.ErrorReportWithNavInfo.toErr(): Err<com.qxdzbc.p6.common.err.ErrorReportWithNavInfo> {
            return Err(this)
        }

        fun ErrorReport.withNav(info: com.qxdzbc.p6.common.err.WithReportNavInfo): com.qxdzbc.p6.common.err.ErrorReportWithNavInfo {
            return withNav(info.wbKey, info.windowId)
        }
    }
}
