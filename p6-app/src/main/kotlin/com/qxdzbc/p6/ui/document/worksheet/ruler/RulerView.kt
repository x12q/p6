package com.qxdzbc.p6.ui.document.worksheet.ruler

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import com.qxdzbc.p6.build.DebugFunctions.debug
import com.qxdzbc.p6.app.common.utils.CellLabelNumberSystem
import com.qxdzbc.common.compose.LayoutCoorsUtils.wrap
import com.qxdzbc.common.compose.OtherComposeFunctions.isNonePressed
import com.qxdzbc.common.compose.PointerEventUtils.executeOnReleaseThenConsumed
import com.qxdzbc.common.compose.density_converter.FloatToDpConverter
import com.qxdzbc.p6.ui.common.view.BorderBox
import com.qxdzbc.p6.ui.common.view.BorderStyleValue
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.p6.ui.common.view.BorderStyle
import com.qxdzbc.p6.ui.document.worksheet.WorksheetConstants
import com.qxdzbc.p6.ui.document.worksheet.ruler.actions.RulerAction
import com.qxdzbc.p6.ui.document.worksheet.slider.GridSlider
import com.qxdzbc.p6.ui.theme.P6Theme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RulerView(
    state: RulerState,
    rulerAction: RulerAction,
    size: Dp,
    rulerModifier: Modifier = Modifier,
) {
    val dimen = state.type
    val slider: GridSlider = state.sliderMs.value
    val itemIndexRange: IntRange = if (dimen == RulerType.Row) slider.visibleRowRange else slider.visibleColRange
    val firstIndex: Int = itemIndexRange.first
    val lastIndex: Int = itemIndexRange.last
    val density = LocalDensity.current
    Surface(
        color = P6Theme.color.uiColor.rulerBackground,
        modifier = Modifier
            .onGloballyPositioned {
                rulerAction.updateRulerLayout(it, state,)
            }
            .onPointerEvent(PointerEventType.Press) {
                val mp = it.changes.first().position
                rulerAction.startDragSelection(mp, state,)
            }
            .onPointerEvent(PointerEventType.Move) {
                if (it.keyboardModifiers.isNonePressed) {
                    val mp = it.changes.first().position
                    rulerAction.makeMouseDragSelectionIfPossible(mp, state,)
                }
            }
            .onPointerEvent(PointerEventType.Release) {
                rulerAction.stopDragSelection(state)
            }
    ) {
        when (dimen) {
            RulerType.Row -> {
                Column(
                    modifier = rulerModifier
                        .wrapContentSize(
                            unbounded = true,
                            align = Alignment.TopStart
                        )
                ) {
                    for (itemIndex in firstIndex..lastIndex) {
                        val bs = if (itemIndex != lastIndex) BorderStyle.BOT_RIGHT else BorderStyle.RIGHT
                        val itemSize = DpSize(
                            width = size,
                            height = state.getItemSizeOrDefault(itemIndex)
                        )
                        BorderBox(
                            borderStyle = bs,
                            modifier = Modifier
                                .size(itemSize)
                                .onPointerEvent(PointerEventType.Press) {
                                    if (it.keyboardModifiers.isNonePressed) {
                                        rulerAction.clickRulerItem(itemIndex, state)
                                    } else {
                                        if (it.keyboardModifiers.isShiftPressed) {
                                            rulerAction.shiftClick(itemIndex,state)
                                        }
                                        if (it.keyboardModifiers.isCtrlPressed) {
                                            rulerAction.ctrlClick(itemIndex,state)
                                        }
                                    }
                                }
                                .onGloballyPositioned {
                                    rulerAction.updateItemLayout(itemIndex, it.wrap(), state,)
                                }

                        ) {
                            RulerLabelView(itemIndex.toString())

                            // x: resizer
                            MBox(modifier = Modifier
                                .onGloballyPositioned {
                                    rulerAction.updateResizerLayout(itemIndex, it,state)
                                }
                                .height(WorksheetConstants.resizerThickness)
                                .fillMaxWidth()
                                .background(Color.Magenta.debug())
                                .align(Alignment.BottomStart)
                                .pointerHoverIcon(P6Theme.icons.downResize)
                                .onPointerEvent(PointerEventType.Enter) {
                                    rulerAction.showRowResizeBarThumb(itemIndex,state)
                                }
                                .onPointerEvent(PointerEventType.Exit) {
                                    rulerAction.hideRowResizeBarThumb(state)
                                }
                                .onPointerEvent(PointerEventType.Press) { pte ->
                                    val itemlayout = state.getResizerLayout(itemIndex)
                                    if (itemlayout != null && itemlayout.isAttached) {
                                        val mousePos = pte.changes.first().position
                                        rulerAction.startRowResizing(itemlayout.localToWindow(mousePos),state)
                                    }
                                }
                                .onPointerEvent(PointerEventType.Move) { pte ->
                                    if (pte.changes.first().pressed) {
                                        val itemlayout = state.getResizerLayout(itemIndex)
                                        if (itemlayout != null && itemlayout.isAttached) {
                                            val change = pte.changes.first()
                                            val mousePos = change.position
                                            rulerAction.moveRowResizer(
                                                itemlayout.localToWindow(mousePos),state
                                            )
                                        }
                                    }
                                }
                                .onPointerEvent(PointerEventType.Release) {
                                    it.executeOnReleaseThenConsumed {
                                        rulerAction.finishRowResizing(itemIndex,state, FloatToDpConverter(density))
                                    }
                                }
                            )
                        }
                    }
                }
            }
            RulerType.Col -> {
                Row(
                    modifier = rulerModifier
                        .wrapContentSize(
                            unbounded = true,
                            align = Alignment.TopStart
                        )
                ) {
                    for (itemIndex in firstIndex..lastIndex) {
                        val bs = if (itemIndex != lastIndex) BorderStyle.BOT_RIGHT else BorderStyle.BOT
                        val itemSize = DpSize(
                            width = state.getItemSizeOrDefault(itemIndex),
                            height = size,
                        )
                        BorderBox(
                            borderStyle = bs,
                            modifier = Modifier
                                .size(itemSize)
                                .onPointerEvent(PointerEventType.Press) {
                                    if (it.keyboardModifiers.isNonePressed) {
                                        rulerAction.clickRulerItem(itemIndex, state)
                                    } else {
                                        if (it.keyboardModifiers.isShiftPressed) {
                                            rulerAction.shiftClick(itemIndex,state)
                                        }
                                        if (it.keyboardModifiers.isCtrlPressed) {
                                            rulerAction.ctrlClick(itemIndex,state)
                                        }
                                    }
                                }
                                .onGloballyPositioned {
                                    rulerAction.updateItemLayout(itemIndex, it.wrap(), state,)
                                }
                        )
                        {
                            RulerLabelView(CellLabelNumberSystem.numberToLabel(itemIndex))
                            // x: resizer
                            MBox(modifier = Modifier
                                .onGloballyPositioned {
                                    rulerAction.updateResizerLayout(itemIndex, it,state)
                                }
                                .width(WorksheetConstants.resizerThickness)
                                .fillMaxHeight()
                                .background(Color.Magenta.debug())
                                .align(Alignment.BottomEnd)
                                .pointerHoverIcon(P6Theme.icons.rightResize)
                                .onPointerEvent(PointerEventType.Enter) {
                                    rulerAction.showColResizeBarThumb(itemIndex,state)
                                }
                                .onPointerEvent(PointerEventType.Exit) {
                                    rulerAction.hideColResizeBarThumb(state)
                                }
                                .onPointerEvent(PointerEventType.Press) { pte ->
                                    val itemLayout = state.getResizerLayout(itemIndex)
                                    if (itemLayout != null && itemLayout.isAttached) {
                                        val mousePos = pte.changes.first().position
                                        val resizerPos = itemLayout.localToWindow(mousePos)
                                        rulerAction.startColResizing(resizerPos,state)
                                    }
                                }
                                .onPointerEvent(PointerEventType.Move) { pte ->
                                    val change = pte.changes.first()
                                    val mousePos = change.position
                                    val itemLayout = state.getResizerLayout(itemIndex)
                                    if (itemLayout != null && itemLayout.isAttached) {
                                        val resizerPos = itemLayout.localToWindow(mousePos)
                                        rulerAction.moveColResizer(resizerPos,state)
                                    }
                                }
                                .onPointerEvent(PointerEventType.Release) {
                                    rulerAction.finishColResizing(itemIndex,state, FloatToDpConverter(density))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}


//fun main() {
//    testApp {
//        var t by rms(0F)
//        var index by rms(0)
//        var clicked by rms(0)
//
//        val rulerState = RulerStateImp(
//            dimen = RulerType.Col,
//            sliderMs = ms(GridSliders.default()),
//        )
//
//        P6Theme(ThemeType.GRAY) {
//            Column {
//                Text("${index} :${t}")
//                Text("Clicked: ${clicked}")
//                Column {
//                    Ruler(
//                        state = rulerState,
//                        size = 30,
//                        rulerAction = RulerActionDoNothing()
//                    )
//                }
//            }
//        }
//    }
//}
