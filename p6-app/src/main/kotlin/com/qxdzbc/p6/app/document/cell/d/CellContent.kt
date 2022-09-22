package com.qxdzbc.p6.app.document.cell.d

import androidx.compose.ui.text.AnnotatedString
import com.qxdzbc.common.CanCheckEmpty
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.document.Shiftable
import com.qxdzbc.p6.app.document.cell.address.GenericCellAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.proto.DocProtos
import com.qxdzbc.p6.rpc.cell.msg.CellContentDM
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

    fun toDm():CellContentDM

    /**
     * formula in its full form
     */
    val fullFormula: String?

    /**
     * formula in its short form. A formula in short form omits workbook identity(name & path) and worksheet name if the storing workbook & worksheet are identical to the one being referred inside the formula.
     * Eg: A1@'Sheet1'@'Wb1' become A1 if this formula is stored in Wb1, Sheet1
     */
    fun shortFormula(wbKey:WorkbookKey?=null, wsName:String?=null): String?

    /**
     * formula in colored short form
     */
    fun colorFormula(colorProvider: ColorProvider, wbKey: WorkbookKey?, wsName: String?): AnnotatedString?

    fun reRun():CellContent?
    fun reRunRs():Rse<CellContent>
    val editableStr: String
    val displayStr: String
    fun setValue(cv: CellValue): CellContent
    val isFormula: Boolean

    fun toProto(): DocProtos.CellContentProto
}

