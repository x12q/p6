package com.qxdzbc.p6.ui.cell

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.p6.ui.cell.state.format.text.CellFormat

@Composable
fun EmptyCellView(
    format: CellFormat,
    boxModifier: Modifier = Modifier,
) {
    MBox(
        modifier = boxModifier
            .fillMaxSize()
            .let { mod ->
                format.cellModifier?.let {
                    mod.then(it)
                } ?: mod
            }
    )
}
