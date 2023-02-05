package com.qxdzbc.p6.app.document.wb_container

import com.qxdzbc.common.Rse
import com.qxdzbc.common.WithSize
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey

interface WorkbookContainer : WorkbookGetter, WithSize {

    /**
     * Add a workbook to this container, and create a new workbook state if necessary.
     */
    fun addWb(wb: Workbook): WorkbookContainer

    /**
     * Overwrite the workbook having the same key as [wb] with [wb].
     */
    fun addWbRs(wb: Workbook): Rse<WorkbookContainer>

    /**
     * Overwrite the workbook having the same key as [wb] with [wb].
     */
    fun overwriteWB(wb: Workbook): WorkbookContainer

    /**
     * Overwrite a workbook having the same key as [wb], and create a new workbook state if necessary.
     */
    fun overwriteWBRs(wb: Workbook): Rse<WorkbookContainer>

    /**
     * Add or overwrite a workbook, then create a new wb state if no such state exist
     */
    fun addOrOverWriteWbRs(wb: Workbook): Rse<WorkbookContainer>

    /**
     * Add or overwrite a workbook, then create a new wb state if no such state exist
     */
    fun addOrOverWriteWb(wb: Workbook): WorkbookContainer

    fun removeWb(wbKey: WorkbookKey): WorkbookContainer
    fun removeWbRs(wbKey: WorkbookKey): Rse<WorkbookContainer>

    @Deprecated("don't use. It is dangerous to use this function because it create in consistency in the app state.")
    fun removeAll(): WorkbookContainer
    fun containWb(wbKey: WorkbookKey): Boolean

    fun replaceKey(oldKey: WorkbookKey, newKey: WorkbookKey): WorkbookContainer
    fun replaceKeyRs(oldKey: WorkbookKey, newKey: WorkbookKey): Rse<WorkbookContainer>
}
