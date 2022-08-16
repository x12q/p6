package com.emeraldblast.p6.app.code

import com.emeraldblast.p6.app.coderunner.CodeRunner
import com.emeraldblast.p6.app.document.cell.address.CellAddress
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result

object BackEndCommanders {
    fun std(codeRunner: CodeRunner):BackEndCommander{
        return BackEndCommanderImp(codeRunner)
    }

    fun dummy():BackEndCommander{
        return object :BackEndCommander{
            override suspend fun initBackEnd(): Result<String, ErrorReport> {
                val str = "init backend"
                println(str)
                return Ok(str)
            }

            override suspend fun createReqUISocket(socketPort: Int): Result<String, ErrorReport> {
                val str = "createReqUISocket"
                println(str)
                return Ok(str)
            }

            override suspend fun startEventServer(socketPort: Int): Result<String, ErrorReport> {
                TODO("Not yet implemented")
            }

            override suspend fun setActiveSheet(
                workbookKey: WorkbookKey,
                sheetName: String,
            ): Result<String, ErrorReport> {
                val str = "setActiveSheet"
                println(str)
                return Ok(str)
            }

            override suspend fun setActiveSheetOnActiveWb(sheetName: String): Result<String, ErrorReport> {
                val str = "setActiveSheetOnActiveWb"
                println(str)
                return Ok(str)
            }

            override suspend fun runCellFormula(
                formula: String,
                worksheetName: String,
                workbookKey: WorkbookKey,
                cellAddress: CellAddress,
            ): Result<String, ErrorReport> {
                val str = "runCellFormula"
                println(str)
                return Ok(str)
            }

            override suspend fun renameSheetRs(
                workbookKey: WorkbookKey,
                oldName: String,
                newName: String,
            ): Result<String, ErrorReport> {
                TODO("Not yet implemented")
            }

            override suspend fun deleteSheet(workbookKey: WorkbookKey, sheetName: String): Result<String, ErrorReport> {
                TODO("Not yet implemented")
            }

            override suspend fun createNewSheetRs(
                workbookKey: WorkbookKey,
                newSheetName: String?,
            ): Result<String, ErrorReport> {
                TODO("Not yet implemented")
            }
        }
    }
}
