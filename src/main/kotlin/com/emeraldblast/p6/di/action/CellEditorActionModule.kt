package com.emeraldblast.p6.di.action

import com.emeraldblast.p6.app.action.cell_editor.open_cell_editor.OpenCellEditorAction
import com.emeraldblast.p6.app.action.cell_editor.open_cell_editor.OpenCellEditorImp
import com.emeraldblast.p6.di.P6Singleton
import dagger.Binds

@dagger.Module
interface CellEditorActionModule {
    @Binds
    @P6Singleton
    fun OpenCellEditor(i:OpenCellEditorImp): OpenCellEditorAction
}
