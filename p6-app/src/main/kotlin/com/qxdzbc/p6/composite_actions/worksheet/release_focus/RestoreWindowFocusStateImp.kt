package com.qxdzbc.p6.composite_actions.worksheet.release_focus

import androidx.compose.runtime.getValue
import com.qxdzbc.common.Rse
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey

import com.qxdzbc.common.compose.Ms
import com.github.michaelbull.result.*
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorState
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class)
class RestoreWindowFocusStateImp @Inject constructor(
    private val sc:StateContainer,
    private val cellEditorStateMs:Ms<CellEditorState>,
) : RestoreWindowFocusState {

    /**
     * clear and close cell editor, then restore window focus state to default
     */
    override fun restoreAllWindowFocusState(): Rse<Unit> {
        sc.windowStateMsList.map {
            it.focusStateMs
        }.forEach {
            it.value = it.value.restoreDefault()
        }
        return Ok(Unit)
    }

    override fun restoreAllWsFocusIfRangeSelectorIsNotActive(): Rse<Unit> {
        val cellEditorState by cellEditorStateMs
        if (!cellEditorState.allowRangeSelector) {
            return this.restoreAllWindowFocusState()
        } else {
            return Ok(Unit)
        }
    }

    override fun setFocusConsideringRangeSelectorAllWindow(): Rse<Unit> {
        if (cellEditorStateMs.value.allowRangeSelector) {
            sc.windowStateMsList.forEach { wds->
                wds.focusState = wds.focusState.focusOnEditor().freeFocusOnCursor()
            }
        }else{
            restoreAllWindowFocusState()
        }
        return Ok(Unit)
    }

    override fun setFocusStateConsideringRangeSelector(wbKey: WorkbookKey): Rse<Unit> {
        val cellEditorState = cellEditorStateMs.value
        val selectingRangeForEditor =cellEditorState.isOpen && cellEditorState.allowRangeSelector
        if (selectingRangeForEditor) {
            sc.getWindowStateMsByWbKey(wbKey)?.also { wds->
                wds.focusState = wds.focusState.freeFocusOnCursor().focusOnEditor()
            }
        }else{
            restoreAllWindowFocusState()
        }
        return Ok(Unit)
    }
}
