package com.qxdzbc.p6.ui.document.cell.state

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.ui.document.cell.state.format.text.CellFormat

object CellStates {
    fun blank(cellAddress: CellAddress):CellState{
        return CellStateImp(
            address =cellAddress,
            cellMs =null,
        )
    }
    @OptIn(ExperimentalUnitApi::class)
    val defaultTextStyle = TextStyle(
        fontSize = TextUnit(CellFormat.defaultFontSize, CellFormat.textSizeUnitType),
    )
}
