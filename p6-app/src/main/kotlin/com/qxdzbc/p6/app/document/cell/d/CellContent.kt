package com.qxdzbc.p6.app.document.cell.d

import androidx.compose.ui.text.AnnotatedString
import com.qxdzbc.common.CanCheckEmpty
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.document.Shiftable
import com.qxdzbc.p6.app.document.cell.address.GenericCellAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.qxdzbc.p6.ui.common.color_generator.ColorProvider

interface CellContent:CanCheckEmpty,Shiftable {
    override fun shift(
        oldAnchorCell: GenericCellAddress<Int, Int>,
        newAnchorCell: GenericCellAddress<Int, Int>
    ): CellContent
    val exUnit: ExUnit?
    val cellValueMs:Ms<CellValue>
    val cellValueAfterRun: CellValue
    val currentCellValue: CellValue

    val formula: String?
    fun formula(wbKey:WorkbookKey?=null, wsName:String?=null): String?

    fun reRun():CellContent?
    val editableContent: String
    val displayValue: String
    fun setValue(cv: CellValue): CellContent
    val isFormula: Boolean
    fun colorFormula(colorProvider: ColorProvider, wbKey: WorkbookKey?, wsName: String?): AnnotatedString?
}

