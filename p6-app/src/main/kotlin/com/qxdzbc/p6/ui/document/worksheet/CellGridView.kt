package com.qxdzbc.p6.ui.document.worksheet

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.PointerMatcher
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.onClick
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.OtherComposeFunctions.isNonePressed
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.p6.app.action.worksheet.WorksheetAction
import com.qxdzbc.p6.build.BuildConfig
import com.qxdzbc.p6.build.BuildVariant
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.ui.common.P6R
import com.qxdzbc.p6.ui.common.view.BorderBox
import com.qxdzbc.p6.ui.common.view.BorderStyle
import com.qxdzbc.p6.ui.document.cell.CellView
import com.qxdzbc.p6.ui.document.cell.EmptyCellView
import com.qxdzbc.p6.ui.document.cell.state.CellState
import com.qxdzbc.p6.ui.document.worksheet.select_rect.SelectRect
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetState


/**
 * Display worksheet cells
 */
@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun CellGridView(
    wsState: WorksheetState,
    wsActions: WorksheetAction,
    modifier: Modifier = Modifier,
) {
    val slider by wsState.sliderMs
   val density = LocalDensity.current
    MBox(
        modifier = modifier
            .fillMaxSize()
            .onGloballyPositioned {
                wsActions.updateCellGridLayoutCoors(it, wsState)
                wsActions.computeSliderSize(wsState,density)
            }
            .onPointerEvent(PointerEventType.Press) {
                if (it.buttons.isPrimaryPressed && it.keyboardModifiers.isNonePressed) {
                    val mp = it.changes.first().position
                    val innerLayout = wsState.cellGridLayoutCoors
                    val mousePosInWindow = if (innerLayout != null && innerLayout.isAttached) {
                        innerLayout.localToWindow(mp)
                    } else {
                        mp
                    }
                    wsActions.startDragSelection(wsState, mousePosInWindow)
                }
            }
            .onPointerEvent(PointerEventType.Move) {
                if (it.buttons.isPrimaryPressed) {
                    val mp = it.changes.first().position
                    val innerLayout = wsState.cellGridLayoutCoors
                    val mousePosInWindow = if (innerLayout != null && innerLayout.isAttached) {
                        innerLayout.localToWindow(mp)
                    } else {
                        mp
                    }
                    wsActions.makeMouseDragSelectionIfPossible(wsState, mousePosInWindow)
                }
            }
            .onPointerEvent(PointerEventType.Release) {
                wsActions.stopDragSelection(wsState)
            }
    ) {
        Row(
            modifier = Modifier.wrapContentSize(
                unbounded = true,
                align = Alignment.TopStart
            )
        ) {
            for (colIndex: Int in slider.visibleColRange) {
                val colWidth = wsState.getColumnWidthOrDefault(colIndex)
                Column(
                    modifier = Modifier
                        .wrapContentSize(
                            unbounded = true,
                            align = Alignment.TopStart
                        )
                ) {
                    for (rowIndex: Int in slider.visibleRowRange) {
                        val cellAddress = CellAddress(colIndex, rowIndex)
                        val cellState: CellState? = wsState.getCellState(colIndex, rowIndex)
                        val rowHeight = wsState.getRowHeight(rowIndex) ?: P6R.size.value.defaultRowHeight

                        // x: pick border style base on the cell position
                        val borderStyle =
                            if (colIndex == slider.lastVisibleCol && rowIndex == slider.lastVisibleRow) {
                                BorderStyle.NONE
                            } else if (colIndex == slider.lastVisibleCol) {
                                BorderStyle.BOT
                            } else if (rowIndex == slider.lastVisibleRow) {
                                BorderStyle.RIGHT
                            } else {
                                BorderStyle.BOT_RIGHT
                            }
                        val cellBoxMod = Modifier.onClick(
                            matcher = PointerMatcher.mouse(PointerButton.Primary),
                            keyboardModifiers = { isCtrlPressed },
                            onClick = {
                                wsActions.ctrlClickSelectCell(cellAddress, wsState)
                            }
                        ).onClick(
                            matcher = PointerMatcher.mouse(PointerButton.Primary),
                            keyboardModifiers = { isShiftPressed },
                            onClick = {
                                wsActions.shiftClickSelectRange(cellAddress, wsState)
                            }
                        ).padding(start=1.dp, end = 1.dp, top=1.dp,bottom=1.dp)

                        BorderBox(
                            style = borderStyle,
                            borderColor = Color.LightGray,
                            padContent = false,
                            modifier = Modifier
                                .size(colWidth, rowHeight)
                                .onGloballyPositioned {
                                    wsActions.addCellLayoutCoor(cellAddress, it, wsState)
                                }
                        ) {
                            val format = wsState.cellFormatTable.getFormat(cellAddress)
                            if (cellState != null) {
                                CellView(
                                    state = cellState,
                                    format=format,
                                    boxModifier = cellBoxMod
                                )
                            } else {
                                EmptyCellView(
                                    format = format,
                                    boxModifier = cellBoxMod
                                )
                            }
                        }
                    }
                }
            }
        }

        MBox {// this Mbox is for preventing massive cells re-drawing
            val gridLayoutCoors = wsState.cellGridLayoutCoors
            val pos = if (gridLayoutCoors != null && gridLayoutCoors.isAttached) {
                gridLayoutCoors.windowToLocal(wsState.selectRectState.rect.topLeft)
            } else {
                wsState.selectRectState.rect.topLeft
            }
            if (BuildConfig.buildVariant == BuildVariant.DEBUG) {
                SelectRect(
                    wsState.selectRectStateMs.value,
                    position = pos
                )
            }
        }
    }
}
