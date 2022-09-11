package com.qxdzbc.common.compose.key_event

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.Key
import com.qxdzbc.common.compose.KeyEventUtils.isCtrlPressedAlone
import com.qxdzbc.common.compose.KeyEventUtils.isCtrlShiftPressed
import com.qxdzbc.common.compose.KeyEventUtils.isShiftPressedAlone
import com.qxdzbc.common.compose.KeyUtils.isArrowKey
import com.qxdzbc.common.compose.KeyUtils.isSingleModifier

abstract class AbsPKeyEvent: PKeyEvent {
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