package com.qxdzbc.p6.ui.document.worksheet.select_whole_row_for_selected_cells

import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt

/**
 * Select all rows of the currently selected cells
 */
interface SelectWholeRowForAllSelectedCellAction {
    fun selectWholeRowForAllSelectedCells(wbwsSt: WbWsSt)
    fun selectWholeRowForAllSelectedCells(wbws: WbWs)
}

