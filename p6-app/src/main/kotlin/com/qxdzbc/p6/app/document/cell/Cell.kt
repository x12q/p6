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
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.qxdzbc.p6.ui.common.color_generator.ColorMap
import kotlin.jvm.Throws


interface Cell : Shiftable, WbWsSt {

    /**
     * This is error caused by evaluation that happens outside of cell content.
     * The purpose of this [ErrorReport] is to store error throws by something that happens outside of cell's scope.
     * At the moment, it is only used for storing stack overflow caused by circular reference between cells.
     */
    val externalEvalError: ErrorReport?

    fun setExternalEvalError(i: ErrorReport?): Cell

    /**
     * A cell is similar to another cell if they are in the same ws, same wb, and their content is similar
     */
    fun isSimilar(c: Cell): Boolean

    override fun shift(
        oldAnchorCell: CRAddress<Int, Int>,
        newAnchorCell: CRAddress<Int, Int>
    ): Cell

    /**
     * Rerun a cell, return a null if something wrong happens
     */
    fun reRun(): Cell?

    /**
     * Rerun a cell
     */
    fun reRunRs(): Rse<Cell>

    /**
     * A cell's address never changes, so no need for a Ms
     */
    val id: CellId

    val address: CellAddress

    val content: CellContent

    /**
     * a full formula reconstructed from the [ExUnit] inside this cell. Null if this cell does not hold any [ExUnit]
     */
    val fullFormulaFromExUnit: String?

    /**
     * a short formula reconstructed from the [ExUnit] inside this cell. Null if this cell does not hold any [ExUnit].
     * A short formula is formula that omit workbook and worksheet prefix for cells and ranges in [ExUnit], that are in the same workbook and worksheet as this [Cell].
     */
    val shortFormulaFromExUnit: String?

    /**
     * Construct a short formula using the [ExUnit] in this [Cell], omit workbook and worksheet prefix of all cells and ranges in the [ExUnit] if they have [wbKey] and [wsName] respectively.
     */
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
    val cachedDisplayText: String

    /**
     * Evaluate display text (aka run the cell), stored the result in [cachedDisplayText]
     */
    fun evaluateDisplayText(): Cell

    /**
     * reRun this [Cell] and return its new [CellValue]
     */
    val cellValueAfterRun: CellValue

    /**
     * this is the cached value of this [Cell]
     */
    val currentCellValue: CellValue

    /**
     * The text that can be put into cell editor to edit
     */
    val editableText: String

    /**
     * TODO at this time, this function does not do anything, it just return [editableText] above.
     * [wbKey] and [wsName] are useless.
     */
    fun editableText(wbKey: WorkbookKey?, wsName: String): String

    /**
     * The text that can be put into cell editor to edit, but with colors highlighting cells and ranges
     */
    fun colorEditableText(colorMap: ColorMap, wbKey: WorkbookKey?, wsName: String): AnnotatedString

    /**
     * reRun the cell, refresh the internal value cache, then return it
     */
    val valueAfterRun: Any?

    val currentValue: Any?

    /**
     * [currentValue] as csv string
     */
    val currentValueAsCsvStr: String

    /**
     * true if this cell contains a formula, false otherwise.
     */
    val isFormula: Boolean

    /**
     * if this cell can be edited.
     * TODO at this point, this is always true
     */
    val isEditable: Boolean

    /**
     * Move this cell to a new [CellAddress]
     */
    fun setAddress(newAddress: CellAddress): Cell

    fun setCellValue(i: CellValue): Cell

    fun setContent(content: CellContent): Cell

    /**
     * if [content] is empty or not
     */
    fun isContentEmpty(): Boolean

    /**
     * convert to a proto obj
     */
    fun toProto(): CellProto

    /**
     * Convert to DirectMapping (DM) object
     */
    fun toDm(): CellDM

    companion object {
        fun random(address: CellAddress): Cell {
            return IndCellImp(address, content = CellContent.randomNumericContent())
        }
    }
}

