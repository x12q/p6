package com.qxdzbc.p6.app.action.cell_editor.update_range_selector_text

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import com.qxdzbc.p6.app.action.worksheet.make_cell_editor_display_text.MakeCellEditorDisplayTextAction
import com.qxdzbc.p6.di.state.app_state.CellEditorStateMs
import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorState
import com.qxdzbc.common.compose.Ms
import javax.inject.Inject

class UpdateRangeSelectorTextImp @Inject constructor(
    @CellEditorStateMs
    private val cellEditorStateMs:Ms<CellEditorState>,
    private val makeDisplayText: MakeCellEditorDisplayTextAction,
) : UpdateRangeSelectorText {
    private var cellEditorState by cellEditorStateMs
    override fun updateRangeSelectorText() {
        if(cellEditorState.isActiveAndAllowRangeSelector){
            val t:TextFieldValue = makeDisplayText.makeRangeSelectorText(cellEditorState)
            cellEditorStateMs.value = cellEditorState.setRangeSelectorTextField(t)
        }
    }
}
