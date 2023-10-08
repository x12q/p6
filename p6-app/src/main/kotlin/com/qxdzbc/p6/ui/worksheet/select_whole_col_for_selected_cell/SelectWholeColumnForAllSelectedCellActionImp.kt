package com.qxdzbc.p6.ui.worksheet.select_whole_col_for_selected_cell

import androidx.compose.runtime.getValue
import com.qxdzbc.p6.composite_actions.common_data_structure.WbWs
import com.qxdzbc.p6.composite_actions.common_data_structure.WbWsSt
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.document_data_layer.range.address.RangeAddress
import com.qxdzbc.p6.document_data_layer.range.address.RangeAddressUtils
import com.qxdzbc.p6.di.P6AnvilScope

import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.worksheet.state.WorksheetState
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class)
class SelectWholeColumnForAllSelectedCellActionImp @Inject constructor(
    private val stateCont:StateContainer,
) : SelectWholeColumnForAllSelectedCellAction {

    private val sc  = stateCont

    fun selectWholeColForAllSelectedCells(wsState:WorksheetState?) {
        wsState?.also {
            val cursorStateMs = wsState.cursorStateMs
            val cursorState by cursorStateMs
            val selectCols: List<Int> = cursorState.colFromFragCells
            val colFromRange: List<IntRange> = cursorState.colFromRange
            var newCursorState: CursorState = cursorState
            newCursorState = newCursorState.addFragRanges(
                selectCols.map { col ->
                    RangeAddressUtils.rangeForWholeCol(col)
                }
            ).addFragRanges(
                colFromRange.map { colRange ->
                    RangeAddress(
                        CellAddress(colRange.first, wsState.firstRow),
                        CellAddress(colRange.last, wsState.lastRow)
                    )
                }
            )
            cursorStateMs.value = newCursorState
        }
    }
    override fun selectWholeColForAllSelectedCells(wbws: WbWsSt) {
        selectWholeColForAllSelectedCells(sc.getWsState(wbws))
    }

    override fun selectWholeColForAllSelectedCells(wbws: WbWs) {
        selectWholeColForAllSelectedCells(sc.getWsState(wbws))
    }
}
