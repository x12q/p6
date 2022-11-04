package com.qxdzbc.common.compose.key_event
import androidx.compose.ui.input.key.*

/**
 * A wrapper class for easier mocking of [KeyEvent]
 */
interface MKeyEvent {
    companion object {
        fun KeyEvent.toMKeyEvent(): MKeyEvent {
            return MKeyEventWrapper(this)
        }
    }

    val keyEvent:KeyEvent
    val key:Key
    val isCtrlShiftPressed:Boolean
    val isCtrlPressedAlone:Boolean
    val isAltPressedAlone:Boolean
    val type:KeyEventType

    val isShiftPressedAlone:Boolean
    val isShiftPressedInCombination:Boolean

    val isFreeOfModificationKey:Boolean

    /**
     * @return true on any key or key combination that can be interpreted as '('
     */
    val isLeftParentheses: Boolean

    /**
     * @return true on any key or key combination that can be interpreted as '['
     */
    val isLeftBracket:Boolean

    /**
     * @return true on any key or key combination that can be interpreted as '{'
     */
    val isLeftCurlyBracket:Boolean
}

