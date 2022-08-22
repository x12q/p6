package com.qxdzbc.p6.app.document.workbook

import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.p6.common.exception.error.ErrorReport
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result

abstract class BaseWorkbook : Workbook {
    override fun getWs(index: Int): Worksheet? {
        return getWsRs(index).component1()
    }

    override fun getWs(name: String): Worksheet? {
        return getWsRs(name).component1()
    }

    override fun getWsRs(index: Int): Result<Worksheet, ErrorReport> {
        if (index in worksheets.indices) {
            return Ok(worksheets[index])
        } else {
            return WorkbookErrors.InvalidWorksheet.report(index).toErr()
        }
    }

    override fun getWsRs(name: String): Result<Worksheet, ErrorReport> {
        val ws = worksheetMap[name]
        return ws?.let { Ok(it) } ?: WorkbookErrors.InvalidWorksheet.report(name).toErr()
    }
}
