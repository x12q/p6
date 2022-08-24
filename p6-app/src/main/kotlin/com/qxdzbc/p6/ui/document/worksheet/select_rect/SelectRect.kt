package com.qxdzbc.p6.ui.document.worksheet.select_rect

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.qxdzbc.p6.ui.common.R
import com.qxdzbc.common.compose.view.MBox

@Composable
fun SelectRect(
    state: SelectRectState,
    position:Offset
) {
    val selectionRect = state.rect
    if (state.isShow) {
        MBox(
            modifier = Modifier
                .size(
                    height = selectionRect.height.dp,
                    width = selectionRect.width.dp
                )
                .offset(x = position.x.dp, y = position.y.dp)
                .border(3.dp, R.color.dBlack)
        )
    }
}
