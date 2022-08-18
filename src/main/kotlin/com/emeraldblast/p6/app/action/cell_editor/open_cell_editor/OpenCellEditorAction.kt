package com.emeraldblast.p6.app.action.cell_editor.open_cell_editor

import com.emeraldblast.p6.app.action.common_data_structure.WbWs

fun interface OpenCellEditorAction {
    fun openCellEditor(wsId: WbWs)
}
