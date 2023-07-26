package com.qxdzbc.p6.app.document.cell

import androidx.compose.ui.text.AnnotatedString
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.rpc.cell.msg.CellDM
import com.qxdzbc.p6.ui.common.color_generator.ColorMap

abstract class BaseCell : Cell {
    override fun reRun(): Cell? {
        return reRunRs().component1()
    }

    override fun isSimilar(c: Cell): Boolean {
        val sameAddress = id.isSimilar(c.id)
        // TODO check this similar check. Does not look good.
        val similarContent = content == c.content
        return sameAddress && similarContent
    }

    override fun shortFormulaFromExUnit(wbKey: WorkbookKey?, wsName: String?): String? {
        return content.shortFormulaFromExUnit(wbKey, wsName)
    }

    override fun colorEditableText(
        colorMap: ColorMap,
        wbKey: WorkbookKey?,
        wsName: String
    ): AnnotatedString {
        if (this.isFormula) {
            return this.content.colorFormula(colorMap, wbKey, wsName) ?: AnnotatedString("")
        } else {
            return AnnotatedString(this.cellValueAfterRun.editableValue ?: "")
        }
    }

    override fun editableText(wbKey: WorkbookKey?, wsName: String): String {
        return editableText
    }

    override val editableText: String
        get() {
            return this.content.originalText?:""
        }
    override val fullFormulaFromExUnit: String? get() = content.fullFormulaFromExUnit
    override val shortFormulaFromExUnit: String? get() = content.shortFormulaFromExUnit(this.wbKey, this.wsName)
    override val isEditable: Boolean get() = true
    override fun isContentEmpty(): Boolean {
        return content.isNotEmpty()
    }

    override val cellValueAfterRun: CellValue get() = content.cellValueAfterRun

    override val valueAfterRun: Any?
        get() {
            val cv = this.cellValueAfterRun
            val rt = cv.value
            return rt
        }
    override val currentCellValue: CellValue
        get() = content.cellValue
    override val currentValue: Any?
        get() = content.cellValue.value
    override val currentValueAsCsvStr: String
        get(){
            val cr = currentValue
            return when(cr){
                null -> ""
                Double,Int,Float,Short,Byte -> cr.toString()
                else ->"${cr}"
            }
        }
    override val isFormula: Boolean get() = content.isFormula


    override fun toDm(): CellDM {
        return CellDM(
            id=this.id.toDm(),
            content = this.content.toDm()
        )
    }
}
