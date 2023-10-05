package com.qxdzbc.p6.ui.worksheet.ruler

import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import com.qxdzbc.p6.common.utils.CellLabelNumberSystem
import com.qxdzbc.common.compose.LayoutCoorsUtils.toP6Layout
import com.qxdzbc.common.compose.OtherComposeFunctions.isNonePressed
import com.qxdzbc.p6.ui.common.view.BorderBox
import com.qxdzbc.p6.ui.common.view.BorderStyle
import com.qxdzbc.p6.ui.worksheet.ruler.actions.RulerAction
import com.qxdzbc.p6.ui.worksheet.slider.GridSlider
import com.qxdzbc.p6.ui.theme.P6Theme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RulerView(
    state: RulerState,
    rulerAction: RulerAction,
    size: Dp,
    modifier: Modifier = Modifier,
) {
    val dimen = state.type
    val slider: GridSlider = state.sliderMs.value
    val itemIndexRange: IntRange = if (dimen == RulerType.Row) slider.visibleRowRangeIncludeMargin else slider.visibleColRangeIncludeMargin
    val firstIndex: Int = itemIndexRange.first
    val lastIndex: Int = itemIndexRange.last
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
                    modifier = modifier
                ) {
                    for (itemIndex in firstIndex..lastIndex) {
                        val bs = if (itemIndex != lastIndex) BorderStyle.BOT_RIGHT else BorderStyle.RIGHT
                        val itemSize = DpSize(
                            width = size,
                            height = state.getItemSizeOrDefault(itemIndex)
                        )
                        // TODO this outer box can be abstracted away to make a cleaner look function
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
                                    rulerAction.updateItemLayout(itemIndex, it.toP6Layout(), state,)
                                }

                        ) {
                            RulerLabel(itemIndex.toString())

                            RowResizer(state, rulerAction, itemIndex, Modifier.align(Alignment.BottomStart))
                        }
                    }
                }
            }
            RulerType.Col -> {
                Row(
                    modifier = modifier
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
                        // TODO this outer box can be abstracted away to make a cleaner look function
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
                                    rulerAction.updateItemLayout(itemIndex, it.toP6Layout(), state,)
                                }
                        ) {

                            RulerLabel(CellLabelNumberSystem.numberToLabel(itemIndex))

                            ColResizer(
                                state, rulerAction, itemIndex, Modifier.align(Alignment.BottomEnd)
                            )
                        }
                    }
                }
            }
        }
    }
}

