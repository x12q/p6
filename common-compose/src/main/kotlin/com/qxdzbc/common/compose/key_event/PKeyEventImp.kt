package com.qxdzbc.common.compose.key_event

import androidx.compose.ui.input.key.*
import com.qxdzbc.common.compose.KeyEventUtils.isCtrlPressedAlone
import com.qxdzbc.common.compose.KeyEventUtils.isCtrlShiftPressed
import com.qxdzbc.common.compose.KeyEventUtils.isShiftPressedAlone
import com.qxdzbc.common.compose.key_event.AbsPKeyEvent

data class PKeyEventImp(override val keyEvent: KeyEvent) : AbsPKeyEvent() {
    override val key: Key
        get() = keyEvent.key
    override val isCtrlShiftPressed: Boolean
        get() = keyEvent.isCtrlShiftPressed
    override val isShiftPressedAlone: Boolean
        get() = keyEvent.isShiftPressedAlone
    override val isCtrlPressedAlone: Boolean
        get() = keyEvent.isCtrlPressedAlone
    override val type: KeyEventType
        get() = keyEvent.type
}
