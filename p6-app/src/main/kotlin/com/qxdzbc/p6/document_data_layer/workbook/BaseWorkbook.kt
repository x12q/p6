package com.qxdzbc.p6.document_data_layer.workbook

import com.qxdzbc.p6.document_data_layer.worksheet.Worksheet
import com.github.michaelbull.result.Ok
import com.qxdzbc.common.ResultUtils.toOk
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St

abstract class BaseWorkbook : Workbook {

    override val size: Int get() = worksheetMap.size

    override fun getWs(index: Int): Worksheet? {
        return getWsRs(index).component1()
    }

    override fun getWs(name: String): Worksheet? {
        return getWsRs(name).component1()
    }

    override fun getWsRs(index: Int): Rse<Worksheet> {
        if (index in worksheets.indices) {
            return Ok(worksheets[index])
        } else {
            return WorkbookErrors.InvalidWorksheet.report(index).toErr()
        }
    }

    override fun getWsRs(name: String): Rse<Worksheet> {
        val ws = worksheets.firstOrNull { it.name == name }
        return ws?.let { Ok(it) } ?: WorkbookErrors.InvalidWorksheet.report(name).toErr()
    }

    override fun getWsMs(index: Int): Ms<Worksheet>? {
        return worksheetMsList.getOrNull(index)
    }

    override fun getWsMs(name: String): Ms<Worksheet>? {
        return worksheetMsList.firstOrNull { it.value.name == name }
    }

    override fun getWsMs(nameSt: St<String>): Ms<Worksheet>? {
        return worksheetMsMap[nameSt]
    }

    override fun getWsMsRs(index: Int): Rse<Ms<Worksheet>> {
        val ws = getWsMs(index)
        return ws?.let { Ok(it) } ?: WorkbookErrors.InvalidWorksheet.report(index).toErr()
    }

    override fun getWsMsRs(name: String): Rse<Ms<Worksheet>> {
        val ws = getWsMs(name)
        return ws?.let { Ok(it) } ?: WorkbookErrors.InvalidWorksheet.report(name).toErr()
    }

    override fun getWsMsRs(nameSt: St<String>): Rse<Ms<Worksheet>> {
        return getWsMs(nameSt)?.toOk()
            ?: WorkbookErrors.InvalidWorksheet.report(nameSt.value).toErr()
    }

    override fun getWs(nameSt: St<String>): Worksheet? {
        return worksheetMsMap[nameSt]?.value
    }

    override fun getWsRs(nameSt: St<String>): Rse<Worksheet> {
        val rt:Rse<Worksheet> = worksheetMsMap[nameSt]?.value?.toOk() ?: WorkbookErrors.InvalidWorksheet.report(nameSt.value).toErr()
        return rt
    }

}
