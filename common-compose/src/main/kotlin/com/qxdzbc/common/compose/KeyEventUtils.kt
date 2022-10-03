package com.qxdzbc.common.compose

import androidx.compose.ui.input.key.*

object KeyEventUtils{
    val KeyEvent.isCtrlShiftPressed:Boolean get(){
        return isCtrlPressed && isShiftPressed && !isAltPressed && !isMetaPressed
    }

    val KeyEvent.isCtrlPressedAlone:Boolean get(){
        return isCtrlPressed && !isShiftPressed && !isAltPressed && !isMetaPressed
    }

    val KeyEvent.isAltPressedAlone:Boolean get(){
        return !isCtrlPressed && !isShiftPressed && isAltPressed && !isMetaPressed
    }

    val KeyEvent.isShiftPressedAlone:Boolean get(){
        return !isCtrlPressed && isShiftPressed && !isAltPressed && !isMetaPressed
    }

    val KeyEvent.isFreeOfModificationKey:Boolean get(){
        return !isCtrlPressed && !isShiftPressed && !isAltPressed && !isMetaPressed
    }

}
