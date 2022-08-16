package com.emeraldblast.p6.app.common.err

import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.github.michaelbull.result.Err

/**
 * Contain infor for finding the correct place to report an error
 */
interface WithReportNavInfo {
    val wbKey: WorkbookKey?
    val windowId: String?
    companion object{
        val default = object :WithReportNavInfo{
            override val wbKey: WorkbookKey? = null
            override val windowId: String? = null
        }
    }
}

class WithNavInfoImp(
    override val wbKey: WorkbookKey?,
    override val windowId: String?,
) :WithReportNavInfo

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
