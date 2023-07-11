package com.qxdzbc.p6.ui.document.worksheet.select_whole_col_for_selected_cell

import androidx.compose.runtime.getValue
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddressUtils
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope

import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetState
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
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
