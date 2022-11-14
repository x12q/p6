package com.qxdzbc.p6.app.action.cursor.handle_cursor_keyboard_event

import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.common.key_event.P6KeyEvent

interface HandleCursorKeyboardEventAction {
    fun handleKeyboardEvent(keyEvent: P6KeyEvent, wbws: WbWs): Boolean
    /**
     * f2 key
     */
    fun f2(wbws: WbWs)
    fun home(wbws: WbWs)
    fun end(wbws: WbWs)
    fun ctrlUp(wbws: WbWs)
    fun ctrlDown(wbws: WbWs)
    fun ctrlRight(wbws: WbWs)
    fun ctrlShiftLeft(wbws: WbWs)
    fun ctrlShiftRight(wbws: WbWs)
    fun ctrlShiftUp(wbws: WbWs)
    fun ctrlShiftDown(wbws: WbWs)
    fun ctrlLeft(wbws: WbWs)
    fun up(wbws: WbWs)
    fun down(wbws: WbWs)
    fun left(wbws: WbWs)
    fun right(wbws: WbWs)

    fun shiftUp(wbws: WbWs)
    fun shiftDown(wbws: WbWs)
    fun shiftLeft(wbws: WbWs)
    fun shiftRight(wbws: WbWs)

    fun onDeleteKey(wbws: WbWs)
}
