package com.qxdzbc.p6.ui.document.cell.state

import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.d.Cells
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.document.cell.state.format.TextFormat

object CellStates {
    fun blank(cellAddress: CellAddress):CellState{
        return CellStateImp(
            address =cellAddress,
            cellMs =ms(Cells.empty(cellAddress)),
            textFormatMs = ms(TextFormat.default)
        )
    }
}
