package com.qxdzbc.p6.ui.document.worksheet.select_whole_row_for_selected_cells

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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
class SelectWholeRowForAllSelectedCellActionImp @Inject constructor(
    private val stateContSt: St<@JvmSuppressWildcards StateContainer>,
) : SelectWholeRowForAllSelectedCellAction {

    private val sc by stateContSt

    override fun selectWholeRowForAllSelectedCells(wbws: WbWs) {
        val wsStateMs = sc.getWsStateMs(wbws)
        if (wsStateMs != null) {
            val wsState by wsStateMs
            val cursorStateMs = wsStateMs.value.cursorStateMs
            var cursorState: CursorState by cursorStateMs
            val selectRows: List<Int> = cursorState.allFragCells.map { it.rowIndex }
            val rowFromRange: List<IntRange> = cursorState.allRanges.map {
                it.rowRange
            }
            var newCursor = cursorState

            newCursor = newCursor.addFragRanges(
                selectRows.map { row ->
                    RangeAddresses.wholeRow(row)
                }
            ).addFragRanges(
                rowFromRange.map { rowRange ->
                    RangeAddress(
                        CellAddress(wsState.firstCol, rowRange.first),
                        CellAddress(wsState.lastCol, rowRange.last)
                    )
                }
            )
            cursorState = newCursor
        }
    }

}
