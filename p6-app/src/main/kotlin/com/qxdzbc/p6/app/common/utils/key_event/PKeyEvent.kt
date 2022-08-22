package com.qxdzbc.p6.app.common.utils.key_event
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
    fun isRangeSelectorToleratedKey():Boolean
    fun isRangeSelectorNavKey():Boolean
    fun isRangeSelectorNonNavKey():Boolean
}

