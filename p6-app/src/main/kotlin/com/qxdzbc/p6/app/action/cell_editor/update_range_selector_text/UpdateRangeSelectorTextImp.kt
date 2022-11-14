package com.qxdzbc.p6.app.action.cell_editor.update_range_selector_text

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import com.qxdzbc.p6.app.action.worksheet.make_cell_editor_display_text.MakeCellEditorTextAction
import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorState
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class UpdateRangeSelectorTextImp @Inject constructor(
    private val cellEditorStateMs:Ms<CellEditorState>,
    private val makeDisplayText: MakeCellEditorTextAction,
) : UpdateRangeSelectorText {
    private var cellEditorState by cellEditorStateMs
    override fun updateRangeSelectorText() {
        if(cellEditorState.isActiveAndAllowRangeSelector){
            val t:TextFieldValue = makeDisplayText.makeRangeSelectorText(cellEditorState)
            cellEditorStateMs.value = cellEditorState.setRangeSelectorTextField(t)
        }
    }
}
