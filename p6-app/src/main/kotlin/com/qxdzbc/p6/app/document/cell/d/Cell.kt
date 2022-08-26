package com.qxdzbc.p6.app.document.cell.d

import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.proto.DocProtos.CellProto


interface Cell {

    fun reRun(): Cell

    val address: CellAddress
    val content: CellContent
    val formula: String?
    fun formula(wbKey: WorkbookKey? = null, wsName: String? = null): String?
    val displayValue: String

    /**
     * a shortcut to the cell value store in [content]
     */
    val cellValueAfterRun: CellValue
    val currentCellValue: CellValue
    val editableValue: String
    fun editableValue(wbKey: WorkbookKey?, wsName: String): String

    /**
     * a shortcut to the value stored in [cellValueAfterRun]
     */
    val valueAfterRun: Any?
    val currentValue: Any?
    val isFormula: Boolean
    val isEditable: Boolean
    fun setAddress(newAddress: CellAddress): Cell

    fun setCellValue(i: CellValue): Cell
    fun setContent(content: CellContent): Cell

    fun hasContent(): Boolean
    fun toProto(): CellProto
}

