package com.emeraldblast.p6.ui.common.view.dialog

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import com.emeraldblast.p6.ui.common.R
import com.emeraldblast.p6.ui.common.compose.testApp
import com.emeraldblast.p6.ui.common.view.MBox
import com.emeraldblast.p6.ui.theme.P6DefaultTypoGraphy
import com.emeraldblast.p6.ui.theme.P6GrayColors

fun main() {
    testApp() {
        MaterialTheme(colors = P6GrayColors, typography = P6DefaultTypoGraphy) {
            MBox(modifier = Modifier.then(R.border.mod.black)) {
                Dialogs.DirectoryDialog(
                    title="Dir",
                    initText="123",
                    onOk = {
                           println(it)
                    },
                    openBrowserAndUpdateText = {
                        println("click dir")
                        null
                    }
                )
            }
        }
    }
}
