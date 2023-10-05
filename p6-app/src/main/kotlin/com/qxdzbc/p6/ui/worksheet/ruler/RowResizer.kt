package com.qxdzbc.p6.ui.worksheet.ruler

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import com.qxdzbc.common.compose.PointerEventUtils.executeOnReleaseThenConsumed
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
fun RowResizer(
    state: RulerState,
    rulerAction: RulerAction,
    itemIndex:Int,
    modifier: Modifier,
) {
    val density = LocalDensity.current
    MBox(modifier = modifier
        .onGloballyPositioned {
            rulerAction.updateResizerLayout(itemIndex, it,state)
        }
        .height(WorksheetConstants.resizerThickness)
        .fillMaxWidth()
        .background(Color.Magenta.debug())
        .pointerHoverIcon(P6Theme.icons.downResize)
        .onPointerEvent(PointerEventType.Enter) {
            rulerAction.showRowResizeBarThumb(itemIndex,state)
        }
        .onPointerEvent(PointerEventType.Exit) {
            rulerAction.hideRowResizeBarThumb(state)
        }
        .onPointerEvent(PointerEventType.Press) { pte ->
            val itemlayout = state.getResizerLayout(itemIndex)
            if (itemlayout != null && itemlayout.isAttached) {
                val mousePos = pte.changes.first().position
                rulerAction.startRowResizing(itemlayout.localToWindow(mousePos),state)
            }
        }
        .onPointerEvent(PointerEventType.Move) { pte ->
            if (pte.changes.first().pressed) {
                val itemlayout = state.getResizerLayout(itemIndex)
                if (itemlayout != null && itemlayout.isAttached) {
                    val change = pte.changes.first()
                    val mousePos = change.position
                    rulerAction.moveRowResizer(
                        itemlayout.localToWindow(mousePos),state
                    )
                }
            }
        }
        .onPointerEvent(PointerEventType.Release) {
            it.executeOnReleaseThenConsumed {
                rulerAction.finishRowResizing(itemIndex,state, FloatToDpConverter(density))
            }
        }
    )

}