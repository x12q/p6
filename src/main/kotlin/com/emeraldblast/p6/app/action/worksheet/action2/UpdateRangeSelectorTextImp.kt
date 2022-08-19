package com.emeraldblast.p6.app.action.worksheet.action2

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.emeraldblast.p6.app.action.worksheet.make_cell_editor_display_text.MakeCellEditorDisplayText
import com.emeraldblast.p6.di.state.app_state.AppStateMs
import com.emeraldblast.p6.di.state.app_state.CellEditorStateMs
import com.emeraldblast.p6.ui.app.cell_editor.in_cell.state.CellEditorState
import com.emeraldblast.p6.ui.app.state.AppState
import com.emeraldblast.p6.ui.common.compose.Ms
import javax.inject.Inject

class UpdateRangeSelectorTextImp @Inject constructor(
    @CellEditorStateMs
    private val cellEditorStateMs:Ms<CellEditorState>,
    private val makeDisplayText: MakeCellEditorDisplayText,
) : UpdateRangeSelectorText {
    private var cellEditorState by cellEditorStateMs
    override fun updateRangeSelectorText() {
        if(cellEditorState.isActiveAndAllowRangeSelector){
            val t = makeDisplayText.makeRangeSelectorText(cellEditorState)
            cellEditorStateMs.value = cellEditorState.setRangeSelectorText(t)
        }
    }
}
