package com.qxdzbc.p6.app.action.cell_editor.cycle_formula_lock_state

interface CycleFormulaLockStateAction {
    fun cycleFormulaLockState()
    fun cycleFormulaLockState(formula:String,cursorPos:Int):String?
}
