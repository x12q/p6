package com.qxdzbc.p6.ui.document.worksheet.select_whole_col_for_selected_cell

import androidx.compose.runtime.getValue
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddresses
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope

import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class SelectWholeColumnForAllSelectedCellActionImp @Inject constructor(
    private val stateContSt: St<@JvmSuppressWildcards StateContainer>,
) : SelectWholeColumnForAllSelectedCellAction {

    private val sc by stateContSt

    override fun selectWholeColForAllSelectedCells(wbws: WbWs) {
        val wsStateMs = sc.getWsStateMs(wbws)
        if (wsStateMs != null) {
            val wsState by wsStateMs
            val cursorStateMs = wsStateMs.value.cursorStateMs
            val cursorState by cursorStateMs
            val selectCols: List<Int> = cursorState.colFromFragCells
            val colFromRange: List<IntRange> = cursorState.colFromRange
            var newCursorState: CursorState = cursorState
            newCursorState = newCursorState.addFragRanges(
                selectCols.map { col ->
                    RangeAddresses.wholeCol(col)
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

}
