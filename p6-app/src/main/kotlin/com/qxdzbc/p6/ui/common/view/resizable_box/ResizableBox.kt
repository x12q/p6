package com.qxdzbc.p6.ui.common.view.resizable_box

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.qxdzbc.p6.app.build.DebugFunctions.debug
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.common.compose.StateUtils.rms
import com.qxdzbc.p6.ui.common.compose.P6TestApp
import com.qxdzbc.p6.ui.common.view.BorderBox
import com.qxdzbc.common.compose.view.MBox
import java.util.*

@Composable
fun ResizableBox(
    style: EnumSet<ResizeStyle> = ResizeStyle.ALL,
    initHeight:Int,
    initWidth:Int,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit = {}
){
    val initState = ResizableBoxState(
        heightMs = ms(initHeight.dp),
        widthMs = ms(initWidth.dp)
    )
    ResizableBox(
        style,initState,modifier, content
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ResizableBox(
    style: EnumSet<ResizeStyle> = ResizeStyle.ALL,
    initState: ResizableBoxState = ResizableBoxState.default,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit = {}
) {
    var state by rms(initState)
    val resizeStripWidth = state.resizeStripWidth

    val commonMod = Modifier
        .onPointerEvent(PointerEventType.Release) { state.onRelease() }

    MBox(
        modifier = modifier
            .onGloballyPositioned { state.boxLc.value = it }
            .height(state.heightMs.value)
            .width(state.widthMs.value)
    ) {
        content()
        if (ResizeStyle.__TOP in style) {
            val op = ResizeOperations.getOperationFor(ResizeStyle.__TOP)
            MBox(modifier = Modifier.onGloballyPositioned {
                state.thumbLCTop.value = it
            }.align(Alignment.TopCenter).height(resizeStripWidth)
                .fillMaxWidth()
                .background(Color.Blue.copy(alpha=0.5F).debug())
                .pointerHoverIcon(op.icon).then(commonMod)
                .onPointerEvent(PointerEventType.Press) { pte ->
                    state = state.onPress(pte, ResizeStyle.__TOP)
                }.onPointerEvent(PointerEventType.Move) { pte ->
                    state = state.onMove(pte, ResizeStyle.__TOP)
                })
        }

        if (ResizeStyle.__BOT in style) {
            val op = ResizeOperations.getOperationFor(ResizeStyle.__BOT)
            MBox(modifier = Modifier.onGloballyPositioned {
                state.thumbLCBot.value = it
            }.align(Alignment.BottomCenter)
                .height(resizeStripWidth)
                .fillMaxWidth()
                .background(Color.Blue.copy(alpha=0.5F).debug())
                .pointerHoverIcon(op.icon)
                .then(commonMod)
                .onPointerEvent(PointerEventType.Press) { pte ->
                    state = state.onPress(pte, ResizeStyle.__BOT)
                }.onPointerEvent(PointerEventType.Move) { pte ->
                    state = state.onMove(pte, ResizeStyle.__BOT)
                })
        }
        if (ResizeStyle.__LEFT in style) {
            val op = ResizeOperations.getOperationFor(ResizeStyle.__LEFT)
            MBox(modifier = Modifier.onGloballyPositioned {
                state.thumbLCLeft.value = it
            }.align(Alignment.CenterStart)
                .width(resizeStripWidth)
                .fillMaxHeight()
                .background(Color.Blue.copy(alpha=0.5F).debug())
                .pointerHoverIcon(op.icon)
                .then(commonMod)
                .onPointerEvent(PointerEventType.Press) { pte ->
                    state = state.onPress(pte, ResizeStyle.__LEFT)
                }.onPointerEvent(PointerEventType.Move) { pte ->
                    state = state.onMove(pte, ResizeStyle.__LEFT)
                })
        }
        if (ResizeStyle.__RIGHT in style) {
            val op = ResizeOperations.getOperationFor(ResizeStyle.__RIGHT)
            MBox(modifier = Modifier.onGloballyPositioned {
                state.thumbLCRight.value = it
            }.align(Alignment.CenterEnd)
                .width(resizeStripWidth)
                .fillMaxHeight()
                .background(Color.Blue.copy(alpha=0.5F).debug())
                .pointerHoverIcon(op.icon)
                .then(commonMod)
                .onPointerEvent(PointerEventType.Press) { pte ->
                    state = state.onPress(pte, ResizeStyle.__RIGHT)
                }.onPointerEvent(PointerEventType.Move) { pte ->
                    state = state.onMove(pte, ResizeStyle.__RIGHT)
                })
        }

    }
}

private fun main() {
    P6TestApp(size = DpSize(300.dp, 300.dp)) {
        BorderBox(modifier = Modifier.fillMaxHeight()) {
            BorderBox (modifier = Modifier.align(Alignment.BottomCenter)){
                ResizableBox(style = ResizeStyle.TOP, /*modifier = Modifier.fillMaxHeight()*/) { }
            }
        }
    }
}
