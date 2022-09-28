package com.qxdzbc.common.compose.key_event
import androidx.compose.ui.input.key.*

/**
 * A wrapper class for easier mocking
 */
interface PKeyEvent {
    val keyEvent:KeyEvent
    val key:Key
    val isCtrlShiftPressed:Boolean
    val isShiftPressedAlone:Boolean
    val isCtrlPressedAlone:Boolean
    val type:KeyEventType
    companion object {
        fun KeyEvent.toPKeyEvent(): PKeyEvent {
            return PKeyEventImp(this)
        }
    }

    /**
     * is a key accepted by a range selector
     */
    fun isRangeSelectorAcceptedKey():Boolean

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
}

