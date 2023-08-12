package com.qxdzbc.p6.ui.window

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.singleWindowApplication
import com.qxdzbc.p6.common.utils.Loggers
import com.qxdzbc.p6.ui.common.view.BorderBox
import com.qxdzbc.p6.ui.common.view.BorderStyleValue
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.p6.ui.common.view.BorderStyle
import com.qxdzbc.p6.ui.common.view.PlaceHolder


/**
 * A window frame is a layout for a window
 */

@Composable
fun WindowFrame(
    menu: @Composable () -> Unit = { PlaceHolder("Menu") },
    formulaBar: @Composable () -> Unit = { PlaceHolder("Formula bar") },
    workbookTab: @Composable () -> Unit = {PlaceHolder("Workbook tab")},
    workbookView: @Composable () -> Unit = { PlaceHolder("SheetView") },
    toolTab: @Composable () -> Unit = { PlaceHolder("ToolTab") },
    statusBar: @Composable () -> Unit = { PlaceHolder("StatusBar") },
    modifier: Modifier = Modifier,
) {
    Loggers.renderLogger.debug("render window frame")
    MBox(modifier = modifier
        .fillMaxSize()
    ) {
        Column {
            val stdBarMod = Modifier.height(30.dp).fillMaxWidth()
            BorderBox(borderStyle = BorderStyle.BOT, modifier = Modifier.fillMaxWidth()) {
                menu()
            }
            BorderBox(borderStyle = BorderStyle.BOT, modifier = stdBarMod){
                workbookTab()
            }
            BorderBox(borderStyle = BorderStyle.BOT, modifier = stdBarMod) {
                formulaBar()
            }
            BorderBox(borderStyle = BorderStyle.BOT, modifier = Modifier.weight(1.0F)) {
                workbookView()
            }
            BorderBox(borderStyle = BorderStyle.BOT, modifier = stdBarMod) {
                toolTab()
            }
            BorderBox(borderStyle = BorderStyle.NONE, modifier = stdBarMod) {
                statusBar()
            }
        }
    }
}


fun main() {
    singleWindowApplication {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            WindowFrame(
                {
                    Text("Menu", modifier = Modifier.align(Alignment.Center))
                },
                {
                    Text("sheet view", modifier = Modifier.align(Alignment.Center))
                },
                {
                    Text("sheet tab", modifier = Modifier.align(Alignment.Center))
                },
                modifier = Modifier.background(Color.Blue)
            )
        }
    }
}
