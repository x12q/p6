package com.emeraldblast.p6.app.common.utils

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.*
import com.emeraldblast.p6.app.common.utils.KeyUtils.isArrowKey
import com.emeraldblast.p6.app.common.utils.KeyUtils.isSingleModifier
import com.emeraldblast.p6.ui.common.compose.isCtrlPressedAlone
import com.emeraldblast.p6.ui.common.compose.isCtrlShiftPressed
import com.emeraldblast.p6.ui.common.compose.isShiftPressedAlone

data class PKeyEventImp(override val keyEvent: KeyEvent) : PKeyEvent {
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

    override fun isRangeSelectorToleratedKey(): Boolean {
        return this.isRangeSelectorNonNavKey() || this.isRangeSelectorNavKey()
    }

    @OptIn(ExperimentalComposeUiApi::class)
    override fun isRangeSelectorNavKey(): Boolean {
        if (this.key.isArrowKey()) {
            return true
        }
        if (this.keyEvent.isCtrlPressedAlone || this.keyEvent.isCtrlShiftPressed || this.keyEvent.isShiftPressedAlone) {
            return this.key.isArrowKey()
        }
        when (this.key) {
            Key.Home, Key.MoveEnd -> return true
        }
        return false
    }

    override fun isRangeSelectorNonNavKey(): Boolean {
        if (this.key.isSingleModifier()) {
            return true
        }
        return false
    }
}
