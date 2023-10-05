package com.qxdzbc.p6.ui.worksheet.ruler

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.qxdzbc.p6.ui.worksheet.ruler.actions.RulerAction

@Composable
fun RowRulerView(
    state: RulerState,
    rulerAction: RulerAction,
    size: Dp,
    modifier: Modifier = Modifier,
) {
    RulerView(
        state = state,
        rulerAction = rulerAction,
        size = size,
        modifier = modifier
    )
}