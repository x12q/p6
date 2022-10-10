package com.qxdzbc.p6.app.action.cell_editor.cycle_formula_lock_state

import com.qxdzbc.p6.translator.cell_range_extractor.CellRangePosition

interface CycleFormulaLockStateAction {
    fun cycleFormulaLockState()
    fun cycleFormulaLockState(formula:String,cursorPos:Int):String?
    fun cycleFormulaLockState(formula: String, cellRangePosList: List<CellRangePosition>, cursorPos: Int): String?
}
