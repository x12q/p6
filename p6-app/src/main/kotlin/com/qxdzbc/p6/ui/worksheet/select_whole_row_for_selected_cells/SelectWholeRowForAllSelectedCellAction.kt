package com.qxdzbc.p6.ui.worksheet.select_whole_row_for_selected_cells

import com.qxdzbc.p6.composite_actions.common_data_structure.WbWs
import com.qxdzbc.p6.composite_actions.common_data_structure.WbWsSt

/**
 * Select all rows of the currently selected cells
 */
interface SelectWholeRowForAllSelectedCellAction {
    fun selectWholeRowForAllSelectedCells(wbwsSt: WbWsSt)
    fun selectWholeRowForAllSelectedCells(wbws: WbWs)
}

