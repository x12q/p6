package com.qxdzbc.common.compose.drag_drop

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import com.qxdzbc.common.compose.LayoutCoorsUtils.wrap
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.view.MBox

/**
 * TODO use local provider to pass the internal state implicitly to this function
 */

@Composable
fun Drop(
    internalStateMs: Ms<DragAndDropHostState>,
    identifier:()->Any,
    content: @Composable () -> Unit,
) {
    MBox(
        modifier = Modifier
            .onGloballyPositioned {
                internalStateMs.value = internalStateMs.value.setDropLayoutCoorWrapper(identifier(),it.wrap())
            }
    ) {
        content()
    }
}
