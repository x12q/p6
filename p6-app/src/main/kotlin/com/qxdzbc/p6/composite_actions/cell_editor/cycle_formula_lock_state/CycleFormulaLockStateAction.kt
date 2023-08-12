package com.qxdzbc.p6.composite_actions.cell_editor.cycle_formula_lock_state

import com.qxdzbc.p6.translator.partial_text_element_extractor.text_element.CellRangeElement

/**
 * cycle lock state of a cell, range address inside a formula
 */
interface CycleFormulaLockStateAction {
    /**
     * cycle lock state of a cell,range address inside the current formula in the active cell editor.
     */
    fun cycleFormulaLockState()

    /**
     * cycle lock state of a cell, range address at [cursorPos] inside [formula]
     */
    fun cycleFormulaLockState(formula:String,cursorPos:Int):String?

    /**
     * cycle lock state of a cell, range address at [cursorPos] inside [formula]. [cellRangePosList] denote the cell range address position inside the [formula].
     */
    fun cycleFormulaLockState(formula: String, cellRangePosList: List<CellRangeElement>, cursorPos: Int): String?
}
