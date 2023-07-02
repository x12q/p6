package com.qxdzbc.p6.ui.document.worksheet.select_whole_row_for_selected_cells

import androidx.compose.runtime.getValue
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
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
class SelectWholeRowForAllSelectedCellActionImp @Inject constructor(
    private val stateCont:StateContainer,
) : SelectWholeRowForAllSelectedCellAction {

    private val sc  = stateCont

     fun selectWholeRowForAllSelectedCells(wsStateMs:Ms<WorksheetState>?) {
        wsStateMs?.also {
            val wsState by wsStateMs
            val cursorStateMs = wsStateMs.value.cursorStateMs
            val cursorState: CursorState by cursorStateMs
            val selectRows: List<Int> = cursorState.allFragCells.map { it.rowIndex }
            val rowFromRange: List<IntRange> = cursorState.allRanges.map {
                it.rowRange
            }
            var newCursor = cursorState

            newCursor = newCursor.addFragRanges(
                selectRows.map { row ->
                    RangeAddressUtils.rangeForWholeRow(row)
                }
            ).addFragRanges(
                rowFromRange.map { rowRange ->
                    RangeAddress(
                        CellAddress(wsState.firstCol, rowRange.first),
                        CellAddress(wsState.lastCol, rowRange.last)
                    )
                }
            )
            cursorStateMs.value = newCursor
        }
     }
    override fun selectWholeRowForAllSelectedCells(wbwsSt: WbWsSt) {
        selectWholeRowForAllSelectedCells(sc.getWsStateMs(wbwsSt))
    }

    override fun selectWholeRowForAllSelectedCells(wbws: WbWs) {
        selectWholeRowForAllSelectedCells(sc.getWsStateMs(wbws))
    }

}
