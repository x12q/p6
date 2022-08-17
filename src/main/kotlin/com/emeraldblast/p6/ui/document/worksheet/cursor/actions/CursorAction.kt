package com.emeraldblast.p6.ui.document.worksheet.cursor.actions

import androidx.compose.ui.input.key.KeyEvent
import com.emeraldblast.p6.app.action.common_data_structure.WithWbWs
import com.emeraldblast.p6.app.common.utils.PKeyEvent
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.document.worksheet.cursor.state.CursorState
import com.emeraldblast.p6.ui.document.worksheet.ruler.RulerState
import com.emeraldblast.p6.ui.document.worksheet.state.WorksheetState
import com.emeraldblast.p6.ui.document.worksheet.state.WorksheetStateId


interface CursorAction {

    /**
     * f2 key
     */
    fun f2(wsState: WithWbWs)


    /**
     * select whole col
     */
    fun ctrlSpace(wsState: WorksheetState)

    /**
     * select whole row
     */


    /**
     * undo action
     */
    fun undo(cursorState: CursorState)

    /**
     * on pressing delete key
     */
    fun delete(cursorState: CursorState)
    fun rangeToClipboard2(cursorState: CursorState)

    fun up(
        cursorStateMs: Ms<CursorState>,
        wsStateMs: Ms<WorksheetState>,
        colRulerStateMs: Ms<RulerState>,
        rowRulerStateMs: Ms<RulerState>
    )

    fun down(
        cursorStateMs: Ms<CursorState>,
        wsStateMs: Ms<WorksheetState>,
        colRulerStateMs: Ms<RulerState>,
        rowRulerStateMs: Ms<RulerState>
    )

    fun left(
        cursorStateMs: Ms<CursorState>,
        wsStateMs: Ms<WorksheetState>,
        colRulerStateMs: Ms<RulerState>,
        rowRulerStateMs: Ms<RulerState>
    )

    fun right(
        cursorStateMs: Ms<CursorState>,
        wsStateMs: Ms<WorksheetState>,
        colRulerStateMs: Ms<RulerState>,
        rowRulerStateMs: Ms<RulerState>
    )

    fun ctrlLeft(
        cursorStateMs: Ms<CursorState>,
        wsStateMs: Ms<WorksheetState>,
        colRulerStateMs: Ms<RulerState>,
        rowRulerStateMs: Ms<RulerState>
    )

    fun ctrlRight(
        cursorStateMs: Ms<CursorState>,
        wsStateMs: Ms<WorksheetState>,
        colRulerStateMs: Ms<RulerState>,
        rowRulerStateMs: Ms<RulerState>
    )

    fun ctrlDown(
        cursorStateMs: Ms<CursorState>,
        wsStateMs: Ms<WorksheetState>,
        colRulerStateMs: Ms<RulerState>,
        rowRulerStateMs: Ms<RulerState>
    )

    fun end(
        cursorStateMs: Ms<CursorState>,
        wsStateMs: Ms<WorksheetState>,
        colRulerStateMs: Ms<RulerState>,
        rowRulerStateMs: Ms<RulerState>
    )

    fun shiftUp(
        cursorStateMs: Ms<CursorState>,
        wsStateMs: Ms<WorksheetState>,
        colRulerStateMs: Ms<RulerState>,
        rowRulerStateMs: Ms<RulerState>
    )

    fun shiftDown(
        cursorStateMs: Ms<CursorState>,
        wsStateMs: Ms<WorksheetState>,
        colRulerStateMs: Ms<RulerState>,
        rowRulerStateMs: Ms<RulerState>
    )

    fun shiftRight(
        cursorStateMs: Ms<CursorState>,
        wsStateMs: Ms<WorksheetState>,
        colRulerStateMs: Ms<RulerState>,
        rowRulerStateMs: Ms<RulerState>
    )

    fun shiftLeft(
        cursorStateMs: Ms<CursorState>,
        wsStateMs: Ms<WorksheetState>,
        colRulerStateMs: Ms<RulerState>,
        rowRulerStateMs: Ms<RulerState>
    )

    fun ctrlUp(
        cursorStateMs: Ms<CursorState>,
        wsStateMs: Ms<WorksheetState>,
        colRulerStateMs: Ms<RulerState>,
        rowRulerStateMs: Ms<RulerState>
    )

    fun home(
        cursorStateMs: Ms<CursorState>,
        wsStateMs: Ms<WorksheetState>,
        colRulerStateMs: Ms<RulerState>,
        rowRulerStateMs: Ms<RulerState>
    )

    fun pasteRange(cursorState: CursorState)
    fun ctrlShiftRight(wsState: WorksheetState)
    fun ctrlShiftUp(wsState: WorksheetState)
    fun ctrlShiftDown(wsState: WorksheetState)

    fun ctrlShiftLeft(wsState: WorksheetState)


    fun shiftSpace(cursorStateMs: Ms<CursorState>, wsState: WorksheetState)
//    fun handleKeyboardEvent(keyEvent: KeyEvent, cursorState: CursorState): Boolean
    fun handleKeyboardEvent(keyEvent: PKeyEvent, cursorState: CursorState): Boolean
}

