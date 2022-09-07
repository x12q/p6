package com.qxdzbc.p6.app.document.cell.d

import androidx.compose.ui.text.AnnotatedString
import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.document.Shiftable
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.address.GenericCellAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.proto.DocProtos.CellProto
import com.qxdzbc.p6.ui.common.color_generator.ColorProvider


interface Cell :Shiftable{
    override fun shift(
        oldAnchorCell: GenericCellAddress<Int, Int>,
        newAnchorCell: GenericCellAddress<Int, Int>
    ): Cell

    fun reRun(): Cell?
    fun reRunRs():Rse<Cell>

    /**
     * A cell's address never changes, so no need for a Ms
     */
    val address: CellAddress
    val content: CellContent
    val formula: String?
    fun formula(wbKey: WorkbookKey? = null, wsName: String? = null): String?

    /**
     * value to be displayed on the cell UI
     */
    val displayValue: String

    /**
     * a shortcut to the [CellValue] store in [content]
     */
    val cellValueAfterRun: CellValue
    val currentCellValue: CellValue
    val editableValue: String
    fun editableValue(wbKey: WorkbookKey?, wsName: String): String
    fun colorEditableValue(colorProvider: ColorProvider, wbKey: WorkbookKey?, wsName: String): AnnotatedString

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

