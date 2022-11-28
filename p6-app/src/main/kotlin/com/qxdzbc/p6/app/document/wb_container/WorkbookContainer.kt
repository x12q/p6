package com.qxdzbc.p6.app.document.wb_container

import com.github.michaelbull.result.Result
import com.qxdzbc.common.Rse
import com.qxdzbc.common.WithSize
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey

interface WorkbookContainer : WorkbookGetter,WithSize{

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
    fun containWb(wbKey: WorkbookKey):Boolean

    fun replaceKey(oldKey:WorkbookKey, newKey: WorkbookKey):WorkbookContainer
    fun replaceKeyRs(oldKey:WorkbookKey, newKey: WorkbookKey): Rse<WorkbookContainer>
}
