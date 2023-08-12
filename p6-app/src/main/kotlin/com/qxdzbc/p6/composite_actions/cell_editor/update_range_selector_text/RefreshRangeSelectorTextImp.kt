package com.qxdzbc.p6.composite_actions.cell_editor.update_range_selector_text

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.composite_actions.cell_editor.color_formula.ColorFormulaInCellEditorAction
import com.qxdzbc.p6.composite_actions.worksheet.make_cell_editor_display_text.GenerateCellEditorTextAction
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.translator.jvm_translator.tree_extractor.TreeExtractor
import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorState
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class)
class RefreshRangeSelectorTextImp @Inject constructor(
    private val cellEditorStateMs: Ms<CellEditorState>,
    private val makeDisplayText: GenerateCellEditorTextAction,
    val treeExtractor: TreeExtractor,
    private val colorFormulaAction:ColorFormulaInCellEditorAction,
) : RefreshRangeSelectorText {

    private var cellEditorState by cellEditorStateMs

    override fun refreshRangeSelectorTextInCurrentCellEditor() {
        cellEditorStateMs.value = this.refreshRangeSelectorText(cellEditorState)
    }

    override fun refreshRangeSelectorText(cellEditorState: CellEditorState): CellEditorState {
        if (cellEditorState.isActiveAndAllowRangeSelector) {
            val tf: TextFieldValue = makeDisplayText.generateRangeSelectorText(cellEditorState)
            val rt = colorFormulaAction.colorDisplayTextInCellEditor(cellEditorState.setRangeSelectorTextField(tf))
            return rt
        } else {
            return cellEditorState
        }
    }
}
