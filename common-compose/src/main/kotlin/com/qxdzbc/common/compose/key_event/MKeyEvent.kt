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

    /**
     * if Ctrl + Shift are pressed in this event
     */
    val isCtrlShiftPressed:Boolean

    /**
     * if Ctrl is the only modification key in this event
     */
    val isCtrlPressedAlone:Boolean
    /**
     * if Alt is the only modification key in this event
     */
    val isAltPressedAlone:Boolean
    val type:KeyEventType

    /**
     * if Shift is the only modification key in this event
     */
    val isShiftPressedAlone:Boolean
    /**
     * if Shift is one of the modification keys pressed in this event
     */
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

