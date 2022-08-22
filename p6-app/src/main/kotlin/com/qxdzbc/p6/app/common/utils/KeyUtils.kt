package com.qxdzbc.p6.app.common.utils

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent

object KeyUtils {
    @OptIn(ExperimentalComposeUiApi::class)
    fun Key.isArrowKey(): Boolean {
        return when (this) {
            Key.DirectionUp, Key.DirectionDown, Key.DirectionLeft, Key.DirectionRight, Key.DirectionUpRight, Key.DirectionUpLeft, Key.DirectionDownRight, Key.DirectionDownLeft -> true
            else -> false
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    fun Key.isSingleModifier(): Boolean {
        when (this) {
            Key.CtrlLeft, Key.CtrlRight, Key.ShiftLeft, Key.ShiftRight, Key.AltLeft, Key.AltRight, Key.MetaLeft, Key.MetaRight -> return true
            else -> return false
        }
    }
}
