package com.emeraldblast.p6.ui.document.worksheet.cursor.actions

import com.emeraldblast.p6.app.action.common_data_structure.WithWbWs
import com.emeraldblast.p6.app.common.utils.PKeyEvent


interface CursorAction {

    /**
     * f2 key
     */
    fun f2(wbws: WithWbWs)
    fun home(wbws: WithWbWs)
    fun end(wbws: WithWbWs)
    fun ctrlUp(wbws: WithWbWs)
    fun ctrlDown(wbws: WithWbWs)
    fun ctrlRight(wbws: WithWbWs)
    fun ctrlShiftLeft(wbws: WithWbWs)
    fun ctrlShiftRight(wbws: WithWbWs)
    fun ctrlShiftUp(wbws: WithWbWs)
    fun ctrlShiftDown(wbws: WithWbWs)
    fun ctrlLeft(wbws: WithWbWs)
    fun selectWholeCol(wbws: WithWbWs)
    fun selectWholeRow(wbws: WithWbWs)
    fun up(wbws: WithWbWs)
    fun down(wbws: WithWbWs)
    fun left(wbws: WithWbWs)
    fun right(wbws: WithWbWs)
    fun shiftUp(wbws: WithWbWs)
    fun shiftDown(wbws: WithWbWs)
    fun shiftLeft(wbws: WithWbWs)
    fun shiftRight(wbws: WithWbWs)
    /**
     * on pressing delete key
     */
    fun delete(wbws: WithWbWs)
    fun undo(wbws: WithWbWs)
    fun handleKeyboardEvent(keyEvent: PKeyEvent, wbws: WithWbWs): Boolean
    fun pasteRange(wbws: WithWbWs)
    fun rangeToClipboard2(wbws: WithWbWs)
}

