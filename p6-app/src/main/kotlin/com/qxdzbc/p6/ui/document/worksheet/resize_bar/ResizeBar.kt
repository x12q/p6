package com.qxdzbc.p6.ui.document.worksheet.resize_bar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.common.compose.view.testApp
import com.qxdzbc.p6.ui.document.worksheet.WorksheetConstants
import com.qxdzbc.p6.ui.document.worksheet.resize_bar.components.ResizeBarBar
import com.qxdzbc.p6.ui.document.worksheet.resize_bar.components.ResizeBarThumb
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerType
import com.qxdzbc.p6.ui.theme.P6Theme

/**
 * A resize bar is a hair-line bar that shows up when users drag-to-resize a bar or a column. A resize bar always stretch out to the length or width of the current worksheet.
 */
@Composable
fun ResizeBar(
    state: ResizeBarState
) {
    val density = LocalDensity.current

    val barMod = when (state.rulerType) {
        RulerType.Col -> Modifier.width(state.thickness).fillMaxHeight()
        RulerType.Row -> Modifier.height(state.thickness).fillMaxWidth()
    }.background(P6Theme.color.uiColor.resizeBarColor)

    val thumbThickness = WorksheetConstants.defaultResizeCursorThumbThickness

    val thumbModifier = when (state.rulerType) {
        RulerType.Col -> {
            Modifier
                .width(thumbThickness)
                .height(
                    state.thumbSize
                )
        }

        RulerType.Row -> {
            Modifier
                .height(thumbThickness)
                .width(
                    state.thumbSize
                )

        }
    }
        .background(Color.Black)

    val barAlignmentValue = when (state.rulerType) {
        RulerType.Col -> Alignment.TopCenter
        RulerType.Row -> Alignment.CenterStart
    }

    val thumbAlignmentValue = when (state.rulerType) {
        RulerType.Col -> Alignment.CenterEnd
        RulerType.Row -> Alignment.BottomCenter
    }

    val boxModifier = when (state.rulerType) {
        RulerType.Col -> Modifier.width(state.selectableAreaWidth)
        RulerType.Row -> Modifier.height(state.selectableAreaWidth)
    }.offset(
        x = with(density) { state.offset.x.toDp() },
        y = with(density) { state.offset.y.toDp() }
    )


    MBox(modifier = boxModifier) {
        if (state.isShowBar) {
            ResizeBarBar(modifier = barMod.align(barAlignmentValue))
        }
        if (state.isShowThumb) {
            ResizeBarThumb(
                modifier = thumbModifier.align(if (state.isShowBar) barAlignmentValue else thumbAlignmentValue)
            )
        }
    }
}

fun main() {
    testApp {
        MBox(modifier = Modifier.size(300.dp, 300.dp).border(1.dp, Color.Red)) {
            ResizeBar(
                ResizeBarStateImp(
                    rulerType = RulerType.Row,
                    offset = Offset(20F, 20F),
                    isShowBar = true,
                    isShowThumb = true,
                    thumbSize = 20.dp
                )
            )
        }
    }
}
