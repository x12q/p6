package com.qxdzbc.p6.app.document.cell

import androidx.compose.ui.text.AnnotatedString
import com.qxdzbc.common.CanCheckEmpty
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.StateUtils
import com.qxdzbc.p6.app.document.Shiftable
import com.qxdzbc.p6.app.document.cell.CellValue.Companion.toCellValue
import com.qxdzbc.p6.app.document.cell.address.CRAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.proto.DocProtos
import com.qxdzbc.p6.rpc.cell.msg.CellContentDM
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.qxdzbc.p6.translator.formula.execution_unit.primitive.IntUnit
import com.qxdzbc.p6.ui.common.color_generator.ColorMap

/**
 * CellContent = formula + cell value
 */
interface CellContent:CanCheckEmpty,Shiftable {
    val originalText:String?

    override fun shift(
        oldAnchorCell: CRAddress<Int, Int>,
        newAnchorCell: CRAddress<Int, Int>
    ): CellContent

    val exUnit: ExUnit?
    val cellValueAfterRun: CellValue
    val cellValue: CellValue

    fun toDm():CellContentDM

    /**
     * formula in its full form
     */
    val fullFormulaFromExUnit: String?

    /**
     * formula in its short form. A formula in short form omits workbook identity(name & path) and worksheet name if the storing workbook & worksheet are identical to the one being referred inside the formula.
     * Eg: A1@'Sheet1'@'Wb1' become A1 if this formula is stored in Wb1, Sheet1
     */
    fun shortFormulaFromExUnit(wbKey:WorkbookKey?=null, wsName:String?=null): String?

    /**
     * formula in colored short form
     */
    fun colorFormula(colorMap: ColorMap, wbKey: WorkbookKey?, wsName: String?): AnnotatedString?

    fun reRun(): CellContent?
    fun reRunRs():Rse<CellContent>
    val editableStr: String
    val displayText: String
    fun setValueAndDeleteExUnit(cv: CellValue): CellContent
    fun setCellValue(cv: CellValue): CellContent
    val isFormula: Boolean

    fun toProto(): DocProtos.CellContentProto

    companion object{
        fun randomNumericContent():CellContentImp{
            val num = (1 .. 1000).random()
            return CellContentImp(
                cellValueMs = StateUtils.ms(num.toCellValue()),
                exUnit = null,
                originalText = num.toString()
            )
        }
        fun randomExUnitContent():CellContentImp{
            val num=(1 .. 1000).random()
            return CellContentImp(
                cellValueMs = StateUtils.ms(CellValue.empty),
                exUnit = IntUnit(num),
                originalText = "=${num}"
            )
        }
    }
}

