package com.emeraldblast.p6.app.code

import com.emeraldblast.p6.app.document.cell.address.CellAddress
import com.emeraldblast.p6.app.document.workbook.WorkbookKey

/**
 * provide additional processing logic after translation
 */
object CodeTemplate {

    fun directLiteral(
        directLiteral: String,
        cellAddress: CellAddress,
        sheetName: String,
        workbookKey: WorkbookKey,
    ): String {
        val rt = """
        ${getCellFullPathTemplate(workbookKey, sheetName, cellAddress.toRawLabel())}.value = $directLiteral    
        """.trimIndent()
        return rt
    }
    private fun formatAddress(rangeAddress:String):String{
        return  "\"@${rangeAddress}\""
    }

    fun formula(code: String, cellAddress: CellAddress, sheetName: String, workbookKey: WorkbookKey): String {
        val label: String = formatAddress(cellAddress.toRawLabel())
        val rt = """
        ${getCellFullPathTemplate(workbookKey, sheetName, cellAddress.toRawLabel())}.cell($label)
        """.trimIndent() + ".formula = \"\"\"\n$code\n\"\"\".strip()"
        return rt
    }

    fun createREQSocketForUIUpdating(socketPort: Int): String {
        return """
            context = getApp().zContext
            socket = context.socket(zmq.REQ)
            socket.connect("tcp://localhost:${socketPort}")
            getApp().socketProvider.updateREQSocketForUIUpdating(socket)
        """.trimIndent()
    }

    fun getWorkbookTemplate(workbookKey: WorkbookKey): String {
        return """getWorkbook(WorkbookKeys.fromNameAndPath("${workbookKey.name}",${workbookKey.pathScript()}))"""
    }

    fun getActiveWorkbookTemplate():String{
        return """getWorkbook()"""
    }

    fun getWorksheetFromWbTemplate(sheetName: String): String {
        return """getWorksheet("$sheetName")"""
    }

    fun getCellFromSheetTemplate(labelOrAdressIndex: String): String {
        return """cell("@$labelOrAdressIndex")"""
    }

    fun getCellFullPathTemplate(workbookKey: WorkbookKey, worksheetName: String, cellLabelOrAddress: String): String {
        return """${getWorkbookTemplate(workbookKey)}.${getWorksheetFromWbTemplate(worksheetName)}.${
            getCellFromSheetTemplate(cellLabelOrAddress)
        }"""
    }

    fun getSheetFullPathTemplate(workbookKey: WorkbookKey, worksheetName: String): String {
        return """${getWorkbookTemplate(workbookKey)}.${getWorksheetFromWbTemplate(worksheetName)}"""
    }
}

