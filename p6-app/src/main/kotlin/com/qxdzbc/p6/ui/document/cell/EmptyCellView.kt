package com.qxdzbc.p6.ui.document.cell

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.ui.format2.CellFormatTable2

@Composable
fun EmptyCellView(
    cellAddress: CellAddress,
    formatTable:CellFormatTable2,
    boxModifier: Modifier = Modifier,
) {
    val color = Color.Transparent
    MBox(
        modifier = boxModifier
            .fillMaxSize()
            .let { mod ->
                formatTable.getCellModifier(cellAddress)?.let {
                    mod.then(it)
                } ?: mod
            }
    )
}
