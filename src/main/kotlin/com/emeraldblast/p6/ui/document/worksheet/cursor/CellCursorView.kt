package com.emeraldblast.p6.ui.document.worksheet.cursor

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.emeraldblast.p6.app.common.utils.key_event.PKeyEvent.Companion.toPKeyEvent
import com.emeraldblast.p6.app.document.cell.address.CellAddress
import com.emeraldblast.p6.ui.document.worksheet.action.WorksheetActionTable
import com.emeraldblast.p6.ui.common.compose.layout_coor_wrapper.LayoutCoorWrapper
import com.emeraldblast.p6.ui.common.compose.StateUtils.rms
import com.emeraldblast.p6.ui.common.compose.OffsetUtils.toIntOffset
import com.emeraldblast.p6.ui.common.view.MBox
import com.emeraldblast.p6.ui.document.worksheet.cursor.actions.CursorAction
import com.emeraldblast.p6.ui.document.worksheet.cursor.state.CursorFocusState
import com.emeraldblast.p6.ui.document.worksheet.cursor.state.CursorState
import com.emeraldblast.p6.ui.app.cell_editor.in_cell.CellEditorView

/**
 * an invisible view that is in charged of handling users' action (keyboard, mouse) that mutate the cursor state
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
    val mainCell:CellAddress = state.mainCell
    val anchorSize = cellLayoutCoorsMap[mainCell]?.size ?: DpSize(0.dp, 0.dp)
    val fc = remember{FocusRequester()}
    //bound layout of anchor cell
    var boundLayoutCoors: LayoutCoordinates? by rms(null)

//    Loggers.renderLogger.debug("render cursor")

    // x: this box contain the anchor cell and cell editor
    MBox(modifier = Modifier.fillMaxSize().onGloballyPositioned {
        boundLayoutCoors = it
    }) {
        val anchorOffset = if (boundLayoutCoors != null && (boundLayoutCoors?.isAttached ?: false)) {
            val anchorOffset = cellLayoutCoorsMap[mainCell]?.posInWindow
            if (anchorOffset != null) {
                val offset = IntOffset(
                    x = (anchorOffset.x - boundLayoutCoors!!.positionInWindow().x).toInt(),
                    y = (anchorOffset.y - boundLayoutCoors!!.positionInWindow().y).toInt()
                )
                offset
            } else {
                IntOffset(0, 0)
            }
        } else {
            IntOffset(0, 0)
        }
            val editorState = state.cellEditorState
            val editTarget:CellAddress? = editorState.targetCell
            val editorOffset =if (boundLayoutCoors != null && (boundLayoutCoors?.isAttached ?: false)) {
                val editTargetOffset = editTarget?.let { cellLayoutCoorsMap[it]?.posInWindow }
                editTargetOffset?.let {
                    val offset = IntOffset(
                        x = (editTargetOffset.x - boundLayoutCoors!!.positionInWindow().x).toInt(),
                        y = (editTargetOffset.y - boundLayoutCoors!!.positionInWindow().y).toInt()
                    )
                    offset
                } ?:IntOffset(0, 0)
            } else {
                IntOffset(0, 0)
            }
            val editorAction = worksheetActionTable.cellEditorAction
        MBox(modifier = Modifier
            .offset { editorOffset }){
            CellEditorView(
                state = state.cellEditorState,
                action = editorAction,
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

    }

    //x: draw selection box
    for ((cellAddress, cellLayout) in cellLayoutCoorsMap) {
        if (cellAddress != mainCell) {
            //x: draw selection box over currently selected cell/range
            if (state.isPointingTo(cellAddress)) {
                val boundLayout = boundLayoutCoors
                if (boundLayout != null && boundLayout.isAttached) {
                    val offset = boundLayout.windowToLocal(cellLayout.posInWindow)
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
    SideEffect {
        if(focusState.isCursorFocused){
            fc.requestFocus()
        }
    }
}

fun makeCursorTestTag(): String {
    return "CursorTestTag"
}
