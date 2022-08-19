package com.emeraldblast.p6.di.action

import com.emeraldblast.p6.app.action.worksheet.WorksheetAction
import com.emeraldblast.p6.app.action.worksheet.WorksheetActionImp
import com.emeraldblast.p6.app.action.worksheet.WorksheetApplier
import com.emeraldblast.p6.app.action.worksheet.WorksheetApplierImp
import com.emeraldblast.p6.app.action.worksheet.action2.MouseOnWorksheetAction
import com.emeraldblast.p6.app.action.worksheet.action2.MouseOnWorksheetActionImp
import com.emeraldblast.p6.app.action.worksheet.action2.WorksheetAction2
import com.emeraldblast.p6.app.action.worksheet.action2.WorksheetAction2Imp
import com.emeraldblast.p6.app.action.worksheet.click_on_cell.ClickOnCell
import com.emeraldblast.p6.app.action.worksheet.click_on_cell.ClickOnCellImp
import com.emeraldblast.p6.app.action.worksheet.delete_multi.DeleteMultiAction
import com.emeraldblast.p6.app.action.worksheet.delete_multi.DeleteMultiActionImp
import com.emeraldblast.p6.app.action.worksheet.delete_multi.applier.DeleteMultiApplier
import com.emeraldblast.p6.app.action.worksheet.delete_multi.applier.DeleteMultiApplierImp
import com.emeraldblast.p6.app.action.worksheet.release_focus.*
import com.emeraldblast.p6.di.P6Singleton
import com.emeraldblast.p6.ui.document.worksheet.cursor.actions.CursorAction
import com.emeraldblast.p6.ui.document.worksheet.cursor.actions.CursorActionImp
import com.emeraldblast.p6.ui.app.cell_editor.in_cell.actions.CellEditorAction
import com.emeraldblast.p6.ui.app.cell_editor.in_cell.actions.CellEditorActionImp
import com.emeraldblast.p6.app.action.worksheet.make_cell_editor_display_text.MakeCellEditorDisplayText
import com.emeraldblast.p6.app.action.worksheet.make_cell_editor_display_text.MakeCellEditorDisplayTextImp
import com.emeraldblast.p6.ui.document.worksheet.ruler.actions.RulerAction
import com.emeraldblast.p6.ui.document.worksheet.ruler.actions.RulerActionImp
import dagger.Binds

@dagger.Module
interface WorksheetActionModule {
    @Binds
    @P6Singleton
    fun MouseOnWorksheetAction(i: MouseOnWorksheetActionImp): MouseOnWorksheetAction

    @Binds
    @P6Singleton
    fun ClickOnCell(i: ClickOnCellImp): ClickOnCell

    @Binds
    @P6Singleton
    fun MakeCellEditorDisplayText(i: MakeCellEditorDisplayTextImp): MakeCellEditorDisplayText

    @Binds
    @P6Singleton
    fun ReleaseWsFocusAction(i: RestoreWindowFocusStateImp): RestoreWindowFocusState

    @Binds
    @P6Singleton
    fun bindWorksheetEventHandler(i: WorksheetApplierImp): WorksheetApplier

    @Binds
    @P6Singleton
    fun CursorAction(i: CursorActionImp): CursorAction

    @Binds
    @P6Singleton
    fun WorksheetAction2(i: WorksheetAction2Imp): WorksheetAction2

    @Binds
    @P6Singleton
    fun WorksheetAction(i: WorksheetActionImp): WorksheetAction

    @Binds
    @P6Singleton
    fun DeleteMultiAction(i: DeleteMultiActionImp): DeleteMultiAction

    @Binds
    @P6Singleton
    fun DeleteMultiApplier(i: DeleteMultiApplierImp): DeleteMultiApplier

    @Binds
    @P6Singleton
    fun CellEditorAction(i: CellEditorActionImp): CellEditorAction

    @Binds
    @P6Singleton
    fun RulerAction(i: RulerActionImp): RulerAction
}
