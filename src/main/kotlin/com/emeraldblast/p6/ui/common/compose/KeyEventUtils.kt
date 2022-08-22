package com.emeraldblast.p6.ui.common.compose

import androidx.compose.ui.input.key.*

object KeyEventUtils{
    val KeyEvent.isCtrlShiftPressed:Boolean get(){
        return isCtrlPressed && isShiftPressed && !isAltPressed && !isMetaPressed
    }

    val KeyEvent.isCtrlPressedAlone:Boolean get(){
        return isCtrlPressed && !isShiftPressed && !isAltPressed && !isMetaPressed
    }

    val KeyEvent.isShiftPressedAlone:Boolean get(){
        return !isCtrlPressed && isShiftPressed && !isAltPressed && !isMetaPressed
    }

}
