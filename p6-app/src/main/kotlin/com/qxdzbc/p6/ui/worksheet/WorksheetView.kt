package com.qxdzbc.p6.ui.worksheet

import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.layout.onGloballyPositioned
import com.qxdzbc.p6.document_data_layer.worksheet.Worksheet
import com.qxdzbc.p6.ui.worksheet.action.WorksheetActionTable
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.p6.composite_actions.worksheet.WorksheetAction
import com.qxdzbc.common.compose.OtherComposeFunctions.addTestTag
import com.qxdzbc.p6.ui.worksheet.cursor.CellCursor
import com.qxdzbc.p6.ui.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.worksheet.resize_bar.ResizeBar
import com.qxdzbc.p6.ui.worksheet.state.WorksheetState
import com.qxdzbc.p6.ui.window.focus_state.WindowFocusState
import com.qxdzbc.p6.ui.worksheet.range_indicator.CellRangeIndicator
import com.qxdzbc.p6.ui.worksheet.ruler.ColumRulerView
import com.qxdzbc.p6.ui.worksheet.ruler.RowRulerView
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.ScrollBar
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.action.ScrollBarActionData


/**
 * Worksheet view + rulers + cursor + slider + selection rect
 * @param executionScope coroutine scope to run formula
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WorksheetView(
    wsState: WorksheetState,
    worksheetActionTable: WorksheetActionTable,
    focusState: WindowFocusState,
    enableTestTag: Boolean = false,
) {
    val ws: Worksheet = wsState.worksheet
    val wsActions: WorksheetAction = worksheetActionTable.worksheetAction
    val wsName = ws.name
    val cursorState: CursorState by wsState.cursorStateMs
    val localAction = wsState.localAction

    Surface(modifier = Modifier.onGloballyPositioned {
        wsActions.updateWsLayoutCoors(it, wsState)
    }) {
        Column {
            Row(Modifier.weight(1f)) {
                Column(Modifier.weight(1f)) {
                    Row {
                        CellRangeIndicator(cursorState.mainCell.label)
                        ColumRulerView(
                            state = wsState.colRulerState,
                            rulerAction = worksheetActionTable.colRulerAction,
                            size = WorksheetConstants.defaultRowHeight
                        )
                    }
                    Row {
                        RowRulerView(
                            state = wsState.rowRulerState,
                            rulerAction = worksheetActionTable.rowRulerAction,
                            size = WorksheetConstants.rowRulerWidth
                        )

                        MBox {
                            CellGrid(
                                wsState = wsState,
                                wsActions = wsActions,
                                modifier = Modifier
                                    .onPointerEvent(
                                        PointerEventType.Scroll
                                    ) { pointerEvent ->
                                        val x: Int = pointerEvent.changes.first().scrollDelta.x.toInt()
                                        val y: Int = pointerEvent.changes.first().scrollDelta.y.toInt()
                                        wsActions.onMouseScroll(x, y, wsState)
                                    }
                                    .then(addTestTag(enableTestTag, makeWorksheetTestTag(ws))),
                            )

                            CellCursor(
                                state = wsState.cursorState,
                                currentDisplayedRange = wsState.slider.currentDisplayedRange,
                                action = worksheetActionTable.cursorAction,
                                focusState = focusState,
                                modifier = Modifier
                                    .then(addTestTag(enableTestTag, makeCursorTestTag(wsName))),
                            )
                        }
                    }
                }

                ScrollBar(
                    state = wsState.verticalScrollBarState,
                    actions = localAction.verticalScrollBarAction,
                    onDrag = { data ->
                        localAction.verticalScrollBarAction.runAction(data)
                    },
                    onClickOnRail = { data ->
                        localAction.verticalScrollBarAction.runAction(data)
                    },
                )
            }

            ScrollBar(
                state = wsState.horizontalScrollBarState,
                actions = localAction.horizontalScrollBarAction,
                onDrag = { data ->
                    localAction.horizontalScrollBarAction.runAction(data)
                },
                onClickOnRail = { data ->
                    localAction.horizontalScrollBarAction.runAction(data)
                },
            )
        }

        MBox {
            ResizeBar(
                state = wsState.colResizeBarState
            )
        }
        MBox {
            ResizeBar(
                state = wsState.rowResizeBarState
            )
        }
    }
}

fun makeWorksheetTestTag(worksheet: Worksheet): String {
    return worksheet.name
}

fun makeCursorTestTag(worksheetName: String): String {
    return "cursor_${worksheetName}"
}
