package com.qxdzbc.p6.common.key_event

import androidx.compose.ui.input.key.*
import com.qxdzbc.common.compose.KeyEventUtils.isAltPressedAlone
import com.qxdzbc.common.compose.KeyEventUtils.isCtrlPressedAlone
import com.qxdzbc.common.compose.KeyEventUtils.isCtrlShiftPressed
import com.qxdzbc.common.compose.KeyEventUtils.isFreeOfModificationKey
import com.qxdzbc.common.compose.KeyEventUtils.isShiftPressedAlone

data class P6KeyEventWrapper(override val keyEvent: KeyEvent): AbsP6KeyEvent(){
    override val key: Key
        get() = keyEvent.key
    override val isCtrlShiftPressed: Boolean
        get() = keyEvent.isCtrlShiftPressed
    override val isShiftPressedAlone: Boolean
        get() = keyEvent.isShiftPressedAlone
    override val isShiftPressedInCombination: Boolean
        get() = keyEvent.isShiftPressed
    override val isFreeOfModificationKey: Boolean
        get() = keyEvent.isFreeOfModificationKey
    override val isCtrlPressedAlone: Boolean
        get() = keyEvent.isCtrlPressedAlone
    override val isAltPressedAlone: Boolean
        get() = keyEvent.isAltPressedAlone
    override val type: KeyEventType
        get() = keyEvent.type
}
