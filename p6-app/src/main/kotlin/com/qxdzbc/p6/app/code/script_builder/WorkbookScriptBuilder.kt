package com.qxdzbc.p6.app.code.script_builder

interface WorkbookScriptBuilder : ScriptBuilder {
    fun getWorksheet(sheetName: String): WorksheetScriptBuilder
    fun getWorksheet(sheetIndex: Int): WorksheetScriptBuilder
    fun setActiveSheet(sheetName:String): ScriptBuilder
    fun renameWorksheetRs(oldName:String, newName:String):ScriptBuilder
    fun renameWorksheetRs(index:Int, newName:String):ScriptBuilder
    fun createNewWorksheetRs(newSheetName:String?=null):ScriptBuilder
}
