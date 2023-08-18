package com.qxdzbc.p6.ui.worksheet

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
import com.qxdzbc.p6.document_data_layer.worksheet.Worksheet
import com.qxdzbc.p6.ui.worksheet.action.WorksheetActionTable
import com.qxdzbc.p6.ui.common.view.BorderBox
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.p6.composite_actions.worksheet.WorksheetAction
import com.qxdzbc.common.compose.OtherComposeFunctions.addTestTag
import com.qxdzbc.p6.ui.common.view.BorderStyle
import com.qxdzbc.p6.ui.worksheet.cursor.CellCursor
import com.qxdzbc.p6.ui.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.worksheet.resize_bar.ResizeBar
import com.qxdzbc.p6.ui.worksheet.ruler.RulerView
import com.qxdzbc.p6.ui.worksheet.state.WorksheetState
import com.qxdzbc.p6.ui.window.focus_state.WindowFocusState
import com.qxdzbc.p6.ui.worksheet.range_indicator.CellRangeIndicator
import com.qxdzbc.p6.ui.worksheet.slider.edge_slider.VerticalEdgeSlider


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
        Column {
            Row {

                CellRangeIndicator(cursorState.mainCell.label)

                val colRulerAction = worksheetActionTable.colRulerAction
                RulerView(
                    state = wsState.colRulerState,
                    rulerAction = colRulerAction,
                    size = WorksheetConstants.defaultRowHeight
                )
            }
            Row {

                val rowRulerAction = worksheetActionTable.rowRulerAction
                RulerView(
                    //x: this is row ruler
                    state = wsState.rowRulerState,
                    rulerAction = rowRulerAction,
                    size = WorksheetConstants.rowRulerWidth
                )

                MBox(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    CellGrid(
                        wsState = wsState,
                        wsActions = wsActions,
                        modifier = Modifier
                            .onPointerEvent(PointerEventType.Scroll) { pointerEvent ->
                                val x: Int = pointerEvent.changes.first().scrollDelta.x.toInt()
                                val y: Int = pointerEvent.changes.first().scrollDelta.y.toInt()
                                wsActions.scroll(x, y, wsState)
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
//
//fun main() = testApp {
//
//    val ws = WorksheetImp("sheet1")
//
//    val wsState: WorksheetState = WorksheetStateImp.rememberMs(
//        workbookKey = WorkbookKey("Wb", null),
//        worksheet = ws,
//        cursorState = CursorStates.defaultRememberMs(),
//        slider = GridSliders.defaultMs()
//    )
//
//    val ec = rememberCoroutineScope()
//    P6Theme(ThemeType.GRAY) {
//        WorksheetView(
//            wsState = wsState.value,
//            wsActions = WorksheetActionsImp(
//                WorksheetStateActionsImp(wsState),
//                WorksheetSideEffectsDoNothing,
//                wsState,
//
//            ),
//            executionScope = ec
//        )
//    }
//}
