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
import com.qxdzbc.p6.ui.common.compose.P6TestApp
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.p6.ui.document.worksheet.WorksheetConstants
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerType

@Composable
fun ResizeBar(
    state: ResizeBarState
) {
    val density = LocalDensity.current
    val barMod = when (state.dimen) {
        RulerType.Col -> Modifier.width(state.thickness).fillMaxHeight()
        RulerType.Row -> Modifier.height(state.thickness).fillMaxWidth()
    }
        .background(Color.Black)

    val thumbThickness = WorksheetConstants.defaultResizeCursorThumbThickness

    val thumbMod = when (state.dimen) {
        RulerType.Col -> {
            Modifier
                .width(thumbThickness)
                .height(
                    state.size
                )
        }

        RulerType.Row -> {
            Modifier
                .height(thumbThickness)
                .width(
                    state.size
                )

        }
    }
        .background(Color.Black)

    val barAlignmentValue = when (state.dimen) {
        RulerType.Col -> Alignment.TopCenter
        RulerType.Row -> Alignment.CenterStart
    }

    val thumbAlignmentValue = when (state.dimen) {
        RulerType.Col -> Alignment.CenterEnd
        RulerType.Row -> Alignment.BottomCenter
    }

    val boxMod = when (state.dimen) {
        RulerType.Col -> Modifier.width(state.selectableAreaWidth)
        RulerType.Row -> Modifier.height(state.selectableAreaWidth)
    }.offset(
        with(density) { state.offset.x.toDp() },
        with(density) { state.offset.y.toDp() }
    )


    MBox(modifier = boxMod) {
        if (state.isShow) {
            MBox(modifier = barMod.align(barAlignmentValue))
        }
        if (state.isShowThumb) {
            MBox(
                modifier = thumbMod.align(if (state.isShow) barAlignmentValue else thumbAlignmentValue)
            )
        }
    }
}

fun main() {
    P6TestApp {
        MBox(modifier = Modifier.size(300.dp, 300.dp).border(1.dp, Color.Red)) {
            ResizeBar(
                ResizeBarStateImp(
                    dimen = RulerType.Row,
                    offset = Offset(20F, 20F),
                    isShow = true,
                    isShowThumb = true,
                    size = 20.dp
                )
            )
        }
    }
}
