package com.qxdzbc.p6.di

import com.qxdzbc.p6.ui.app.action.AppActionTable
import com.qxdzbc.p6.ui.app.action.AppActionTableImp
import com.qxdzbc.p6.ui.document.workbook.action.WorkbookActionTable
import com.qxdzbc.p6.ui.document.workbook.action.WorkbookActionTableImp
import com.qxdzbc.p6.ui.document.worksheet.action.WorksheetActionTable
import com.qxdzbc.p6.ui.document.worksheet.action.WorksheetActionTableImp
import com.qxdzbc.p6.ui.script_editor.action.CodeEditorActionTable
import com.qxdzbc.p6.ui.script_editor.action.CodeEditorActionTableImp
import com.qxdzbc.p6.ui.window.action.WindowActionTable
import com.qxdzbc.p6.ui.window.action.WindowActionTableImp
import dagger.Binds

/**
 * Provide action table objects
 */
@dagger.Module
interface ActionTableModule {
    @Binds
    @com.qxdzbc.p6.di.P6Singleton
    fun CodeEditorActionTable(i: CodeEditorActionTableImp): CodeEditorActionTable

    @Binds
    @com.qxdzbc.p6.di.P6Singleton
    fun AppActionTable(i: AppActionTableImp): AppActionTable

    @Binds
    @com.qxdzbc.p6.di.P6Singleton
    fun windowActionTable(i: WindowActionTableImp): WindowActionTable

    @Binds
    @com.qxdzbc.p6.di.P6Singleton
    fun workbookActionTable(i: WorkbookActionTableImp): WorkbookActionTable

    @Binds
    @com.qxdzbc.p6.di.P6Singleton
    fun worksheetActionTable(i: WorksheetActionTableImp): WorksheetActionTable

}
