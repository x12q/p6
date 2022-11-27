package com.qxdzbc.p6.app.common.key_event

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.Key
import com.qxdzbc.common.compose.KeyEventUtils.isCtrlPressedAlone
import com.qxdzbc.common.compose.KeyEventUtils.isCtrlShiftPressed
import com.qxdzbc.common.compose.KeyEventUtils.isShiftPressedAlone
import com.qxdzbc.common.compose.KeyUtils.isArrowKey
import com.qxdzbc.common.compose.KeyUtils.isSingleModifier
import com.qxdzbc.common.compose.key_event.AbsMKeyEvent

abstract class AbsP6KeyEvent : AbsMKeyEvent(), P6KeyEvent {
    override fun isAcceptedByRangeSelector(): Boolean {
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

    @OptIn(ExperimentalComposeUiApi::class)
    override fun isTextual(): Boolean {
        if (this.isShiftPressedAlone || this.isFreeOfModificationKey) {
            if (this.key.keyCode in (Key.F1.keyCode..Key.F12.keyCode)) {
                return false
            } else {
                when (this.key) {
                    Key.Tab, Key.CapsLock, Key.ShiftLeft, Key.ShiftRight, Key.CtrlLeft, Key.CtrlRight,
                    Key.AltLeft, Key.AltRight, Key.MetaLeft, Key.MetaRight, Key.PrintScreen, Key.ScrollLock,
                    Key.Break, Key.Insert, Key.Home, Key.MoveHome, Key.PageUp, Key.Delete, Key.MoveEnd,
                    Key.PageDown, Key.NumLock, Key.DirectionUp, Key.DirectionLeft,Key.DirectionDown,
                    Key.DirectionCenter,Key.DirectionDownLeft, Key.DirectionDownRight,
                    Key.DirectionUpLeft,Key.DirectionUpRight,
                    -> {
                        return false
                    }

                    else -> {
                        return true
                    }
                }
            }
        } else {
            return false
        }

    }
}
