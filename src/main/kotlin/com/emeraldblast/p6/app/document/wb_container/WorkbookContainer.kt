package com.emeraldblast.p6.app.document.wb_container

import com.emeraldblast.p6.app.common.utils.Rse
import com.emeraldblast.p6.app.document.workbook.Workbook
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.emeraldblast.p6.ui.common.compose.Ms
import java.nio.file.Path
import com.github.michaelbull.result.Result
interface WorkbookContainer {
    val wbList:List<Workbook>

    fun getWb(wbKey:WorkbookKey): Workbook?
    fun getWbMs(wbKey:WorkbookKey): Ms<Workbook>?
    fun getWbRs(wbKey:WorkbookKey): Result<Workbook,ErrorReport>
    fun getWb(path: Path):Workbook?
    fun getWbRs(path: Path):Result<Workbook,ErrorReport>

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
    fun removeAll():WorkbookContainer
    fun hasWb(wbKey: WorkbookKey):Boolean
    fun replaceKey(oldKey:WorkbookKey, newKey: WorkbookKey):WorkbookContainer
    fun replaceKeyRs(oldKey:WorkbookKey, newKey: WorkbookKey): Rse<WorkbookContainer>
}
