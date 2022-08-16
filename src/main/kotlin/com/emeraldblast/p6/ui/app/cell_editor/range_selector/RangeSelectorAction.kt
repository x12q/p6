package com.emeraldblast.p6.ui.app.cell_editor.range_selector

import androidx.compose.ui.input.key.KeyEvent
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.document.worksheet.ruler.RulerState
import com.emeraldblast.p6.ui.document.worksheet.state.WorksheetState

interface RangeSelectorAction {
    fun handleKeyboardEvent(keyEvent: KeyEvent, state: RangeSelectorState): Boolean
    fun up(
        rangeSelectorStateMs:Ms<RangeSelectorState>,
        wsStateMs: Ms<WorksheetState>,
        colRulerStateMs: Ms<RulerState>,
        rowRulerStateMs: Ms<RulerState>
    )

    fun down(
        rangeSelectorStateMs:Ms<RangeSelectorState>,
        wsStateMs: Ms<WorksheetState>,
        colRulerStateMs: Ms<RulerState>,
        rowRulerStateMs: Ms<RulerState>
    )

    fun left(
        rangeSelectorStateMs:Ms<RangeSelectorState>,
        wsStateMs: Ms<WorksheetState>,
        colRulerStateMs: Ms<RulerState>,
        rowRulerStateMs: Ms<RulerState>
    )

    fun right(
        rangeSelectorStateMs:Ms<RangeSelectorState>,
        wsStateMs: Ms<WorksheetState>,
        colRulerStateMs: Ms<RulerState>,
        rowRulerStateMs: Ms<RulerState>
    )

    fun ctrlLeft(
        rangeSelectorStateMs:Ms<RangeSelectorState>,
        wsStateMs: Ms<WorksheetState>,
        colRulerStateMs: Ms<RulerState>,
        rowRulerStateMs: Ms<RulerState>
    )

    fun ctrlRight(
        rangeSelectorStateMs:Ms<RangeSelectorState>,
        wsStateMs: Ms<WorksheetState>,
        colRulerStateMs: Ms<RulerState>,
        rowRulerStateMs: Ms<RulerState>
    )

    fun ctrlDown(
        rangeSelectorStateMs:Ms<RangeSelectorState>,
        wsStateMs: Ms<WorksheetState>,
        colRulerStateMs: Ms<RulerState>,
        rowRulerStateMs: Ms<RulerState>
    )

    fun end(
        rangeSelectorStateMs:Ms<RangeSelectorState>,
        wsStateMs: Ms<WorksheetState>,
        colRulerStateMs: Ms<RulerState>,
        rowRulerStateMs: Ms<RulerState>
    )

    fun shiftUp(
        rangeSelectorStateMs:Ms<RangeSelectorState>,
        wsStateMs: Ms<WorksheetState>,
        colRulerStateMs: Ms<RulerState>,
        rowRulerStateMs: Ms<RulerState>
    )

    fun shiftDown(
        rangeSelectorStateMs:Ms<RangeSelectorState>,
        wsStateMs: Ms<WorksheetState>,
        colRulerStateMs: Ms<RulerState>,
        rowRulerStateMs: Ms<RulerState>
    )

    fun shiftRight(
        rangeSelectorStateMs:Ms<RangeSelectorState>,
        wsStateMs: Ms<WorksheetState>,
        colRulerStateMs: Ms<RulerState>,
        rowRulerStateMs: Ms<RulerState>
    )

    fun shiftLeft(
        rangeSelectorStateMs:Ms<RangeSelectorState>,
        wsStateMs: Ms<WorksheetState>,
        colRulerStateMs: Ms<RulerState>,
        rowRulerStateMs: Ms<RulerState>
    )

    fun ctrlUp(
        rangeSelectorStateMs:Ms<RangeSelectorState>,
        wsStateMs: Ms<WorksheetState>,
        colRulerStateMs: Ms<RulerState>,
        rowRulerStateMs: Ms<RulerState>
    )

    fun home(
        rangeSelectorStateMs:Ms<RangeSelectorState>,
        wsStateMs: Ms<WorksheetState>,
        colRulerStateMs: Ms<RulerState>,
        rowRulerStateMs: Ms<RulerState>
    )

    fun ctrlShiftRight(wsState: WorksheetState)
    fun ctrlShiftUp(wsState: WorksheetState)
    fun ctrlShiftDown(wsState: WorksheetState)

    fun ctrlShiftLeft(wsState: WorksheetState)

}
