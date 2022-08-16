package com.emeraldblast.p6.app.code.script_builder

import com.emeraldblast.p6.app.document.workbook.WorkbookKey

interface UserFunctionScriptBuilder : ScriptBuilder {
    fun getActiveSheet(): WorksheetScriptBuilder
    fun getActiveWorkbook(): WorkbookScriptBuilder
    fun getSheetFromActiveWorkbook(sheetName: String): WorksheetScriptBuilder
    fun getWorkbook(workbookKey: WorkbookKey): WorkbookScriptBuilder
    fun getWorkbook(index: Int): WorkbookScriptBuilder
}
