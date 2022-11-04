package com.qxdzbc.common.compose.key_event

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.Key
import com.qxdzbc.common.compose.KeyEventUtils.isCtrlPressedAlone
import com.qxdzbc.common.compose.KeyEventUtils.isCtrlShiftPressed
import com.qxdzbc.common.compose.KeyEventUtils.isShiftPressedAlone
import com.qxdzbc.common.compose.KeyUtils.isArrowKey
import com.qxdzbc.common.compose.KeyUtils.isSingleModifier

abstract class AbsMKeyEvent: MKeyEvent {

    @OptIn(ExperimentalComposeUiApi::class)
    override val isLeftBracket: Boolean
        get() {
            return this.isFreeOfModificationKey && this.key == Key.LeftBracket
        }

    @OptIn(ExperimentalComposeUiApi::class)
    override val isLeftCurlyBracket: Boolean
        get() {
            return this.isShiftPressedInCombination && this.key == Key.LeftBracket
        }

    @OptIn(ExperimentalComposeUiApi::class)
    override val isLeftParentheses:Boolean get(){
        when(this.key){
            Key.Nine->{
                return this.isShiftPressedInCombination
            }
            Key.NumPadLeftParenthesis -> return true
            else -> return false
        }
    }
}
