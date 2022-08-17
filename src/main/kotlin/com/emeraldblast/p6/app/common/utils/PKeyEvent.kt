package com.emeraldblast.p6.app.common.utils
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.*
import com.emeraldblast.p6.app.common.utils.KeyUtils.isArrowKey
import com.emeraldblast.p6.app.common.utils.KeyUtils.isSingleModifier
import com.emeraldblast.p6.ui.common.compose.isCtrlPressedAlone
import com.emeraldblast.p6.ui.common.compose.isCtrlShiftPressed
import com.emeraldblast.p6.ui.common.compose.isShiftPressedAlone

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
        fun KeyEvent.toPKeyEvent():PKeyEvent{
            return PKeyEventImp(this)
        }
    }
    fun isRangeSelectorToleratedKey():Boolean
    fun isRangeSelectorNavKey():Boolean
    fun isRangeSelectorNonNavKey():Boolean
}

abstract class AbstractPKeyEvent:PKeyEvent{
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
