package com.qxdzbc.p6.ui.document.cell

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.ui.document.cell.state.format.text.CellFormat
import com.qxdzbc.p6.ui.format2.CellFormatTable

@Composable
fun EmptyCellView(
    format:CellFormat,
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
