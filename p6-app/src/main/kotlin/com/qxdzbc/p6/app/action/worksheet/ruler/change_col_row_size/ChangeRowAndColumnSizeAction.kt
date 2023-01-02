package com.qxdzbc.p6.app.action.worksheet.ruler.change_col_row_size

import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt

interface ChangeRowAndColumnSizeAction{
    fun changeColWidth(colIndex: Int, sizeDiff: Int, wbwsSt: WbWsSt,undoable:Boolean)
    fun changeRowHeight(rowIndex: Int, sizeDiff: Int, wbwsSt: WbWsSt,undoable:Boolean)
}
