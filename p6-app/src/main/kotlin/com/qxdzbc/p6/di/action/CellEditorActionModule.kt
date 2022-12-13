package com.qxdzbc.p6.di.action

import com.qxdzbc.p6.app.action.cell_editor.color_formula.ColorFormulaInCellEditorAction
import com.qxdzbc.p6.app.action.cell_editor.color_formula.ColorFormulaInCellEditorActionImp
import com.qxdzbc.p6.app.action.cell_editor.cycle_formula_lock_state.CycleFormulaLockStateAction
import com.qxdzbc.p6.app.action.cell_editor.cycle_formula_lock_state.CycleFormulaLockStateImp
import com.qxdzbc.p6.app.action.cell_editor.open_cell_editor.OpenCellEditorAction
import com.qxdzbc.p6.app.action.cell_editor.open_cell_editor.OpenCellEditorImp
import com.qxdzbc.p6.app.action.cell_editor.update_range_selector_text.UpdateRangeSelectorText
import com.qxdzbc.p6.app.action.cell_editor.update_range_selector_text.UpdateRangeSelectorTextImp
import com.qxdzbc.p6.di.P6Singleton
import dagger.Binds

@dagger.Module
interface CellEditorActionModule {
}
