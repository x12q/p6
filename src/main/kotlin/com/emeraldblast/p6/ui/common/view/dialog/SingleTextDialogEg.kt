package com.emeraldblast.p6.ui.common.view.dialog

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import com.emeraldblast.p6.ui.common.R
import com.emeraldblast.p6.ui.common.compose.TestApp
import com.emeraldblast.p6.ui.common.view.MBox
import com.emeraldblast.p6.ui.theme.P6AllWhiteColors
import com.emeraldblast.p6.ui.theme.P6DefaultTypoGraphy


fun main() {
    TestApp() {
        MaterialTheme(colors = P6AllWhiteColors, typography = P6DefaultTypoGraphy) {
            MBox(modifier = Modifier.then(R.border.mod.black)) {
                Dialogs.SingleTextDialog ()
            }
        }
    }
}
