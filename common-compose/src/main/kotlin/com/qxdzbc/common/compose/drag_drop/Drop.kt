package com.qxdzbc.common.compose.drag_drop

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import com.qxdzbc.common.compose.LayoutCoorsUtils.wrap
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.view.MBox

/**
 * TODO use local provider to pass the internal state implicitly to this function
 * Offset modifier must be set on the Drop composable. Don't set offset in its children composables.
 */
@Composable
fun Drop(
    internalStateMs: Ms<DragAndDropHostInternalState>,
    identifier:()->Any,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    MBox(
        modifier = modifier
            .onGloballyPositioned {
                internalStateMs.value = internalStateMs.value.addDropLayoutCoorWrapper(identifier(),it.wrap())

            }
    ) {
        content()
    }
}
