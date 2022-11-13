package com.qxdzbc.p6.ui.document.worksheet.cursor

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.*
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
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.p6.app.common.key_event.P6KeyEvent.Companion.toP6KeyEvent
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.ui.app.cell_editor.CellEditorView
import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorState
import com.qxdzbc.p6.ui.common.P6R
import com.qxdzbc.p6.ui.document.worksheet.cursor.actions.CursorAction
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorFocusState
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.document.worksheet.cursor.thumb.ThumbView

/**
 * Cursor view consist of:
 *  - an invisible view that handle user keyboard input
 *  - views depicting selected, copied, referred cells, ranges
 *  cell layout map is originally part of a worksheet. But cursor view and thumb view need that map to position themselves. But the map itself should not be part of the cursor state by nature sense.
 *  Putting the map into the cursor state will simplify the CursorView signature
 */
@Composable
fun CursorView(
    state: CursorState,
    currentDisplayedRange: RangeAddress,
    action: CursorAction,
    focusState: CursorFocusState,
    modifier: Modifier = Modifier,
) {
    val cellLayoutCoorsMap: Map<CellAddress, LayoutCoorWrapper> = state.cellLayoutCoorsMap
    val mainCell: CellAddress = state.mainCell
    var boundLayoutCoorsWrapper: LayoutCoorWrapper? by rms(null)

    LaunchedEffect(Unit) {
        action.focusOnCursor(state.id)
    }

    LaunchedEffect(focusState.isCursorFocused) {
        if(focusState.isCursorFocused){
            action.focusOnCursor(state.id)
        }else{
            action.freeFocusOnCursor(state.id)
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
            val mainCellPosition: Offset? = cellLayoutCoorsMap[mainCell]?.posInWindowOrZero
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
            val editTargetOffset = editTarget?.let { cellLayoutCoorsMap[it]?.posInWindowOrZero }
            editTargetOffset?.let {
                blc.windowToLocal(it).toIntOffset()
            } ?: IntOffset(0, 0)
        } else {
            IntOffset(0, 0)
        }

        MBox(modifier = Modifier.offset { editorOffset }) {
            val editorSize = state.cellEditorState.targetCell?.let { cellLayoutCoorsMap[it] }?.sizeOrZero ?: DpSize(0.dp,0.dp)
            CellEditorView(
                state = state.cellEditorState,
                action = action.cellEditorAction,
                focusState = focusState,
                size = editorSize,
            )
        }

        // x: this is the main cell
        if (state.cellEditorState.isNotOpen || state.cellEditorState.rangeSelectorCursorId == state.id) {
            val mainCellSize = cellLayoutCoorsMap[mainCell]?.sizeOrZero ?: DpSize(0.dp, 0.dp)
            MBox(
                modifier = modifier
                    .focusRequester(focusState.cursorFocusRequester.focusRequester)
                    .focusable(true)
                    .onFocusChanged {
                        action.updateCursorFocus(state.id,it.isFocused)
                    }
                    .offset { mainCellOffset }
                    .size(mainCellSize)
                    .then(P6R.border.mod.cursorBorder)
                    .onPreviewKeyEvent { keyEvent ->
                        action.handleKeyboardEvent(keyEvent.toP6KeyEvent(), state)
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
                    action = action.thumbAction
                )
            }
        }
        val refRangeAndColorMap: Map<RangeAddress, Color> = action.getFormulaRangeAndColor(state)
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
                                val offset = blc.windowToLocal(topLeftCoor.posInWindowOrZero)
                                val size = if(r.isCell()){
                                    topLeftCoor.sizeOrZero.toSize()
                                }else{
                                    val botRightOffset = blc.windowToLocal(botRightCoor.posInWindowOrZero)
                                    Size(botRightOffset.x - offset.x + botRightCoor.sizeOrZero.width.value, botRightOffset.y - offset.y+botRightCoor.sizeOrZero.height.value)
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
                                val offset: Offset = blc.windowToLocal(cellLayout.posInWindowOrZero)
                                drawRect(
                                    color = Color.Blue.copy(alpha = 0.2F),
                                    topLeft = offset,
                                    size = cellLayout.sizeOrZero.toSize(),
                                )
                            }
                        }
                    }

                    // x: draw copied range
                    if (state.containInClipboard(cellAddress)) {
                        if (blc.isAttached) {
                            val offset = blc.windowToLocal(cellLayout.posInWindowOrZero)
                            drawRect(
                                color = Color.Magenta,
                                topLeft = offset,
                                size = cellLayout.sizeOrZero.toSize(),
                                style = P6R.canvas.stroke.dashLine
                            )
                        }
                    }
                }
                val thumbState = state.thumbState
                if(thumbState.isShowingSelectedRange){
                    if(blc.isAttached){
                        drawRect(
                            color = Color.Red.copy(alpha =0.4F),
                            topLeft = blc.windowToLocal(thumbState.selectedRangeWindowOffsetOrZero),
                            size = thumbState.selectedRangeSizeOrZero.toSize(),
                        )
                    }
                }
            }
        }
    }
}

