package com.emeraldblast.p6.app.code.script_builder

import com.emeraldblast.p6.app.code.CodeTemplate
import com.emeraldblast.p6.app.document.cell.address.CellAddress
import com.emeraldblast.p6.app.document.workbook.WorkbookKey

class AllScriptBuilderImp(private val sb: StringBuilder = StringBuilder()) : AllScriptBuilder {

    override fun getActiveSheet(): WorksheetScriptBuilder {
        sb.append("getActiveWorksheet()")
        return this
    }

    override fun getSheetFromActiveWorkbook(sheetName: String): WorksheetScriptBuilder {
        sb.append("""getWorksheet("$sheetName")""")
        return this
    }

    override fun getActiveWorkbook(): WorkbookScriptBuilder {
        sb.append("getActiveWorkbook()")
        return this
    }

    override fun getWorkbook(workbookKey: WorkbookKey): WorkbookScriptBuilder {
        sb.append(CodeTemplate.getWorkbookTemplate(workbookKey))
        return this
    }

    override fun getWorkbook(index: Int): WorkbookScriptBuilder {
        sb.append("""getWorkbook($index)""")
        return this
    }

    override fun getWorksheet(sheetName: String): WorksheetScriptBuilder {
        sb.append(""".getWorksheet("$sheetName")""")
        return this
    }

    override fun getWorksheet(sheetIndex: Int): WorksheetScriptBuilder {
        sb.append(""".getWorksheet(${sheetIndex})""")
        return this
    }

    override fun setActiveSheet(sheetName: String): ScriptBuilder {
        sb.append(""".setActiveWorksheet("$sheetName")""")
        return this
    }

    override fun renameWorksheetRs(oldName: String, newName: String): ScriptBuilder {
        sb.append(""".renameWorksheetRs("$oldName","$newName")""")
        return this
    }

    override fun renameWorksheetRs(index: Int, newName: String): ScriptBuilder {
        sb.append(""".renameWorksheetRs($index,"$newName")""")
        return this
    }

    override fun createNewWorksheetRs(newSheetName: String?): ScriptBuilder {
        if(newSheetName==null){
            sb.append(""".createNewWorksheetRs()""")
        }else{
            sb.append(""".createNewWorksheetRs("$newSheetName")""")
        }
        return this
    }

    override fun cell(cellLabel: String): CellScriptBuilder {
        sb.append(""".cell("@$cellLabel")""")
        return this
    }

    override fun cell(cellAddress: CellAddress): CellScriptBuilder {
        sb.append(""".cell((${cellAddress.colIndex},${cellAddress.rowIndex}))""")
        return this
    }

    override fun readValue(): ScriptBuilder {
        sb.append(""".value""")
        return this
    }

    override fun readFormula(): ScriptBuilder {
        sb.append(""".formula""")
        return this
    }

    override fun writeValue(newValue: String): ScriptBuilder {
        sb.append(""".value=${newValue}""")
        return this
    }

    override fun writeFormula(newFormula: String): ScriptBuilder {
        if (newFormula.startsWith("\"") && newFormula.endsWith("\"")) {
            sb.append(""".formula=$newFormula""")
        } else {
            sb.append(""".formula="$newFormula"""")
        }
        return this
    }

    override fun build(): String {
        return sb.toString()
    }

    override fun clear(): ScriptBuilder {
        sb.clear()
        return this
    }

    override fun append(csq: CharSequence): ScriptBuilder {
        sb.append(csq)
        return this
    }

    override fun append(csq: CharSequence, start: Int, end: Int): ScriptBuilder {
        sb.append(csq, start, end)
        return this
    }

    override fun append(c: Char): ScriptBuilder {
        sb.append(c)
        return this
    }
}
