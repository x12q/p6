package com.qxdzbc.p6.app.action.cell_editor.open_cell_editor

import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt

/**
 * Open cell editor
 */
interface OpenCellEditorAction {
    /**
     * Open cell editor on a worksheet identified with [wbws], at the current cursor position/cell.
     */
    fun openCellEditor(wbws: WbWs)
    fun openCellEditor(wbwsSt:WbWsSt)
}
