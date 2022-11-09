package com.qxdzbc.p6.app.document.wb_container

import com.github.michaelbull.result.Result
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import java.nio.file.Path

interface WorkbookGetter{
    val allWbs:List<Workbook>
    val allWbMs:List<Ms<Workbook>>
    fun getWb(wbKeySt: St<WorkbookKey>): Workbook?
    fun getWbMs(wbKeySt: St<WorkbookKey>): Ms<Workbook>?
    fun getWbRs(wbKeySt: St<WorkbookKey>): Result<Workbook, ErrorReport>
    fun getWbMsRs(wbKeySt: St<WorkbookKey>): Result<Ms<Workbook>, ErrorReport>

    fun getWb(wbKey: WorkbookKey): Workbook?
    fun getWbMs(wbKey: WorkbookKey): Ms<Workbook>?
    fun getWbRs(wbKey: WorkbookKey): Result<Workbook, ErrorReport>
    fun getWbMsRs(wbKey: WorkbookKey): Result<Ms<Workbook>, ErrorReport>

    fun getWb(path: Path): Workbook?
    fun getWbRs(path: Path): Result<Workbook, ErrorReport>
    fun getWbMsRs(path: Path): Result<Ms<Workbook>, ErrorReport>
}
