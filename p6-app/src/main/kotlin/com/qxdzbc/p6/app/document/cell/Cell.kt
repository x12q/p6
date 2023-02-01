package com.qxdzbc.p6.app.document.cell

import androidx.compose.ui.text.AnnotatedString
import com.qxdzbc.common.Rse
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.document.Shiftable
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.address.CRAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.proto.DocProtos.CellProto
import com.qxdzbc.p6.rpc.cell.msg.CellDM
import com.qxdzbc.p6.ui.common.color_generator.ColorMap
import kotlin.jvm.Throws


interface Cell :Shiftable,WbWsSt{

    /**
     * This is error caused by evaluation that happens outside of cell content
     */
    val externalEvalError: ErrorReport?
    fun setExternalEvalError(i: ErrorReport?):Cell

    /**
     * A cell is similar to another cell if they are in the same ws, same wb, and their content is similar
     */
    fun isSimilar(c: Cell):Boolean

    override fun shift(
        oldAnchorCell: CRAddress<Int, Int>,
        newAnchorCell: CRAddress<Int, Int>
    ): Cell

    fun reRun(): Cell?
    fun reRunRs():Rse<Cell>

    /**
     * A cell's address never changes, so no need for a Ms
     */
    val id:CellId
    val address: CellAddress

    val content: CellContent
    val fullFormulaFromExUnit: String?
    val shortFormulaFromExUnit: String?
    fun shortFormulaFromExUnit(wbKey: WorkbookKey? = null, wsName: String? = null): String?

    /**
     * try to access and return display text, may throw exception.
     * This should not be used to get the display text.
     */
    @Throws(Throwable::class)
    fun attemptToAccessDisplayText(): String

    /**
     * cached display text is what shown on the cell view.
     */
    val cachedDisplayText:String

    /**
     * Evaluate display text, stored the result in [cachedDisplayText]
     */
    fun evaluateDisplayText():Cell

    /**
     * a shortcut to the [CellValue] store in [content]
     */
    val cellValueAfterRun: CellValue
    val currentCellValue: CellValue

    val editableText: String
    fun editableText(wbKey: WorkbookKey?, wsName: String): String
    fun colorEditableValue(colorMap: ColorMap, wbKey: WorkbookKey?, wsName: String): AnnotatedString

    /**
     * reRun the cell, refresh the internal value cache, then return it
     */
    val valueAfterRun: Any?
    val currentValue: Any?
    val currentValueAsCsvStr:String
    val isFormula: Boolean
    val isEditable: Boolean
    fun setAddress(newAddress: CellAddress): Cell

    fun setCellValue(i: CellValue): Cell
    fun setContent(content: CellContent): Cell

    fun hasContent(): Boolean
    fun toProto(): CellProto
    fun toDm():CellDM

    companion object{
        fun random(address: CellAddress):Cell{
             return IndCellImp(address, content = CellContent.randomNumericContent())
        }
    }
}

