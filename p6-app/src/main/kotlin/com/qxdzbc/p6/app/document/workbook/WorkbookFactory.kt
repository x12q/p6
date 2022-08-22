package com.qxdzbc.p6.app.document.workbook

import com.qxdzbc.p6.common.exception.error.ErrorReport
import com.github.michaelbull.result.Result

/**
 * Encase certain default wb creation code
 */
interface WorkbookFactory {
    fun createWbRs(): Result<Workbook, ErrorReport>
}
