package com.qxdzbc.p6.app.action.cursor.handle_cursor_keyboard_event

import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.common.key_event.P6KeyEvent

interface HandleCursorKeyboardEventAction {
    fun handleKeyboardEvent(keyEvent: P6KeyEvent, wbws: WbWs): Boolean

    /**
     * f2 key
     */
    fun f2(wbws: WbWs)
    fun f2(wbwsSt: WbWsSt)

    fun home(wbws: WbWs)
    fun home(wbwsSt: WbWsSt)

    fun end(wbws: WbWs)
    fun end(wbwsSt: WbWsSt)

    fun ctrlUp(wbws: WbWs)
    fun ctrlUp(wbwsSt: WbWsSt)

    fun ctrlDown(wbws: WbWs)
    fun ctrlDown(wbwsSt: WbWsSt)

    fun ctrlRight(wbws: WbWs)
    fun ctrlRight(wbwsSt: WbWsSt)

    fun ctrlShiftLeft(wbws: WbWs)
    fun ctrlShiftLeft(wbwsSt: WbWsSt)

    fun ctrlShiftRight(wbws: WbWs)
    fun ctrlShiftRight(wbwsSt: WbWsSt)

    fun ctrlShiftUp(wbws: WbWs)
    fun ctrlShiftUp(wbwsSt: WbWsSt)

    fun ctrlShiftDown(wbws: WbWs)
    fun ctrlShiftDown(wbwsSt: WbWsSt)

    fun ctrlLeft(wbws: WbWs)
    fun ctrlLeft(wbwsSt: WbWsSt)

    fun up(wbws: WbWs)
    fun up(wbwsSt: WbWsSt)

    fun down(wbws: WbWs)
    fun down(wbwsSt: WbWsSt)

    fun left(wbws: WbWs)
    fun left(wbwsSt: WbWsSt)

    fun right(wbws: WbWs)
    fun right(wbwsSt: WbWsSt)

    fun shiftUp(wbws: WbWs)
    fun shiftUp(wbwsSt: WbWsSt)

    fun shiftDown(wbws: WbWs)
    fun shiftDown(wbwsSt: WbWsSt)

    fun shiftLeft(wbws: WbWs)
    fun shiftLeft(wbwsSt: WbWsSt)

    fun shiftRight(wbws: WbWs)
    fun shiftRight(wbwsSt: WbWsSt)

    fun onDeleteKey(wbws: WbWs)
    fun onDeleteKey(wbwsSt: WbWsSt)

    /**
     * Move the cursor 1 page down
     */
    fun onPageDown(wbws: WbWs)
    /**
     * Move the cursor 1 page down
     */
    fun onPageDown(wbwsSt: WbWsSt)
}
