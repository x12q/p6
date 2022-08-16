package com.emeraldblast.p6.di

import com.emeraldblast.p6.ui.action_table.*
import dagger.Binds

/**
 * Provide action table objects
 */
@dagger.Module
interface ActionTableModule {
    @Binds
    @com.emeraldblast.p6.di.P6Singleton
    fun CodeEditorActionTable(i: CodeEditorActionTableImp): CodeEditorActionTable

    @Binds
    @com.emeraldblast.p6.di.P6Singleton
    fun AppActionTable(i: AppActionTableImp): AppActionTable

    @Binds
    @com.emeraldblast.p6.di.P6Singleton
    fun windowActionTable(i: WindowActionTableImp): WindowActionTable

    @Binds
    @com.emeraldblast.p6.di.P6Singleton
    fun workbookActionTable(i: WorkbookActionTableImp): WorkbookActionTable

    @Binds
    @com.emeraldblast.p6.di.P6Singleton
    fun worksheetActionTable(i: WorksheetActionTableImp): WorksheetActionTable

}
