package com.qxdzbc.p6.ui.document.worksheet.cursor

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.LayoutCoorsUtils.wrap
import com.qxdzbc.common.compose.OffsetUtils.toIntOffset
import com.qxdzbc.common.compose.StateUtils.rms
import com.qxdzbc.common.compose.key_event.PKeyEvent.Companion.toPKeyEvent
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.ui.app.cell_editor.CellEditorView
import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorState
import com.qxdzbc.p6.ui.common.P6R
import com.qxdzbc.p6.ui.document.worksheet.action.WorksheetActionTable
import com.qxdzbc.p6.ui.document.worksheet.cursor.actions.CursorAction
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorFocusState
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.document.worksheet.cursor.thumb.ThumbView

/**
 * Cursor view consist of:
 *  - an invisible view that handle user keyboard input
 *  - views depicting selected, copied, referred cells, ranges
 *
 *  TODO move [cellLayoutCoorsMap] into [state]
 *  cell layout map is originally part of a worksheet. But cursor view and thumb view need that map to position themselves. But the map itself should not be part of the cursor state by nature sense.
 *  Putting the map into the cursor state will simplify the CursorView signature
 */
@Composable
fun CursorView(
    state: CursorState,
    currentDisplayedRange: RangeAddress,
    cursorAction: CursorAction,
    focusState: CursorFocusState,
    modifier: Modifier = Modifier,
    worksheetActionTable: WorksheetActionTable,
) {
    val cellLayoutCoorsMap: Map<CellAddress, LayoutCoorWrapper> = state.cellLayoutCoorsMap
    val mainCell: CellAddress = state.mainCell
    val fc = remember { FocusRequester() }
    // x: the whole surface layout coors
    var boundLayoutCoorsWrapper: LayoutCoorWrapper? by rms(null)

    LaunchedEffect(focusState.isCursorFocused) {
        if (focusState.isCursorFocused) {
            fc.requestFocus()
        }
    }

    // x: this an invisible box that matches the whole cell grid in size and contains the anchor cell, cell editor, and all the annotation views (selected, copied, referred cells)
    MBox(modifier = Modifier
        .fillMaxSize()
        .onGloballyPositioned {
            boundLayoutCoorsWrapper = it.wrap()
        }) {
        val blc = boundLayoutCoorsWrapper
        val mainCellOffset: IntOffset = if (blc != null && blc.isAttached) {
            val mainCellPosition: Offset? = cellLayoutCoorsMap[mainCell]?.posInWindow
            if (mainCellPosition != null) {
                blc.windowToLocal(mainCellPosition).toIntOffset()
            } else {
                IntOffset(0, 0)
            }
        } else {
            IntOffset(0, 0)
        }
        val editorState: CellEditorState = state.cellEditorState
        val editTarget: CellAddress? = editorState.targetCell
        val editorOffset = if (blc != null && blc.isAttached) {
            val editTargetOffset = editTarget?.let { cellLayoutCoorsMap[it]?.posInWindow }
            editTargetOffset?.let {
                blc.windowToLocal(it).toIntOffset()
            } ?: IntOffset(0, 0)
        } else {
            IntOffset(0, 0)
        }

        MBox(modifier = Modifier.offset { editorOffset }) {
            val editorSize = state.cellEditorState.targetCell?.let { cellLayoutCoorsMap[it] }?.size ?: DpSize(0.dp,0.dp)
            CellEditorView(
                state = state.cellEditorState,
                action = worksheetActionTable.cellEditorAction,
                isFocused = focusState.isEditorFocused,
                size = editorSize,
            )
        }

        // x: this is the main cell
        if (state.cellEditorState.isNotActive || state.cellEditorState.rangeSelectorCursorId == state.id) {
            val mainCellSize = cellLayoutCoorsMap[mainCell]?.size ?: DpSize(0.dp, 0.dp)
            MBox(
                modifier = modifier
                    .focusRequester(fc)
                    .focusable(true)
                    .offset { mainCellOffset }
                    .size(mainCellSize)
                    .then(P6R.border.mod.cursorBorder)
                    .onPreviewKeyEvent { keyEvent ->
                        cursorAction.handleKeyboardEvent(keyEvent.toPKeyEvent(), state)
                    }
            )
            val thumbState = state.thumbState
            MBox(
                modifier = Modifier
                    .offset(
                        x=mainCellOffset.x.dp + mainCellSize.width - thumbState.offsetNegate.width,
                        y=mainCellOffset.y.dp + mainCellSize.height - thumbState.offsetNegate.height,
                    )
                    .border(1.dp,Color.White)
            ){
                ThumbView(
                    state = state.thumbState,
                    action = worksheetActionTable.thumbAction
                )
            }
        }
        val refRangeAndColorMap: Map<RangeAddress, Color> = cursorAction.getFormulaRangeAndColor(state)
        if (blc != null && blc.isAttached) {
            //x: draw boxes over selected/copied/referred cells
            Canvas(modifier = Modifier.fillMaxSize()) {
                // x: draw boxes around referred range
                val visibleRefRangeAndColor: Map<RangeAddress?, Color> = refRangeAndColorMap.mapKeys { (range, _) ->
                    range.intersect(currentDisplayedRange)
                }
                for ((r, c) in visibleRefRangeAndColor) {
                    r?.also {
                        val topLeftCoor = cellLayoutCoorsMap[r.topLeft]
                        val botRightCoor = cellLayoutCoorsMap[r.botRight]
                        if (topLeftCoor != null && botRightCoor != null) {
                            if (topLeftCoor.isAttached && botRightCoor.isAttached) {
                                val offset = blc.windowToLocal(topLeftCoor.posInWindow)
                                val size = if(r.isCell()){
                                    topLeftCoor.size.toSize()
                                }else{
                                    val botRightOffset = blc.windowToLocal(botRightCoor.posInWindow)
                                    Size(botRightOffset.x - offset.x + botRightCoor.size.width.value, botRightOffset.y - offset.y+botRightCoor.size.height.value)
                                }
                                // x: dash line
                                drawRect(
                                    color = c,
                                    topLeft = offset,
                                    size = size,
                                    style = P6R.canvas.stroke.dashLine
                                )
                                // x: filled rect
                                drawRect(
                                    color = c.copy(alpha=0.3f),
                                    topLeft = offset,
                                    size = size,
                                )
                            }
                        }
                    }
                }

                for ((cellAddress, cellLayout) in cellLayoutCoorsMap) {
                    if (cellAddress != mainCell) {
                        //x: draw selection box over currently selected cell/range
                        if (state.isPointingTo(cellAddress)) {
                            if (blc.isAttached) {
                                val offset: Offset = blc.windowToLocal(cellLayout.posInWindow)
                                drawRect(
                                    color = Color.Blue.copy(alpha = 0.2F),
                                    topLeft = offset,
                                    size = cellLayout.size.toSize(),
                                )
                            }
                        }
                    }

                    // x: draw copied range
                    if (state.containInClipboard(cellAddress)) {
                        if (blc.isAttached) {
                            val offset = blc.windowToLocal(cellLayout.posInWindow)
                            drawRect(
                                color = Color.Magenta,
                                topLeft = offset,
                                size = cellLayout.size.toSize(),
                                style = P6R.canvas.stroke.dashLine
                            )
                        }
                    }
                }
                val thumbState = state.thumbState
                if(thumbState.isShowingSelectedRange){
                    println("offset: ${blc.windowToLocal(thumbState.selectedRangeWindowOffset)}")
                    println("size: ${thumbState.selectedRangeSize.toSize()}")
                    drawRect(
                        color = Color.Red.copy(alpha =0.4F),
                        topLeft = blc.windowToLocal(thumbState.selectedRangeWindowOffset),
                        size = thumbState.selectedRangeSize.toSize(),
                    )
                }
            }
        }
    }
}

