package com.qxdzbc.p6.ui.document.worksheet.cursor

import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.OffsetUtils.toIntOffset
import com.qxdzbc.common.compose.StateUtils.rms
import com.qxdzbc.common.compose.key_event.PKeyEvent.Companion.toPKeyEvent
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.ui.app.cell_editor.in_cell.CellEditorView
import com.qxdzbc.p6.ui.app.cell_editor.in_cell.state.CellEditorState
import com.qxdzbc.p6.ui.common.p6R
import com.qxdzbc.p6.ui.document.worksheet.action.WorksheetActionTable
import com.qxdzbc.p6.ui.document.worksheet.cursor.actions.CursorAction
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorFocusState
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState

/**
 * Cursor view consist of:
 *  - an invisible view that handle user keyboard input
 *  - views depicting selected, copied, referred cells, ranges
 */
@Composable
fun CursorViewCv(
    state: CursorState,
    cellLayoutCoorsMap: Map<CellAddress, LayoutCoorWrapper>,
    currentDisplayedRange: RangeAddress,
    cursorAction: CursorAction,
    focusState: CursorFocusState,
    modifier: Modifier = Modifier,
    enableTestTag: Boolean = false,
    worksheetActionTable: WorksheetActionTable,
) {
    val mainCell: CellAddress = state.mainCell
    val anchorSize = cellLayoutCoorsMap[mainCell]?.size ?: DpSize(0.dp, 0.dp)
    val fc = remember { FocusRequester() }
    //bound layout of anchor cell
    var boundLayoutCoors: LayoutCoordinates? by rms(null)

    LaunchedEffect(focusState.isCursorFocused) {
        if (focusState.isCursorFocused) {
            fc.requestFocus()
        }
    }

    // x: this a invisible box that matches the whole cell grid in size and contains the anchor cell, cell editor, and all the annotation views (selected, copied, referred cells)
    MBox(modifier = Modifier
        .fillMaxSize()
        .onGloballyPositioned {
            boundLayoutCoors = it
        }) {
        val blc = boundLayoutCoors
        val anchorOffset: IntOffset = if (blc != null && blc.isAttached) {
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
            CellEditorView(
                state = state.cellEditorState,
                action = worksheetActionTable.cellEditorAction,
                isFocused = focusState.isEditorFocused,
                size = anchorSize,
            )
        }


        // x: this is anchorCell
        if (!state.cellEditorState.isActive || state.cellEditorState.rangeSelectorCursorId == state.id) {
            MBox(
                modifier = Modifier
                    .then(modifier)
                    .focusRequester(fc)
                    .focusable(true)
                    .offset { anchorOffset }
                    .size(anchorSize)
                    .then(p6R.border.mod.cursorBorder)
                    .then(if (enableTestTag) Modifier.testTag(makeCursorTestTag()) else Modifier)
                    .onPreviewKeyEvent { keyEvent ->
                        cursorAction.handleKeyboardEvent(keyEvent.toPKeyEvent(), state)
                    }
            )
        }
        val refRangeAndColorMap: Map<RangeAddress, Color> = cursorAction.getFormulaRangeAndColor(state)
        if (blc != null && blc.isAttached) {
            //x: draw boxes over selected/copied/referred cells
            Canvas(modifier = Modifier.fillMaxSize()) {
                val visibleRefRangeAndColor: Map<RangeAddress?, Color> = refRangeAndColorMap.mapKeys { (range, _) ->
                    range.intersect(currentDisplayedRange)
                }
                for ((r, c) in visibleRefRangeAndColor) {
                    r?.also {
                        val topLeftCoor = cellLayoutCoorsMap[r.topLeft]
                        val botRightCoor = cellLayoutCoorsMap[r.botRight]
                        if (topLeftCoor != null && botRightCoor != null) {
                            if (topLeftCoor.isAttached() && botRightCoor.isAttached()) {
                                val offset = blc.windowToLocal(topLeftCoor.posInWindow)
                                val size = if(r.isCell()){
                                    topLeftCoor.size.toSize()
                                }else{
                                    val botRightOffset = blc.windowToLocal(botRightCoor.posInWindow)
                                    Size(botRightOffset.x - offset.x + botRightCoor.size.width.value, botRightOffset.y - offset.y+botRightCoor.size.height.value)
                                }
                                drawRect(
                                    color = c,
                                    topLeft = offset,
                                    size = size,
                                    style = p6R.canvas.stroke.dashLine
                                )
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
                                style = p6R.canvas.stroke.dashLine
                            )
                        }
                    }
                }
            }
        }
    }
}

