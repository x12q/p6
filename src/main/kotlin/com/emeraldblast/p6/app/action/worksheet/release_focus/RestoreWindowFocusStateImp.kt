package com.emeraldblast.p6.app.action.worksheet.release_focus

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.emeraldblast.p6.app.common.utils.Rse
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.di.state.app_state.AppStateMs
import com.emeraldblast.p6.ui.app.state.AppState
import com.emeraldblast.p6.ui.common.compose.Ms
import com.github.michaelbull.result.*
import javax.inject.Inject

class RestoreWindowFocusStateImp @Inject constructor(
    @AppStateMs
    val appStateMs: Ms<AppState>,
) : RestoreWindowFocusState {

    var appState by appStateMs

    /**
     * clear and close cell editor, then restore window focus state to default
     */
    override fun restoreCellEditorAndCursorState(): Rse<Unit> {
        appState.cellEditorState = appState.cellEditorState.clearAllText().close()
        appState.windowStateMsList.map {
            it.value.focusStateMs
        }.forEach {
            it.value = it.value.restoreDefault()
        }
        return Ok(Unit)
    }

    override fun restoreAllWsFocusIfAllow(): Rse<Unit> {
        val cellEditorState by appState.cellEditorStateMs
        if (!cellEditorState.allowRangeSelector) {
            return this.restoreCellEditorAndCursorState()
        } else {
            return Ok(Unit)
        }
    }

    override fun setFocusConsideringRangeSelectorAllWindow(): Rse<Unit> {
        if (appState.cellEditorState.allowRangeSelector) {
            appState.windowStateMsList.forEach {wds->
                wds.value.focusState = wds.value.focusState.focusOnEditor().freeFocusOnCursor()
            }
        }else{
            restoreCellEditorAndCursorState()
        }
        return Ok(Unit)
    }

    override fun setFocusStateConsideringRangeSelector(wbKey: WorkbookKey): Rse<Unit> {
        val cellEditorState = appState.cellEditorState
        val selectingRangeForEditor =cellEditorState.isActive && cellEditorState.allowRangeSelector
        if (selectingRangeForEditor) {
            appState.getWindowStateMsByWbKey(wbKey)?.also {wds->
                wds.value.focusState = wds.value.focusState.focusOnEditor().freeFocusOnCursor()
            }
        }else{
            restoreCellEditorAndCursorState()
        }
        return Ok(Unit)
    }
}
