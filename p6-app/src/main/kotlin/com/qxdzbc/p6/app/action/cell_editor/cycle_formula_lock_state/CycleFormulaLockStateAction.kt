package com.qxdzbc.p6.app.action.cell_editor.cycle_formula_lock_state

import com.qxdzbc.p6.translator.partial_text_element_extractor.text_element.CellRangeElement

interface CycleFormulaLockStateAction {
    fun cycleFormulaLockState()
    fun cycleFormulaLockState(formula:String,cursorPos:Int):String?
    fun cycleFormulaLockState(formula: String, cellRangePosList: List<CellRangeElement>, cursorPos: Int): String?
}
