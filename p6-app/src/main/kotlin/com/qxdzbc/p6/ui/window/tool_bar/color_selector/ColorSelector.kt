package com.qxdzbc.p6.ui.window.tool_bar.color_selector

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.rms
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.p6.ui.common.common_objects.P6Icons
import com.qxdzbc.p6.ui.common.view.MRow
import com.qxdzbc.p6.ui.common.view.buttons.IconBox
import com.qxdzbc.p6.ui.window.tool_bar.ToolBarDropDownMenu
import com.qxdzbc.p6.ui.window.tool_bar.color_selector.action.ColorSelectorAction
import com.qxdzbc.p6.ui.window.tool_bar.color_selector.action.ColorSelectorActionDoNothing
import com.qxdzbc.p6.ui.window.tool_bar.color_selector.color_dialog.ColorDialog
import com.qxdzbc.p6.ui.window.tool_bar.color_selector.state.ColorSelectorInternalState
import com.qxdzbc.p6.ui.window.tool_bar.color_selector.state.ColorSelectorInternalStateImp
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
    colorList: List<Color> = defaultColorList,
    internalStateMs:Ms<ColorSelectorInternalState> = rms(ColorSelectorInternalStateImp())
) {
    val currentColor = state.currentColor
    val openColorDialog = internalStateMs.value.openColorDialog
    val expanded = internalStateMs.value.expanded
    ToolBarDropDownMenu(
        expanded = expanded,
        header = {
            Box(modifier = Modifier.clickable {
                internalStateMs.value = internalStateMs.value.setExpanded(true)
            }) {
                IconBox(
                    icon = icon,
                    tint = MaterialTheme.colors.onPrimary,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
                Box(
                    modifier = Modifier.size(width = 15.dp, height = 4.dp).let {
                        if (currentColor != null) {
                            it.background(currentColor)
                        } else {
                            it.border(1.dp, MaterialTheme.colors.onPrimary).background(ColorSelectorState.defaultColor)
                        }
                    }
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
                    internalStateMs.value = internalStateMs.value.setExpanded(false)
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
                        MBox(modifier = Modifier
                            .clickable {
                                internalStateMs.value = internalStateMs.value.setExpanded(false)
                                action.pickColor(windowId, color)
                            }) {
                            ColorSquare(color)
                        }
                    }
                }
            }
            Box(modifier = Modifier.padding(top = 3.dp).height(20.dp).fillMaxWidth().clickable {
                internalStateMs.value = internalStateMs.value.setExpanded(false).setOpenColorDialog(true)
            }) {
                Text("custom color", modifier = Modifier.align(Alignment.Center))
            }
        },
        onDismiss = {
            internalStateMs.value = internalStateMs.value.setExpanded(false)
        }
    )
    if (openColorDialog) {
        ColorDialog(
            onPick = {
                action.pickColor(windowId, it)
                internalStateMs.value = internalStateMs.value.setOpenColorDialog(false)
            },
            onCancel = {
                internalStateMs.value = internalStateMs.value.setOpenColorDialog(false)
            }
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
                icon = P6Icons.FormatColorText,
                state = ColorSelectorStateImp(Color.Red),
                action = ColorSelectorActionDoNothing()
            )
        }
    }
}
