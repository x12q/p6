package com.qxdzbc.p6.app.document.workbook

import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.github.michaelbull.result.Ok
import com.qxdzbc.common.ResultUtils.toOk
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.St

abstract class BaseWorkbook : Workbook {

    override val size: Int get() = worksheetMap.size

    override fun getWs(index: Int): Worksheet? {
        return worksheetMsList.getOrNull(index)
    }

    override fun getWs(name: String): Worksheet? {
        return worksheetMsList.firstOrNull { it.name == name }
    }

    override fun getWsRs(index: Int): Rse<Worksheet> {
        val ws = getWs(index)
        return ws?.let { Ok(it) } ?: WorkbookErrors.InvalidWorksheet.report(index).toErr()
    }

    override fun getWsRs(name: String): Rse<Worksheet> {
        val ws = getWs(name)
        return ws?.let { Ok(it) } ?: WorkbookErrors.InvalidWorksheet.report(name).toErr()
    }

    override fun getWsRs(nameSt: St<String>): Rse<Worksheet> {
        return getWs(nameSt)?.toOk()
            ?: WorkbookErrors.InvalidWorksheet.report(nameSt.value).toErr()
    }

    override fun getWs(nameSt: St<String>): Worksheet? {
        return worksheetMsMap[nameSt]
    }

}
