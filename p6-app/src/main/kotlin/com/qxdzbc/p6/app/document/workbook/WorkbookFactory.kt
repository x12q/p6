package com.qxdzbc.p6.app.document.workbook

import com.qxdzbc.common.error.ErrorReport
import com.github.michaelbull.result.Result

/**
 * Encase certain default wb creation code
 */
interface WorkbookFactory {
    fun createWbRs(): Result<Workbook, ErrorReport>
}
