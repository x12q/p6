package com.qxdzbc.p6.ui.worksheet.range_indicator

import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import com.qxdzbc.p6.ui.common.view.BorderBox
import com.qxdzbc.p6.ui.common.view.BorderStyle
import com.qxdzbc.p6.ui.worksheet.WorksheetConstants

@Composable
fun CellRangeIndicator(
    label:String
) {
    BorderBox( // x: cell/range indicator
        borderStyle = BorderStyle.BOT_RIGHT,
        modifier = Modifier.size(
            DpSize(
                WorksheetConstants.rowRulerWidth,
                WorksheetConstants.defaultRowHeight,
            )
        )
    ) {
        Text(
            text = label,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

