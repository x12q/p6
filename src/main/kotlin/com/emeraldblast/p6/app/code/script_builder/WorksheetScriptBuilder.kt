package com.emeraldblast.p6.app.code.script_builder

import com.emeraldblast.p6.app.document.cell.address.CellAddress

interface WorksheetScriptBuilder : ScriptBuilder {
    fun cell(cellLabel: String): CellScriptBuilder
    fun cell(cellAddress: CellAddress): CellScriptBuilder
}
