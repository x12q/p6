package com.emeraldblast.p6.app.code

import com.emeraldblast.p6.app.app_context.InitErrors
import com.emeraldblast.p6.app.code.script_builder.AllScriptBuilderImp
import com.emeraldblast.p6.app.coderunner.CodeRunner
import com.emeraldblast.p6.app.common.utils.Utils
import com.emeraldblast.p6.app.document.cell.address.CellAddress
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import javax.inject.Inject

class BackEndCommanderImp @Inject constructor(private val runner: CodeRunner) : BackEndCommander {

    override suspend fun initBackEnd(): Result<String, ErrorReport> {
        val initScript = Utils.readResource("/script/initScript.py")
        if(initScript!=null){
            val rt = runner.run(initScript)
            return rt
        }else{
            return Err(InitErrors.invalidInitScript())
        }
    }

    override suspend fun createReqUISocket(socketPort: Int): Result<String, ErrorReport> {
        val script = """
            context = getApp().zContext
            socket = context.socket(zmq.REQ)
            socket.connect("tcp://localhost:${socketPort}")
            getApp().socketProvider.updateNotificationSocket(socket)
        """.trimIndent()
        val rt = runner.run(script)
        return rt
    }

    override suspend fun startEventServer(socketPort: Int): Result<String, ErrorReport> {
        val script = """
getApp().eventServer.start(${socketPort})
        """.trimIndent()
        val rt = runner.run(script)
        return rt
    }

    override suspend fun setActiveSheet(workbookKey: WorkbookKey, sheetName: String): Result<String, ErrorReport> {
        val codeBuilder = AllScriptBuilderImp()
        val script = codeBuilder.getWorkbook(workbookKey).setActiveSheet(sheetName).build()
        val rt = runner.run(script)
        return rt
    }

    override suspend fun setActiveSheetOnActiveWb(sheetName: String): Result<String, ErrorReport> {
        val codeBuilder = AllScriptBuilderImp()
        val script = codeBuilder.getActiveWorkbook().setActiveSheet(sheetName).build()
        val rt = runner.run(script)
        return rt
    }

    override suspend fun runCellFormula(
        formula: String,
        worksheetName: String,
        workbookKey: WorkbookKey,
        cellAddress: CellAddress,
    ): Result<String, ErrorReport> {

        val fm: String = formula.trim()
        val isFormula: Boolean = fm.startsWith("=")
        // x: post-processing and execute the translated code
        val sb = AllScriptBuilderImp()
            .getWorkbook(workbookKey)
            .getWorksheet(worksheetName)
            .cell(cellAddress)
        val assignmentCode: String =
            if (isFormula) {
                sb.writeFormula(fm).build()
            } else {
                val literalRs: Result<String, ErrorReport> = DirectLiteralTranslator.translate(fm)
                if (literalRs is Ok) {
                    sb.writeValue(literalRs.value).build()
                } else {
                    return literalRs
                }
            }
        // x: wrap everything in whole workbook template
        val finalCode: String = assignmentCode
        val output: Result<String, ErrorReport> = runner.run(finalCode)
        return output
    }

    override suspend fun renameSheetRs(
        workbookKey: WorkbookKey,
        oldName: String,
        newName: String,
    ): Result<String, ErrorReport> {
        val code = AllScriptBuilderImp()
            .getWorkbook(workbookKey)
            .renameWorksheetRs(oldName, newName)
            .build()
        return runner.run(code)
    }

    override suspend fun deleteSheet(workbookKey: WorkbookKey, sheetName: String): Result<String, ErrorReport> {
        TODO("Not yet implemented")
    }

    override suspend fun createNewSheetRs(workbookKey: WorkbookKey, newSheetName:String?):Result<String,ErrorReport>{
        val code = AllScriptBuilderImp()
            .getWorkbook(workbookKey)
            .createNewWorksheetRs(newSheetName)
            .build()
        return this.runner.run(code)
    }

//    suspend fun createREQSocketForUIUpdating(servicePort: Int) {
//        val code = """
//            context = getApp().zContext
//            socket = context.socket(zmq.REQ)
//            socket.connect("tcp://localhost:${servicePort}")
//            getApp().socketProvider.updateREQSocketForUIUpdating(socket)
//        """.trimIndent()
//        this.runner.run(code)
//    }
}
