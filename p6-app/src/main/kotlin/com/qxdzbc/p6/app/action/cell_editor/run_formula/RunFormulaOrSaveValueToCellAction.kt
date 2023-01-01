package com.qxdzbc.p6.app.action.cell_editor.run_formula

interface RunFormulaOrSaveValueToCellAction{
    /**
     * run the current formula inside the cell editor if there's a formula. Otherwise, parse and save the current value in the editor into the target cell
     */
    fun runFormulaOrSaveValueToCell(undoable:Boolean)
}
