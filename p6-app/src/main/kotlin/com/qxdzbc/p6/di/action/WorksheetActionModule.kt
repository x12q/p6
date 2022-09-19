package com.qxdzbc.p6.di.action

import com.qxdzbc.p6.app.action.worksheet.WorksheetAction
import com.qxdzbc.p6.app.action.worksheet.WorksheetActionImp
import com.qxdzbc.p6.app.action.worksheet.mouse_on_ws.MouseOnWorksheetAction
import com.qxdzbc.p6.app.action.worksheet.mouse_on_ws.MouseOnWorksheetActionImp
import com.qxdzbc.p6.app.action.worksheet.action2.WorksheetAction2
import com.qxdzbc.p6.app.action.worksheet.action2.WorksheetAction2Imp
import com.qxdzbc.p6.app.action.worksheet.convert_proto_to_ws.ConvertProtoToWorksheet
import com.qxdzbc.p6.app.action.worksheet.convert_proto_to_ws.ConvertProtoToWorksheetImp
import com.qxdzbc.p6.app.action.worksheet.mouse_on_ws.click_on_cell.ClickOnCell
import com.qxdzbc.p6.app.action.worksheet.mouse_on_ws.click_on_cell.ClickOnCellImp
import com.qxdzbc.p6.app.action.worksheet.delete_multi.DeleteMultiCellAction
import com.qxdzbc.p6.app.action.worksheet.delete_multi.DeleteMultiCellActionImp
import com.qxdzbc.p6.app.action.worksheet.delete_multi.applier.DeleteMultiApplier
import com.qxdzbc.p6.app.action.worksheet.delete_multi.applier.DeleteMultiApplierImp
import com.qxdzbc.p6.app.action.worksheet.load_data.LoadDataAction
import com.qxdzbc.p6.app.action.worksheet.load_data.LoadDataActionImp
import com.qxdzbc.p6.app.action.worksheet.release_focus.*
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.ui.document.worksheet.cursor.actions.CursorAction
import com.qxdzbc.p6.ui.document.worksheet.cursor.actions.CursorActionImp
import com.qxdzbc.p6.ui.app.cell_editor.in_cell.actions.CellEditorAction
import com.qxdzbc.p6.ui.app.cell_editor.in_cell.actions.CellEditorActionImp
import com.qxdzbc.p6.app.action.worksheet.make_cell_editor_display_text.MakeCellEditorDisplayText
import com.qxdzbc.p6.app.action.worksheet.make_cell_editor_display_text.MakeCellEditorDisplayTextImp
import com.qxdzbc.p6.app.action.worksheet.paste_range.PasteRangeAction
import com.qxdzbc.p6.app.action.worksheet.paste_range.PasteRangeActionImp
import com.qxdzbc.p6.ui.document.worksheet.ruler.actions.RulerAction
import com.qxdzbc.p6.ui.document.worksheet.ruler.actions.RulerActionImp
import dagger.Binds

@dagger.Module
interface WorksheetActionModule {
    @Binds
    @P6Singleton
    fun LoadDataAction(i: LoadDataActionImp):LoadDataAction
    @Binds
    @P6Singleton
    fun ConvertProtoToWorksheet(i:ConvertProtoToWorksheetImp): ConvertProtoToWorksheet

    @Binds
    @P6Singleton
    fun PasteRangeAction(i: PasteRangeActionImp):PasteRangeAction

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
    fun CursorAction(i: CursorActionImp): CursorAction

    @Binds
    @P6Singleton
    fun WorksheetAction2(i: WorksheetAction2Imp): WorksheetAction2

    @Binds
    @P6Singleton
    fun WorksheetAction(i: WorksheetActionImp): WorksheetAction

    @Binds
    @P6Singleton
    fun DeleteMultiAction(i: DeleteMultiCellActionImp): DeleteMultiCellAction

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
