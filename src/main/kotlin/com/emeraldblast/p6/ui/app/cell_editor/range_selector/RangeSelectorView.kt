package com.emeraldblast.p6.ui.app.cell_editor.range_selector

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
import com.emeraldblast.p6.app.document.cell.address.CellAddress
import com.emeraldblast.p6.ui.common.compose.LayoutCoorWrapper
import com.emeraldblast.p6.ui.common.compose.rms
import com.emeraldblast.p6.ui.common.compose.toIntOffset
import com.emeraldblast.p6.ui.common.view.MBox

@Composable
fun RangeSelectorView(
    state: RangeSelectorState,
    action: RangeSelectorAction,
    isFocus:Boolean,
    cellLayoutCoorsMap: Map<CellAddress, LayoutCoorWrapper>,
) {
    var boundLayoutCoors: LayoutCoordinates? by rms(null)
    val fc = remember { FocusRequester() }
    MBox(modifier = Modifier
        .focusRequester(fc)
        .focusable(true)
        .onPreviewKeyEvent { keyEvent ->
            action.handleKeyboardEvent(keyEvent, state)
        }
        .fillMaxSize().onGloballyPositioned {
        boundLayoutCoors = it
    }) {
        for ((cellAddress, cellLayout) in cellLayoutCoorsMap) {
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
    }
    DisposableEffect(isFocus){
        if(isFocus){
            fc.requestFocus()
        }
        onDispose {  }
    }
}
