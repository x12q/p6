package com.qxdzbc.p6.app.action.cell_editor.open_cell_editor

import com.qxdzbc.p6.app.action.common_data_structure.WbWs

/**
 * Open cell editor
 */
fun interface OpenCellEditorAction {
    /**
     * Open cell editor on a worksheet identified with [wbws]
     */
    fun openCellEditor(wbws: WbWs)
}
