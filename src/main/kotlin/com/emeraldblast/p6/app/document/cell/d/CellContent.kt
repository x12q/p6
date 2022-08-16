package com.emeraldblast.p6.app.document.cell.d

import com.emeraldblast.p6.common.CanCheckEmpty
import com.emeraldblast.p6.ui.common.compose.Ms

interface CellContent:CanCheckEmpty {

    val cellValueMs:Ms<CellValue>
    val cellValueAfterRun: CellValue
    val currentCellValue: CellValue

    val formula: String?

    fun reRun():CellContent?
    val editableContent: String
    val displayValue: String
    fun setValue(cv: CellValue): CellContent
    fun setFormula(newFormula: String): CellContent
    val isFormula: Boolean
}

