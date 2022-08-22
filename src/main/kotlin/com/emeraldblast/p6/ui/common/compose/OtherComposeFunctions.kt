package com.emeraldblast.p6.ui.common.compose

import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.platform.testTag

object OtherComposeFunctions{
    val PointerKeyboardModifiers.areAnyPressed: Boolean
        get() = isCtrlPressed || isAltPressed || isFunctionPressed || isAltGraphPressed || isShiftPressed || isMetaPressed || isFunctionPressed || isSymPressed
    val PointerKeyboardModifiers.isNonePressed: Boolean
        get() = !areAnyPressed

    fun addTestTag(enableTestTag: Boolean, tag: String): Modifier {
        return if (enableTestTag) Modifier.testTag(tag) else Modifier
    }
}



