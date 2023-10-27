package com.qxdzbc.p6.ui.worksheet.select_whole_col_for_selected_cell

import com.qxdzbc.p6.composite_actions.common_data_structure.WbWs
import com.qxdzbc.p6.composite_actions.common_data_structure.WbWsSt

/**
 * Select all columns of the currently selected cells
 */
interface SelectWholeColumnForAllSelectedCellAction {
    fun selectWholeColForAllSelectedCells(wbwsSt: WbWsSt)
    fun selectWholeColForAllSelectedCells(wbws: WbWs)
}

