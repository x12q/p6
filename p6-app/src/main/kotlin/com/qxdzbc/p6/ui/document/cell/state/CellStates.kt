package com.qxdzbc.p6.ui.document.cell.state

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.Cells
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextFormat

object CellStates {
    fun blank(cellAddress: CellAddress):CellState{
        return CellStateImp(
            address =cellAddress,
            cellMs =null,
            textFormatMs = ms(TextFormat.default)
        )
    }
    @OptIn(ExperimentalUnitApi::class)
    val defaultTextStyle = TextStyle(
        fontSize = TextUnit(13f, TextUnitType.Sp),
    )
}
