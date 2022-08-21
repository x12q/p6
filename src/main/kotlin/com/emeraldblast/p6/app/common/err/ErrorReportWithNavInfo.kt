package com.emeraldblast.p6.app.common.err

import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.github.michaelbull.result.Err

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
