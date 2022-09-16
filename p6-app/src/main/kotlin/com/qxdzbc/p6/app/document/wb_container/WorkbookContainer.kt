package com.qxdzbc.p6.app.document.wb_container

import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import java.nio.file.Path
import com.github.michaelbull.result.Result
import com.qxdzbc.common.CanCheckEmpty
import com.qxdzbc.common.WithSize

interface WorkbookGetter{
    fun getWb(wbKeySt:St<WorkbookKey>): Workbook?
    fun getWbMs(wbKeySt:St<WorkbookKey>): Ms<Workbook>?
    fun getWbRs(wbKeySt:St<WorkbookKey>): Result<Workbook,ErrorReport>
    fun getWbMsRs(wbKeySt:St<WorkbookKey>): Result<Ms<Workbook>,ErrorReport>

    fun getWb(wbKey:WorkbookKey): Workbook?
    fun getWbMs(wbKey:WorkbookKey): Ms<Workbook>?
    fun getWbRs(wbKey:WorkbookKey): Result<Workbook,ErrorReport>
    fun getWbMsRs(wbKey:WorkbookKey): Result<Ms<Workbook>,ErrorReport>

    fun getWb(path: Path):Workbook?
    fun getWbRs(path: Path):Result<Workbook,ErrorReport>
    fun getWbMsRs(path: Path):Result<Ms<Workbook>,ErrorReport>
}

interface WorkbookContainer : WorkbookGetter,WithSize{
    val wbList:List<Workbook>

    fun addWb(wb: Workbook): WorkbookContainer
    fun addWbRs(wb: Workbook):Result<WorkbookContainer,ErrorReport>

    fun overwriteWB(wb: Workbook):WorkbookContainer
    fun overwriteWBRs(wb: Workbook): Rse<WorkbookContainer>

    /**
     * Add or overwrite a workbook, this will also create a new wb state if no such state exist
     */
    fun addOrOverWriteWbRs(wb:Workbook): Rse<WorkbookContainer>
    fun addOrOverWriteWb(wb:Workbook):WorkbookContainer

    fun removeWb(wbKey: WorkbookKey):WorkbookContainer
    fun removeWbRs(wbKey: WorkbookKey): Rse<WorkbookContainer>
    @Deprecated("don't use. It is dangerous to use this function because it create in consistency in the app state.")
    fun removeAll():WorkbookContainer
    fun hasWb(wbKey: WorkbookKey):Boolean

    fun replaceKey(oldKey:WorkbookKey, newKey: WorkbookKey):WorkbookContainer
    fun replaceKeyRs(oldKey:WorkbookKey, newKey: WorkbookKey): Rse<WorkbookContainer>
}
