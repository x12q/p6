package com.emeraldblast.p6.ui.document.cell.state

import com.emeraldblast.p6.app.document.cell.address.CellAddress
import com.emeraldblast.p6.app.document.cell.d.Cells
import com.emeraldblast.p6.ui.common.compose.ms
import com.emeraldblast.p6.ui.document.cell.state.format.TextFormat

object CellStates {
    fun blank(cellAddress: CellAddress):CellState{
        return CellStateImp(
            address =cellAddress,
            cellMs =ms(Cells.empty(cellAddress)),
            textFormatMs = ms(TextFormat.default)
        )
    }
}
