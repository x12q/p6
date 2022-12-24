package com.qxdzbc.p6.ui.window.tool_bar.color_selector

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.qxdzbc.common.compose.StateUtils.rms
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.p6.ui.common.P6R
import com.qxdzbc.p6.ui.common.view.MRow
import com.qxdzbc.p6.ui.common.view.buttons.IconBox
import com.qxdzbc.p6.ui.window.tool_bar.ToolBarDropDownMenu
import com.qxdzbc.p6.ui.window.tool_bar.color_selector.action.ColorSelectorAction
import com.qxdzbc.p6.ui.window.tool_bar.color_selector.action.ColorSelectorActionDoNothing
import com.qxdzbc.p6.ui.window.tool_bar.color_selector.color_dialog.ColorDialog
import com.qxdzbc.p6.ui.window.tool_bar.color_selector.state.ColorSelectorState
import com.qxdzbc.p6.ui.window.tool_bar.color_selector.state.ColorSelectorStateImp

internal val defaultColorList = listOf(
    Color.Black,
    Color(67, 67, 67),
    Color(102, 102, 102),
    Color(153, 153, 153),
    Color(183, 183, 183),
    Color(204, 204, 204),
    Color(217, 217, 217),
    Color(239, 239, 239),
    Color(243, 243, 243),
    Color.White,

    Color(153, 40, 27),
    Color(239, 68, 48),
    Color(245, 153, 48),
    Color(252, 239, 45),
    Color(117, 233, 21),
    Color(104, 248, 252),
    Color(74, 134, 232),
    Color(50, 103, 252),
    Color(161, 111, 253),
    Color(234, 120, 254)
)

@Composable
fun ColorSelector(
    windowId: String,
    state: ColorSelectorState,
    action: ColorSelectorAction,
    icon: ImageVector,
    colorList: List<Color> = defaultColorList
) {
    val currentColor = state.currentColor
    var openColorDialog by rms(false)
    var expanded by rms(false)
    ToolBarDropDownMenu(
        expanded = expanded,
        header = {
            Box(modifier = Modifier.clickable {
                expanded = true
            }) {
                IconBox(
                    icon = icon,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
                Box(
                    modifier = Modifier.size(width = 15.dp, height = 4.dp).background(currentColor)
                        .align(Alignment.BottomCenter)
                )
            }
        },
        items = {
            Box(modifier = Modifier
                .padding(bottom = 3.dp)
                .height(20.dp)
                .fillMaxWidth()
                .clickable {
                    action.clearColor(windowId)
                    expanded = false
                }) {
                Text("clear", modifier = Modifier.align(Alignment.Center))
            }
            val chunks = colorList.chunked(10)
            for (chunk in chunks) {
                MRow(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 5.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    for (color in chunk) {
                        MBox(modifier = Modifier.clickable {
                            action.pickColor(windowId, color)
                            expanded = false
                        }) {
                            ColorSquare(color)
                        }
                    }
                }
            }
            Box(modifier = Modifier.padding(top = 3.dp).height(20.dp).fillMaxWidth().clickable {
                openColorDialog = true
                expanded = false
            }) {
                Text("custom color", modifier = Modifier.align(Alignment.Center))
            }
        },
        onDismiss = {
            expanded = false
        }
    )
    if (openColorDialog) {
        ColorDialog(
            onPick = {
                expanded = false
            },
            onCancel = { openColorDialog = false }
        )
    }
}

fun main() = application {
    Window(
        state = WindowState(width = 350.dp, height = 450.dp),
        onCloseRequest = ::exitApplication
    ) {
        Column {
            ColorSelector(
                windowId = "",
                icon = P6R.icons.FormatColorText,
                state = ColorSelectorStateImp(Color.Red),
                action = ColorSelectorActionDoNothing()
            )
        }
    }
}
