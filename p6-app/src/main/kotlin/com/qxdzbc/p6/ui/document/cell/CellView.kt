package com.qxdzbc.p6.ui.document.cell

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.d.*
import com.qxdzbc.p6.ui.common.R
import com.qxdzbc.p6.ui.common.compose.StateUtils.toMs
import com.qxdzbc.p6.ui.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.common.compose.TestApp
import com.qxdzbc.p6.ui.common.view.MBox
import com.qxdzbc.p6.ui.document.cell.state.CellState
import com.qxdzbc.p6.ui.document.cell.state.CellStateImp
import com.qxdzbc.p6.ui.document.cell.state.format.TextFormat
import com.qxdzbc.p6.ui.document.cell.state.format.TextHorizontalAlignment
import com.qxdzbc.p6.ui.document.cell.state.format.TextVerticalAlignment
import com.qxdzbc.p6.ui.document.cell.state.format.cell.CellFormat

@Composable
fun CellView(
    state: CellState,
    boxModifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
) {
    val color = state.backgroundColor
    val cell: Cell? = state.cell
    MBox(
        modifier = boxModifier
            .fillMaxSize()
            .background(color)
    ) {
        if (cell != null) {
            Text(
                cell.content.displayValue,
                modifier = textModifier
                    .align(state.alignment),
                style = state.textStyle
            )
        }
    }
}


@Composable
fun EmptyCellView(
    boxModifier: Modifier = Modifier,
) {
    val color = Color.Transparent
    MBox(
        modifier = boxModifier
            .fillMaxSize()
            .background(color)
    ) {
    }
}


fun main() = TestApp {
    val address = CellAddress(1, 1)
    Column {
        Box(modifier = Modifier.size(R.size.value.defaultCellSize).border(1.dp, Color.Black)) {
            CellView(
                CellStateImp(
                    address = address,
                    cellMs = CellImp(address, CellContentImp(cellValueMs = CellValue.from("text abc").toMs())).toMs(),
                    textFormatMs = ms(
                        TextFormat(
                            color = Color.Red,
                            verticalAlignment = TextVerticalAlignment.Center,
                            horizontalAlignment = TextHorizontalAlignment.Center,
                            isCrossed = true,
                            isUnderlined = true,
                            fontWeight = FontWeight.Bold
                        )
                    ),
                    cellFormatMs = ms(
                        CellFormat(
                            backgroundColor = Color.Blue
                        )
                    )
                )
            )
        }
        Box(modifier = Modifier.size(R.size.value.defaultCellSize).border(1.dp, Color.Black)) {
            CellView(
                CellStateImp(
                    address = address,
                    cellMs = CellImp(address, CellContentImp(cellValueMs = CellValue.from("text abc").toMs())).toMs()
                )
            )
        }
    }
}