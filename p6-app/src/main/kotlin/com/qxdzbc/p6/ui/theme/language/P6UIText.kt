package com.qxdzbc.p6.ui.theme.language

import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Contains texts such as dialog title, button texts
 */

class P6UIText(
    val renameSheetDialogTitle:String = "Rename sheet"
){
    companion object{
        fun english(): P6UIText {
            return P6UIText()
        }

        val local = staticCompositionLocalOf(
            defaultFactory = {
                english()
            }
        )
    }
}
