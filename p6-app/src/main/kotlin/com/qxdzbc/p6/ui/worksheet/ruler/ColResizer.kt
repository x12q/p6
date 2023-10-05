package com.qxdzbc.p6.ui.worksheet.ruler

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import com.qxdzbc.common.compose.density_converter.FloatToDpConverter
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.p6.build.DebugFunctions.debug
import com.qxdzbc.p6.ui.theme.P6Theme
import com.qxdzbc.p6.ui.worksheet.WorksheetConstants
import com.qxdzbc.p6.ui.worksheet.ruler.actions.RulerAction


/**
 * A resizer is an area on a ruler item where user can click and drag to resize the ruler item
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ColResizer (
    state: RulerState,
    rulerAction: RulerAction,
    itemIndex:Int,
    modifier: Modifier,
){
    val density = LocalDensity.current
    MBox(modifier = modifier
        .onGloballyPositioned {
            rulerAction.updateResizerLayout(itemIndex, it,state)
        }
        .width(WorksheetConstants.resizerThickness)
        .fillMaxHeight()
        .background(Color.Magenta.debug())
        .pointerHoverIcon(P6Theme.icons.rightResize)
        .onPointerEvent(PointerEventType.Enter) {
            rulerAction.showColResizeBarThumb(itemIndex,state)
        }
        .onPointerEvent(PointerEventType.Exit) {
            rulerAction.hideColResizeBarThumb(state)
        }
        .onPointerEvent(PointerEventType.Press) { pte ->
            val itemLayout = state.getResizerLayout(itemIndex)
            if (itemLayout != null && itemLayout.isAttached) {
                val mousePos = pte.changes.first().position
                val resizerPos = itemLayout.localToWindow(mousePos)
                rulerAction.startColResizing(resizerPos,state)
            }
        }
        .onPointerEvent(PointerEventType.Move) { pte ->
            val change = pte.changes.first()
            val mousePos = change.position
            val itemLayout = state.getResizerLayout(itemIndex)
            if (itemLayout != null && itemLayout.isAttached) {
                val resizerPos = itemLayout.localToWindow(mousePos)
                rulerAction.moveColResizer(resizerPos,state)
            }
        }
        .onPointerEvent(PointerEventType.Release) {
            rulerAction.finishColResizing(itemIndex,state, FloatToDpConverter(density))
        }
    )
}