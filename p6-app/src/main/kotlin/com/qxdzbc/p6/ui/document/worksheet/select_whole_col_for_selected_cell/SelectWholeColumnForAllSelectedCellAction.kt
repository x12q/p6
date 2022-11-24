package com.qxdzbc.p6.ui.document.worksheet.select_whole_col_for_selected_cell

import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt

/**
 * Select all columns of the currently selected cells
 */
interface SelectWholeColumnForAllSelectedCellAction {
    fun selectWholeColForAllSelectedCells(wbws: WbWsSt)
    fun selectWholeColForAllSelectedCells(wbws: WbWs)
}

