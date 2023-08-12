package com.qxdzbc.p6.ui.worksheet.select_rect

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.p6.ui.common.common_objects.DebugColors

/**
 * A rectangular drawn
 */
@Composable
fun SelectRect(
    state: SelectRectState,
    position:Offset
) {
    val selectionRect = state.rect
    val density = LocalDensity.current
    if (state.isShow) {
        MBox(
            modifier = Modifier
                .size(
                    height = with(density){
                        selectionRect.height.toDp()
                    },
                    width=with(density){
                        selectionRect.width.toDp()
                    }
                )
                .offset(
                    x= with(density){
                        position.x.toDp()
                    },
                    y=with(density){
                        position.y.toDp()
                    }
                )
                .border(3.dp, DebugColors.dBlack)
        )
    }
}
