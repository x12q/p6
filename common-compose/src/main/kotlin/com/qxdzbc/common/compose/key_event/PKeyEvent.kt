package com.qxdzbc.common.compose.key_event
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.*

/**
 * A wrapper class for easier mocking
 */
interface PKeyEvent {
    companion object {
        fun KeyEvent.toPKeyEvent(): PKeyEvent {
            return PKeyEventWrapper(this)
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
     * is a key accepted by a range selector
     */
    fun isAcceptedByRangeSelector():Boolean

    /**
     * is a key a navigation key accepted by a range selector. Including:
     *  - arrow keys
     *  - ctrl + shift + arrow keys
     *  - ctrl + arrow keys
     *  - shift + arrow keys
     *  - home key
     *  - end key
     */
    fun isRangeSelectorNavKey():Boolean

    /**
     * is a key a non-navigation key accepted by a range selector. Including:
     *  - ctrl
     *  - shift
     *  - alt
     */
    fun isRangeSelectorNonNavKey():Boolean

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

