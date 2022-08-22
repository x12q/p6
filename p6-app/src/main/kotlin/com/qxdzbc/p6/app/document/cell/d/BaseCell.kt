package com.qxdzbc.p6.app.document.cell.d

abstract class BaseCell : Cell {
    override val editableValue: String
        get() {
            if(this.isFormula){
                return this.formula ?: ""
            }else{
                return this.cellValueAfterRun.editableValue ?: ""
            }
        }
    override val formula: String? get() = content.formula
    override val displayValue: String get() {
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
