package com.qxdzbc.p6.ui.document.worksheet

import androidx.compose.runtime.getValue
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.worksheet.make_cell_editor_display_text.MakeCellEditorDisplayTextAction
import com.qxdzbc.p6.di.state.app_state.StateContainerSt
import com.qxdzbc.p6.ui.app.state.StateContainer
import javax.inject.Inject

class UpdateCellEditorTextWithRangeSelectorActionImp @Inject constructor(
    @StateContainerSt
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
