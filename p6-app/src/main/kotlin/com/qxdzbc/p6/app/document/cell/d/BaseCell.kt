package com.qxdzbc.p6.app.document.cell.d

import androidx.compose.ui.text.AnnotatedString
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.ui.common.color_generator.ColorProvider

abstract class BaseCell : Cell {
    override fun formula(wbKey: WorkbookKey?, wsName: String?): String? {
        return content.formula(wbKey, wsName)
    }

    override fun colorEditableValue(
        colorProvider: ColorProvider,
        wbKey: WorkbookKey?,
        wsName: String
    ): AnnotatedString {
        if(this.isFormula){
            return this.content.colorFormula(colorProvider,wbKey, wsName) ?: AnnotatedString("")
        }else{
            return AnnotatedString(this.cellValueAfterRun.editableValue ?: "")
        }
    }

    override fun editableValue(wbKey: WorkbookKey?, wsName: String): String {
        println("H3")
        if(this.isFormula){
            return this.formula(wbKey, wsName) ?: ""
        }else{
            return this.cellValueAfterRun.editableValue ?: ""
        }
    }

    override val editableValue: String
        get() {
            println("H2")
            if(this.isFormula){
                return this.formula ?: ""
            }else{
                return this.cellValueAfterRun.editableValue ?: ""
            }
        }
    override val formula: String? get() = content.formula
    override val displayValue: String get() {
        // this
        println("H1")
        try{
            return content.displayValue
        }catch (e:Throwable){
            return "ERR"
        }
    }
    override val isEditable: Boolean get() = true
    override fun hasContent(): Boolean {
        return content.isNotEmpty()
    }
    override val cellValueAfterRun: CellValue get() = content.cellValueAfterRun
    override val valueAfterRun: Any? get() = this.cellValueAfterRun.valueAfterRun
    override val currentCellValue: CellValue
        get() = content.currentCellValue
    override val currentValue: Any?
        get() = content.currentCellValue.currentValue
    override val isFormula: Boolean get() = content.isFormula
}
