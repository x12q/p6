package com.qxdzbc.p6.app.common.err

import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.common.error.ErrorReport
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.qxdzbc.common.Rse

class ErrorReportWithNavInfo(
    val errorReport: ErrorReport,
    override val wbKey: WorkbookKey? = null,
    override val windowId: String? = null
) :WithReportNavInfo{
    companion object {
        fun ErrorReport.withNav(
            wbKey: WorkbookKey? = null,
            windowId: String? = null
        ): ErrorReportWithNavInfo {
            return ErrorReportWithNavInfo(this, wbKey, windowId)
        }

        fun ErrorReportWithNavInfo.toErr(): Err<ErrorReportWithNavInfo> {
            return Err(this)
        }

        fun ErrorReport.withNav(info: WithReportNavInfo): ErrorReportWithNavInfo {
            return withNav(info.wbKey, info.windowId)
        }
    }
}
