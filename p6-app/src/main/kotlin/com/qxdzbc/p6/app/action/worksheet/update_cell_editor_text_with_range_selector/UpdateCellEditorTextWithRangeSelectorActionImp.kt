package com.qxdzbc.p6.app.action.worksheet.update_cell_editor_text_with_range_selector

import androidx.compose.runtime.getValue
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.worksheet.make_cell_editor_display_text.MakeCellEditorDisplayTextAction
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope

import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class UpdateCellEditorTextWithRangeSelectorActionImp @Inject constructor(
    private val stateContSt: St<@JvmSuppressWildcards StateContainer>,
    private val makeCellEditorText: MakeCellEditorDisplayTextAction,
) : UpdateCellEditorTextWithRangeSelectorAction {
    private val sc by stateContSt
    override fun updateRangeSelectorText() {
        if (sc.cellEditorState.isActiveAndAllowRangeSelector) {
            val text = makeCellEditorText.makeRangeSelectorText(sc.cellEditorState)
            sc.cellEditorState = sc.cellEditorState.setRangeSelectorTextField(text)
        }
    }
}
