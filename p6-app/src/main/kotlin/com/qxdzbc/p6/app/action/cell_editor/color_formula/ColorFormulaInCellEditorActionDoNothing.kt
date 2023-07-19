package com.qxdzbc.p6.app.action.cell_editor.color_formula

import com.qxdzbc.p6.di.DoNothing
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorState
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@DoNothing
@ContributesBinding(P6AnvilScope::class, boundType = ColorFormulaInCellEditorAction::class)
class ColorFormulaInCellEditorActionDoNothing @Inject constructor() : ColorFormulaInCellEditorAction {
    override fun colorCurrentTextInCellEditor() {}

    override fun colorCurrentTextInCellEditor(cellEditorState: CellEditorState): CellEditorState {
        return cellEditorState
    }

    override fun colorDisplayTextInCellEditor(cellEditorState: CellEditorState): CellEditorState {
        return cellEditorState
    }
}