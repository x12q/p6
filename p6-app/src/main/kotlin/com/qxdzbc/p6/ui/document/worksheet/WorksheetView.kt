package com.qxdzbc.p6.ui.document.worksheet

import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.qxdzbc.p6.app.common.utils.Loggers
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.p6.ui.document.worksheet.action.WorksheetActionTable
import com.qxdzbc.p6.ui.common.p6R
import com.qxdzbc.p6.ui.common.view.BorderBox
import com.qxdzbc.p6.ui.common.view.BorderStyle
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.p6.app.action.worksheet.WorksheetAction
import com.qxdzbc.common.compose.OtherComposeFunctions.addTestTag
import com.qxdzbc.p6.ui.document.worksheet.cursor.CursorView
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.document.worksheet.resize_bar.ResizeBar
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerView
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetState
import com.qxdzbc.p6.ui.window.focus_state.WindowFocusState


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
    Surface(modifier = Modifier.onGloballyPositioned {
        wsActions.updateWsLayoutCoors(it, wsState)
    }) {
        Loggers.renderLogger.debug("render worksheet view surface")
        MBox {
            Column {
                Row {
                    BorderBox( // x: cell/range indicator
                        style = BorderStyle.BOT_RIGHT,
                        modifier = Modifier.size(
                            DpSize(
                                p6R.size.value.rowRulerWidth.dp,
                                p6R.size.value.defaultRowHeight.dp
                            )
                        )
                    ) {
                        Text(
                            text = cursorState.mainCell.toRawLabel(),
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    MBox {
                        val colRulerAction = worksheetActionTable.colRulerAction
                        RulerView(
                            state = wsState.colRulerState,
                            rulerAction = colRulerAction,
                            size = p6R.size.value.defaultRowHeight
                        )
                    }
                }
                Row {
                    MBox {
                        val rowRulerAction = worksheetActionTable.rowRulerAction
                        RulerView(
                            //x: this is row ruler
                            state = wsState.rowRulerState,
                            rulerAction = rowRulerAction,
                            size = p6R.size.value.rowRulerWidth
                        )
                    }
                    MBox(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        CellGridView(
                            wsState = wsState,
                            wsActions = wsActions,
                            modifier = Modifier
                                .onPointerEvent(PointerEventType.Scroll) { pointerEvent ->
                                    val x: Int = pointerEvent.changes.first().scrollDelta.x.toInt()
                                    val y: Int = pointerEvent.changes.first().scrollDelta.y.toInt()
                                    wsActions.scroll(x, y, wsState)
                                }
                                .then(addTestTag(enableTestTag, makeWorksheetTestTag(ws))),
                            enableTestTag = enableTestTag
                        )
                        MBox {
                            val cursorAction = worksheetActionTable.cursorAction
                            CursorView(
                                state = wsState.cursorState,
                                currentDisplayedRange= wsState.slider.currentDisplayedRange,
                                cellLayoutCoorsMap = wsState.cellLayoutCoorMap,
                                cursorAction = cursorAction,
                                focusState=focusState,
                                modifier = Modifier
                                    .then(addTestTag(enableTestTag, makeCursorTestTag(wsName))),
                                worksheetActionTable = worksheetActionTable
                            )
                        }
                    }
                }
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
}

fun makeWorksheetTestTag(worksheet: Worksheet): String {
    return worksheet.name
}

fun makeCursorTestTag(worksheetName: String): String {
    return "cursor_${worksheetName}"
}
//
//fun main() = testApp {
//
//    val ws = WorksheetImp("sheet1")
//
//    val wsStateMs: MutableState<WorksheetState> = WorksheetStateImp.rememberMs(
//        workbookKey = WorkbookKey("Wb", null),
//        worksheet = ws,
//        cursorState = CursorStates.defaultRememberMs(),
//        slider = GridSliders.defaultMs()
//    )
//
//    val ec = rememberCoroutineScope()
//    P6Theme(ThemeType.GRAY) {
//        WorksheetView(
//            wsState = wsStateMs.value,
//            wsActions = WorksheetActionsImp(
//                WorksheetStateActionsImp(wsStateMs),
//                WorksheetSideEffectsDoNothing,
//                wsStateMs,
//
//            ),
//            executionScope = ec
//        )
//    }
//}
