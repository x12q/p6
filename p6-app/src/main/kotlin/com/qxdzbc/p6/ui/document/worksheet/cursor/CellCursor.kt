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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.LayoutCoorsUtils.wrap
import com.qxdzbc.common.compose.OffsetUtils.rawConvertToIntOffset
import com.qxdzbc.common.compose.StateUtils.rms
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.p6.app.common.key_event.P6KeyEvent.Companion.toP6KeyEvent
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.ui.app.cell_editor.CellEditor
import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorState
import com.qxdzbc.p6.ui.document.worksheet.cursor.actions.CursorAction
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorFocusState
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.document.worksheet.cursor.thumb.ThumbView
import com.qxdzbc.p6.ui.theme.P6Theme

/**
 * Cell cursor view consist of:
 *  - an invisible view that handle user keyboard input
 *  - views highlighting selected, copied, referred cells, ranges.
 *
 *  Cell cursor plays 2 roles:
 *  - as cell cursor
 *  - and as range selector
 */
@Composable
fun CellCursor(
    state: CursorState,
    currentDisplayedRange: RangeAddress,
    action: CursorAction,
    focusState: CursorFocusState,
    modifier: Modifier = Modifier,
) {
    val cellLayoutCoorsMap: Map<CellAddress, LayoutCoorWrapper> = state.cellLayoutCoorsMap
    val mainCell: CellAddress = state.mainCell
    var boundLayoutCoorsWrapper: LayoutCoorWrapper? by rms(null)
    val density = LocalDensity.current

    LaunchedEffect(Unit) {
        action.focusOnCursor(state.id)
    }

    LaunchedEffect(focusState.isCursorFocused) {
        if(focusState.isCursorFocused){
            action.focusOnCursor(state.id)
        }
    }
    /*
     x: this an invisible box that matches the whole cell grid in size and
     contains the anchor cell, cell editor, and all the annotation views
     (selected, copied, referred cells)
     */
    MBox(modifier = Modifier
        .fillMaxSize()
        .onGloballyPositioned {
            boundLayoutCoorsWrapper = it.wrap()
        }) {
        val layout = boundLayoutCoorsWrapper
        val mainCellOffset: IntOffset = if (layout != null && layout.isAttached) {
            val mainCellPosition: Offset? = cellLayoutCoorsMap[mainCell]?.posInWindowOrZero
            if (mainCellPosition != null) {
                layout.windowToLocal(mainCellPosition).rawConvertToIntOffset()
            } else {
                IntOffset(0, 0)
            }
        } else {
            IntOffset(0, 0)
        }
        val editorState: CellEditorState = state.cellEditorState
        val editTarget: CellAddress? = editorState.targetCell
        val editorOffset = if (layout != null && layout.isAttached) {
            val editTargetOffset = editTarget?.let { cellLayoutCoorsMap[it]?.posInWindowOrZero }
            editTargetOffset?.let {
                layout.windowToLocal(it).rawConvertToIntOffset()
            } ?: IntOffset(0, 0)
        } else {
            IntOffset(0, 0)
        }

        MBox(modifier = Modifier.offset { editorOffset }) {
            val editorSize = state.cellEditorState.targetCell?.let { cellLayoutCoorsMap[it] }?.dbSizeOrZero(density) ?: DpSize(0.dp,0.dp)
            CellEditor(
                state = state.cellEditorState,
                action = action.cellEditorAction,
                focusState = focusState,
                size = editorSize,
            )
        }

        // x: this is the main cell cursor
        if (state.cellEditorState.isNotOpen || state.cellEditorState.rangeSelectorId == state.id) {
            val mainCellSize = cellLayoutCoorsMap[mainCell]?.dbSizeOrZero(density) ?: DpSize(0.dp, 0.dp)
            MBox(
                modifier = modifier
                    .focusRequester(focusState.cursorFocusRequester.focusRequester)
                    .focusable(true)
                    .onFocusChanged {
                        action.updateCursorFocus(state.id,it.isFocused)
                    }
                    .offset { mainCellOffset }
                    .size(mainCellSize)
                    .border(2.dp, P6Theme.color.ws.cursorColor)
                    .onPreviewKeyEvent { keyEvent ->
                        action.handleKeyboardEvent(keyEvent.toP6KeyEvent(), state)
                    }
            )
            val thumbState = state.thumbState
            MBox(
                modifier = Modifier
                    .offset(
                        x=with(density){mainCellOffset.x.toDp()} + mainCellSize.width - thumbState.offsetNegate.width,
                        y=with(density){mainCellOffset.y.toDp()} + mainCellSize.height - thumbState.offsetNegate.height,
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
        if (layout != null && layout.isAttached) {
            //x: draw boxes over selected/copied/referred cells
            Canvas(modifier = Modifier.fillMaxSize()) {
                // x: draw boxes around referred range
                val visibleRefRangeAndColor: Map<RangeAddress?, Color> = refRangeAndColorMap.mapKeys { (range, _) ->
                    range.intersect(currentDisplayedRange)
                }
                for ((rangeAddress, color) in visibleRefRangeAndColor) {
                    rangeAddress?.also {
                        val topLeftCoor = cellLayoutCoorsMap[rangeAddress.topLeft]
                        val botRightCoor = cellLayoutCoorsMap[rangeAddress.botRight]
                        if (topLeftCoor != null && botRightCoor != null) {
                            if (topLeftCoor.isAttached && botRightCoor.isAttached) {
                                val offset = layout.windowToLocal(topLeftCoor.posInWindowOrZero)

                                val size = if(rangeAddress.isCell()){
                                    topLeftCoor.dbSizeOrZero(density).toSize()
                                }else{
                                    val botRightOffset = layout.windowToLocal(botRightCoor.posInWindowOrZero)
                                    Size(
                                        width = botRightOffset.x - offset.x + botRightCoor.pixelSizeOrZero.width,
                                        height = botRightOffset.y - offset.y+botRightCoor.pixelSizeOrZero.height)
                                }

                                // x: dash line
                                drawRect(
                                    color = color,
                                    topLeft = offset,
                                    size = size,
                                    style = P6Theme.canvas.dashLine
                                )
                                // x: filled rect
                                drawRect(
                                    color = color.copy(alpha=0.3f),
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
                            if (layout.isAttached) {
                                val offset: Offset = layout.windowToLocal(cellLayout.posInWindowOrZero)
                                drawRect(
                                    color = Color.Blue.copy(alpha = 0.2F),
                                    topLeft = offset,
                                    size = cellLayout.dbSizeOrZero(density).toSize(),
                                )
                            }
                        }
                    }

                    // x: draw copied range
                    if (state.containInClipboard(cellAddress)) {
                        if (layout.isAttached) {
                            val offset = layout.windowToLocal(cellLayout.posInWindowOrZero)
                            drawRect(
                                color = Color.Magenta,
                                topLeft = offset,
                                size = cellLayout.dbSizeOrZero(density).toSize(),
                                style = P6Theme.canvas.dashLine
                            )
                        }
                    }
                }
                val thumbState = state.thumbState
                if(thumbState.isShowingSelectedRange){
                    if(layout.isAttached){
                        drawRect(
                            color = Color.Red.copy(alpha =0.4F),
                            topLeft = layout.windowToLocal(thumbState.selectedRangeWindowOffsetOrZero),
                            size = thumbState.selectedRangeSizeOrZero,
                        )
                    }
                }
            }
        }
    }
}

