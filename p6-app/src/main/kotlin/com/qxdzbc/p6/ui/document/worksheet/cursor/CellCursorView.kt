package com.qxdzbc.p6.ui.document.worksheet.cursor

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
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
import com.qxdzbc.p6.ui.app.cell_editor.in_cell.CellEditorView
import com.qxdzbc.p6.ui.app.cell_editor.in_cell.state.CellEditorState
import com.qxdzbc.p6.ui.document.worksheet.action.WorksheetActionTable
import com.qxdzbc.p6.ui.document.worksheet.cursor.actions.CursorAction
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorFocusState
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState

/**
 * an invisible view that handles users' action (keyboard, mouse) that mutate the cursor state
 */
@Composable
fun CursorView(
    state: CursorState,
    cellLayoutCoorsMap: Map<CellAddress, LayoutCoorWrapper>,
    cursorAction: CursorAction,
    focusState: CursorFocusState,
    modifier: Modifier = Modifier,
    enableTestTag: Boolean = false,
    worksheetActionTable: WorksheetActionTable,
) {
    val isFocused = rms { true }
    isFocused.value = focusState.isCursorFocused
    val mainCell: CellAddress = state.mainCell
    val anchorSize = cellLayoutCoorsMap[mainCell]?.size ?: DpSize(0.dp, 0.dp)
    val fc = remember { FocusRequester() }
    //bound layout of anchor cell
    var boundLayoutCoors: LayoutCoordinates? by rms(null)

    // x: this invisible box match the cell grid in size and contains the anchor cell, cell editor, and all the annotation views (selected, copied, used markers)
    MBox(modifier = Modifier
        .fillMaxSize()
        .onGloballyPositioned {
            boundLayoutCoors = it
        }) {
        val anchorOffset:IntOffset = if (boundLayoutCoors != null && (boundLayoutCoors?.isAttached ?: false)) {
            val mainCellPosition: Offset? = cellLayoutCoorsMap[mainCell]?.posInWindow
            if (mainCellPosition != null) {
                boundLayoutCoors!!.windowToLocal(mainCellPosition).toIntOffset()
            } else {
                IntOffset(0, 0)
            }
        } else {
            IntOffset(0, 0)
        }
        val editorState:CellEditorState = state.cellEditorState
        val editTarget: CellAddress? = editorState.targetCell
        val editorOffset = if (boundLayoutCoors != null && (boundLayoutCoors?.isAttached ?: false)) {
            val editTargetOffset = editTarget?.let { cellLayoutCoorsMap[it]?.posInWindow }
            editTargetOffset?.let {
                boundLayoutCoors!!.windowToLocal(it).toIntOffset()
            } ?: IntOffset(0, 0)
        } else {
            IntOffset(0, 0)
        }
        MBox(modifier = Modifier.offset { editorOffset }) {
            CellEditorView(
                state = state.cellEditorState,
                action = worksheetActionTable.cellEditorAction,
                isFocused = focusState.isEditorFocused,
                defaultSize = anchorSize,
            )
        }


        // x: this is anchorCell

        MBox(
            modifier = Modifier
                .then(modifier)
                .focusRequester(fc)
                .focusable(true)
                .offset { anchorOffset }
                .size(anchorSize)
                .background(Color.Red.copy(alpha = 0.4F))
                .then(if (enableTestTag) Modifier.testTag(makeCursorTestTag()) else Modifier)
                .onPreviewKeyEvent { keyEvent ->
                    cursorAction.handleKeyboardEvent(keyEvent.toPKeyEvent(), state)
                }
        )
        val refRangeAndColor=cursorAction.getFormulaRangeAndColor(state)
        //x: draw a box over selected cells to indicate that they are selected
        for ((cellAddress, cellLayout) in cellLayoutCoorsMap) {
            if (boundLayoutCoors != null && boundLayoutCoors!!.isAttached) {
                val offset:Offset = boundLayoutCoors!!.windowToLocal(cellLayout.posInWindow)
                for((range,color) in refRangeAndColor){
                    if(cellAddress in range){
                        MBox(
                            modifier = Modifier
                                .offset { offset.toIntOffset() }
                                .size(cellLayout.size)
                                .background(color.copy(alpha=0.3F))
                        )
                    }
                }
            }

            if (cellAddress != mainCell) {
                //x: draw selection box over currently selected cell/range
                if (state.isPointingTo(cellAddress)) {
                    val boundLayout = boundLayoutCoors
                    if (boundLayout != null && boundLayout.isAttached) {
                        val offset:Offset = boundLayout.windowToLocal(cellLayout.posInWindow)
                        MBox(
                            modifier = Modifier
                                .offset { offset.toIntOffset() }
                                .size(cellLayout.size)
                                .background(Color.Green.copy(alpha = 0.4F))
                        )
                    }
                }
            }
            // x: draw copied range
            if (state.containInClipboard(cellAddress)) {
                val boundLayout = boundLayoutCoors
                if (boundLayout != null && boundLayout.isAttached) {
                    val offset = boundLayout.windowToLocal(cellLayout.posInWindow)
                    MBox(
                        modifier = Modifier
                            .offset { offset.toIntOffset() }
                            .size(cellLayout.size)
                            .background(Color.Magenta.copy(alpha = 0.4F))
                    )
                }
            }
        }
    }

    SideEffect {
        if (focusState.isCursorFocused) {
            fc.requestFocus()
        }
    }
}

fun makeCursorTestTag(): String {
    return "CursorTestTag"
}
