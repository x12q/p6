package com.emeraldblast.p6.app.code

import com.emeraldblast.p6.app.document.cell.address.CellAddress
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.github.michaelbull.result.Result

/**
 * Contain functions to control the back-end
 */
interface BackEndCommander {
    suspend fun initBackEnd(): Result<String, ErrorReport>
    suspend fun createReqUISocket(socketPort: Int): Result<String, ErrorReport>
    suspend fun startEventServer(socketPort: Int):Result<String,ErrorReport>

    suspend fun setActiveSheet(workbookKey: WorkbookKey, sheetName: String): Result<String, ErrorReport>
    suspend fun setActiveSheetOnActiveWb(sheetName: String): Result<String, ErrorReport>
    suspend fun runCellFormula(
        formula: String,
        worksheetName: String,
        workbookKey: WorkbookKey,
        cellAddress: CellAddress,
    ): Result<String, ErrorReport>

    suspend fun renameSheetRs(workbookKey: WorkbookKey, oldName: String, newName: String): Result<String, ErrorReport>
    suspend fun deleteSheet(workbookKey: WorkbookKey, sheetName:String):Result<String,ErrorReport>
    suspend fun createNewSheetRs(workbookKey: WorkbookKey, newSheetName:String?):Result<String,ErrorReport>
}

