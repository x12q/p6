package com.qxdzbc.p6.di.action

import com.qxdzbc.p6.app.action.worksheet.WorksheetAction
import com.qxdzbc.p6.app.action.worksheet.WorksheetActionImp
import com.qxdzbc.p6.app.action.worksheet.compute_slider_size.ComputeSliderSizeAction
import com.qxdzbc.p6.app.action.worksheet.compute_slider_size.ComputeSliderSizeActionImp
import com.qxdzbc.p6.app.action.worksheet.action2.WorksheetAction2
import com.qxdzbc.p6.app.action.worksheet.action2.WorksheetAction2Imp
import com.qxdzbc.p6.app.action.worksheet.convert_proto_to_ws.ConvertProtoToWorksheet
import com.qxdzbc.p6.app.action.worksheet.convert_proto_to_ws.ConvertProtoToWorksheetImp
import com.qxdzbc.p6.app.action.worksheet.delete_multi.DeleteMultiCellAction
import com.qxdzbc.p6.app.action.worksheet.delete_multi.DeleteMultiCellActionImp
import com.qxdzbc.p6.app.action.worksheet.delete_multi.applier.DeleteMultiApplier
import com.qxdzbc.p6.app.action.worksheet.delete_multi.applier.DeleteMultiApplierImp
import com.qxdzbc.p6.app.action.worksheet.load_data.LoadDataAction
import com.qxdzbc.p6.app.action.worksheet.load_data.LoadDataActionImp
import com.qxdzbc.p6.app.action.worksheet.make_cell_editor_display_text.MakeCellEditorDisplayTextAction
import com.qxdzbc.p6.app.action.worksheet.make_cell_editor_display_text.MakeCellEditorDisplayTextActionImp
import com.qxdzbc.p6.app.action.worksheet.make_slider_follow_cell.MakeSliderFollowCellAction
import com.qxdzbc.p6.app.action.worksheet.make_slider_follow_cell.MakeSliderFollowCellActionImp
import com.qxdzbc.p6.app.action.worksheet.mouse_on_ws.MouseOnWorksheetAction
import com.qxdzbc.p6.app.action.worksheet.mouse_on_ws.MouseOnWorksheetActionImp
import com.qxdzbc.p6.app.action.worksheet.mouse_on_ws.click_on_cell.ClickOnCell
import com.qxdzbc.p6.app.action.worksheet.mouse_on_ws.click_on_cell.ClickOnCellImp
import com.qxdzbc.p6.app.action.worksheet.paste_range.PasteRangeAction
import com.qxdzbc.p6.app.action.worksheet.paste_range.PasteRangeActionImp
import com.qxdzbc.p6.app.action.worksheet.release_focus.RestoreWindowFocusState
import com.qxdzbc.p6.app.action.worksheet.release_focus.RestoreWindowFocusStateImp
import com.qxdzbc.p6.app.action.worksheet.remove_all_cell.RemoveAllCellAction
import com.qxdzbc.p6.app.action.worksheet.remove_all_cell.RemoveAllCellActionImp
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.ui.app.cell_editor.actions.CellEditorAction
import com.qxdzbc.p6.ui.app.cell_editor.actions.CellEditorActionImp
import com.qxdzbc.p6.ui.document.worksheet.UpdateCellEditorTextWithRangeSelectorAction
import com.qxdzbc.p6.ui.document.worksheet.UpdateCellEditorTextWithRangeSelectorActionImp
import com.qxdzbc.p6.ui.document.worksheet.cursor.actions.CursorAction
import com.qxdzbc.p6.ui.document.worksheet.cursor.actions.CursorActionImp
import com.qxdzbc.p6.ui.document.worksheet.cursor.thumb.action.*
import com.qxdzbc.p6.ui.document.worksheet.ruler.actions.RulerAction
import com.qxdzbc.p6.ui.document.worksheet.ruler.actions.RulerActionImp
import com.qxdzbc.p6.ui.document.worksheet.select_whole_col_for_selected_cell.SelectWholeColumnForAllSelectedCellAction
import com.qxdzbc.p6.ui.document.worksheet.select_whole_col_for_selected_cell.SelectWholeColumnForAllSelectedCellActionImp
import com.qxdzbc.p6.ui.document.worksheet.select_whole_row_for_selected_cells.SelectWholeRowForAllSelectedCellAction
import com.qxdzbc.p6.ui.document.worksheet.select_whole_row_for_selected_cells.SelectWholeRowForAllSelectedCellActionImp
import dagger.Binds

@dagger.Module
interface WorksheetActionModule {

//    @Binds
//    @P6Singleton
//    fun EndThumbDragAction(i:EndThumbDragActionImp):EndThumbDragAction
//
//    @Binds
//    @P6Singleton
//    fun ThumbAction(i:ThumbActionImp):ThumbAction
//
//    @Binds
//    @P6Singleton
//    fun DragThumbAction(i:DragThumbActionImp):DragThumbAction
//
//    @Binds
//    @P6Singleton
//    fun MakeSliderFollowCellAction(i:MakeSliderFollowCellActionImp): MakeSliderFollowCellAction
//
//    @Binds
//    @P6Singleton
//    fun UpdateCellEditorTextWithRangeSelectorAction(i: UpdateCellEditorTextWithRangeSelectorActionImp):UpdateCellEditorTextWithRangeSelectorAction
//    @Binds
//    @P6Singleton
//    fun SelectWholeRowForAllSelectedCellAction(i: SelectWholeRowForAllSelectedCellActionImp):SelectWholeRowForAllSelectedCellAction
//
//    @Binds
//    @P6Singleton
//    fun SelectWholeColumnForAllSelectedCellAction(i: SelectWholeColumnForAllSelectedCellActionImp):SelectWholeColumnForAllSelectedCellAction
//
//    @Binds
//    @P6Singleton
//    fun ComputeSliderSizeAction(i: ComputeSliderSizeActionImp): ComputeSliderSizeAction
//
//    @Binds
//    @P6Singleton
//    fun RemoveAllCellAction(i: RemoveAllCellActionImp): RemoveAllCellAction
//
//    @Binds
//    @P6Singleton
//    fun LoadDataAction(i: LoadDataActionImp): LoadDataAction
//
//    @Binds
//    @P6Singleton
//    fun ConvertProtoToWorksheet(i: ConvertProtoToWorksheetImp): ConvertProtoToWorksheet
//
//    @Binds
//    @P6Singleton
//    fun PasteRangeAction(i: PasteRangeActionImp): PasteRangeAction
//
//    @Binds
//    @P6Singleton
//    fun MouseOnWorksheetAction(i: MouseOnWorksheetActionImp): MouseOnWorksheetAction
//
//    @Binds
//    @P6Singleton
//    fun ClickOnCell(i: ClickOnCellImp): ClickOnCell
//
//    @Binds
//    @P6Singleton
//    fun MakeCellEditorDisplayText(i: MakeCellEditorDisplayTextActionImp): MakeCellEditorDisplayTextAction
//
//    @Binds
//    @P6Singleton
//    fun ReleaseWsFocusAction(i: RestoreWindowFocusStateImp): RestoreWindowFocusState
//
//
//    @Binds
//    @P6Singleton
//    fun CursorAction(i: CursorActionImp): CursorAction
//
//    @Binds
//    @P6Singleton
//    fun WorksheetAction2(i: WorksheetAction2Imp): WorksheetAction2
//
//    @Binds
//    @P6Singleton
//    fun WorksheetAction(i: WorksheetActionImp): WorksheetAction
//
//    @Binds
//    @P6Singleton
//    fun DeleteMultiAction(i: DeleteMultiCellActionImp): DeleteMultiCellAction
//
//    @Binds
//    @P6Singleton
//    fun DeleteMultiApplier(i: DeleteMultiApplierImp): DeleteMultiApplier
//
//    @Binds
//    @P6Singleton
//    fun CellEditorAction(i: CellEditorActionImp): CellEditorAction
//
//    @Binds
//    @P6Singleton
//    fun RulerAction(i: RulerActionImp): RulerAction
}
