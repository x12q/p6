package test.di;

import androidx.compose.runtime.MutableState;
import androidx.compose.runtime.State;
import androidx.compose.ui.window.ApplicationScope;
import com.google.gson.Gson;
import com.qxdzbc.common.copiers.binary_copier.BinaryCopier;
import com.qxdzbc.common.file_util.FileUtil;
import com.qxdzbc.p6.app.action.app.AppRM;
import com.qxdzbc.p6.app.action.app.AppRMImp;
import com.qxdzbc.p6.app.action.app.AppRMImp_Factory;
import com.qxdzbc.p6.app.action.app.close_wb.CloseWorkbookActionImp;
import com.qxdzbc.p6.app.action.app.close_wb.CloseWorkbookActionImp_Factory;
import com.qxdzbc.p6.app.action.app.close_wb.applier.CloseWorkbookApplierImp;
import com.qxdzbc.p6.app.action.app.close_wb.applier.CloseWorkbookApplierImp_Factory;
import com.qxdzbc.p6.app.action.app.close_wb.applier.CloseWorkbookInternalApplierImp;
import com.qxdzbc.p6.app.action.app.close_wb.applier.CloseWorkbookInternalApplierImp_Factory;
import com.qxdzbc.p6.app.action.app.close_wb.rm.CloseWorkbookRMImp;
import com.qxdzbc.p6.app.action.app.close_wb.rm.CloseWorkbookRMImp_Factory;
import com.qxdzbc.p6.app.action.app.create_new_wb.CreateNewWorkbookAction;
import com.qxdzbc.p6.app.action.app.create_new_wb.CreateNewWorkbookActionImp;
import com.qxdzbc.p6.app.action.app.create_new_wb.CreateNewWorkbookActionImp_Factory;
import com.qxdzbc.p6.app.action.app.create_new_wb.applier.CreateNewWorkbookApplierImp;
import com.qxdzbc.p6.app.action.app.create_new_wb.applier.CreateNewWorkbookApplierImp_Factory;
import com.qxdzbc.p6.app.action.app.create_new_wb.rm.CreateNewWbRMImp;
import com.qxdzbc.p6.app.action.app.create_new_wb.rm.CreateNewWbRMImp_Factory;
import com.qxdzbc.p6.app.action.app.get_wb.GetWorkbookAction;
import com.qxdzbc.p6.app.action.app.get_wb.GetWorkbookActionImp;
import com.qxdzbc.p6.app.action.app.get_wb.GetWorkbookActionImp_Factory;
import com.qxdzbc.p6.app.action.app.load_wb.LoadWorkbookAction;
import com.qxdzbc.p6.app.action.app.load_wb.LoadWorkbookActionImp;
import com.qxdzbc.p6.app.action.app.load_wb.LoadWorkbookActionImp_Factory;
import com.qxdzbc.p6.app.action.app.load_wb.applier.LoadWorkbookApplierImp;
import com.qxdzbc.p6.app.action.app.load_wb.applier.LoadWorkbookApplierImp_Factory;
import com.qxdzbc.p6.app.action.app.load_wb.applier.LoadWorkbookInternalApplierImp;
import com.qxdzbc.p6.app.action.app.load_wb.applier.LoadWorkbookInternalApplierImp_Factory;
import com.qxdzbc.p6.app.action.app.load_wb.rm.LoadWorkbookRMImp;
import com.qxdzbc.p6.app.action.app.load_wb.rm.LoadWorkbookRMImp_Factory;
import com.qxdzbc.p6.app.action.app.process_save_path.MakeSavePathImp;
import com.qxdzbc.p6.app.action.app.process_save_path.MakeSavePathImp_Factory;
import com.qxdzbc.p6.app.action.app.restart_kernel.applier.RestartKernelApplierImp;
import com.qxdzbc.p6.app.action.app.restart_kernel.applier.RestartKernelApplierImp_Factory;
import com.qxdzbc.p6.app.action.app.restart_kernel.rm.RestartKernelRMImp;
import com.qxdzbc.p6.app.action.app.restart_kernel.rm.RestartKernelRMImp_Factory;
import com.qxdzbc.p6.app.action.app.save_wb.SaveWorkbookAction;
import com.qxdzbc.p6.app.action.app.save_wb.SaveWorkbookActionImp;
import com.qxdzbc.p6.app.action.app.save_wb.SaveWorkbookActionImp_Factory;
import com.qxdzbc.p6.app.action.app.set_active_wb.SetActiveWorkbookAction;
import com.qxdzbc.p6.app.action.app.set_active_wb.SetActiveWorkbookActionImp;
import com.qxdzbc.p6.app.action.app.set_active_wb.SetActiveWorkbookActionImp_Factory;
import com.qxdzbc.p6.app.action.app.set_active_wd.SetActiveWindowAction;
import com.qxdzbc.p6.app.action.app.set_active_wd.SetActiveWindowActionImp;
import com.qxdzbc.p6.app.action.app.set_active_wd.SetActiveWindowActionImp_Factory;
import com.qxdzbc.p6.app.action.app.set_wbkey.ReplaceWorkbookKeyActionImp;
import com.qxdzbc.p6.app.action.app.set_wbkey.ReplaceWorkbookKeyActionImp_Factory;
import com.qxdzbc.p6.app.action.applier.BaseApplier;
import com.qxdzbc.p6.app.action.applier.BaseApplierImp;
import com.qxdzbc.p6.app.action.applier.BaseApplierImp_Factory;
import com.qxdzbc.p6.app.action.applier.ErrorApplierImp;
import com.qxdzbc.p6.app.action.applier.ErrorApplierImp_Factory;
import com.qxdzbc.p6.app.action.applier.WorkbookUpdateCommonApplierImp;
import com.qxdzbc.p6.app.action.applier.WorkbookUpdateCommonApplierImp_Factory;
import com.qxdzbc.p6.app.action.cell.CellRM;
import com.qxdzbc.p6.app.action.cell.CellRMImp;
import com.qxdzbc.p6.app.action.cell.CellRMImp_Factory;
import com.qxdzbc.p6.app.action.cell.cell_update.UpdateCellAction;
import com.qxdzbc.p6.app.action.cell.cell_update.UpdateCellActionImp;
import com.qxdzbc.p6.app.action.cell.cell_update.UpdateCellActionImp_Factory;
import com.qxdzbc.p6.app.action.cell.copy_cell.CopyCellAction;
import com.qxdzbc.p6.app.action.cell.copy_cell.CopyCellActionImp;
import com.qxdzbc.p6.app.action.cell.copy_cell.CopyCellActionImp_Factory;
import com.qxdzbc.p6.app.action.cell.multi_cell_update.MultiCellUpdateAction;
import com.qxdzbc.p6.app.action.cell.multi_cell_update.MultiCellUpdateActionImp;
import com.qxdzbc.p6.app.action.cell.multi_cell_update.MultiCellUpdateActionImp_Factory;
import com.qxdzbc.p6.app.action.cell.update_cell_format.UpdateCellFormatActionImp;
import com.qxdzbc.p6.app.action.cell.update_cell_format.UpdateCellFormatActionImp_Factory;
import com.qxdzbc.p6.app.action.cell_editor.close_cell_editor.CloseCellEditorActionImp;
import com.qxdzbc.p6.app.action.cell_editor.close_cell_editor.CloseCellEditorActionImp_Factory;
import com.qxdzbc.p6.app.action.cell_editor.color_formula.ColorFormulaInCellEditorAction;
import com.qxdzbc.p6.app.action.cell_editor.color_formula.ColorFormulaInCellEditorActionImp;
import com.qxdzbc.p6.app.action.cell_editor.color_formula.ColorFormulaInCellEditorActionImp_Factory;
import com.qxdzbc.p6.app.action.cell_editor.cycle_formula_lock_state.CycleFormulaLockStateAction;
import com.qxdzbc.p6.app.action.cell_editor.cycle_formula_lock_state.CycleFormulaLockStateImp;
import com.qxdzbc.p6.app.action.cell_editor.cycle_formula_lock_state.CycleFormulaLockStateImp_Factory;
import com.qxdzbc.p6.app.action.cell_editor.open_cell_editor.OpenCellEditorAction;
import com.qxdzbc.p6.app.action.cell_editor.open_cell_editor.OpenCellEditorImp;
import com.qxdzbc.p6.app.action.cell_editor.open_cell_editor.OpenCellEditorImp_Factory;
import com.qxdzbc.p6.app.action.cell_editor.run_formula.RunFormulaOrSaveValueToCellActionImp;
import com.qxdzbc.p6.app.action.cell_editor.run_formula.RunFormulaOrSaveValueToCellActionImp_Factory;
import com.qxdzbc.p6.app.action.cell_editor.update_range_selector_text.UpdateRangeSelectorTextImp;
import com.qxdzbc.p6.app.action.cell_editor.update_range_selector_text.UpdateRangeSelectorTextImp_Factory;
import com.qxdzbc.p6.app.action.common.BuildAnnotatedTextActionImp;
import com.qxdzbc.p6.app.action.common.BuildAnnotatedTextActionImp_Factory;
import com.qxdzbc.p6.app.action.cursor.copy_cursor_range_to_clipboard.CopyCursorRangeToClipboardActionImp;
import com.qxdzbc.p6.app.action.cursor.copy_cursor_range_to_clipboard.CopyCursorRangeToClipboardActionImp_Factory;
import com.qxdzbc.p6.app.action.cursor.handle_cursor_keyboard_event.HandleCursorKeyboardEventActionImp;
import com.qxdzbc.p6.app.action.cursor.handle_cursor_keyboard_event.HandleCursorKeyboardEventActionImp_Factory;
import com.qxdzbc.p6.app.action.cursor.paste_range_to_cursor.PasteRangeToCursorImp;
import com.qxdzbc.p6.app.action.cursor.paste_range_to_cursor.PasteRangeToCursorImp_Factory;
import com.qxdzbc.p6.app.action.cursor.thumb.drag_thumb_action.DragThumbAction;
import com.qxdzbc.p6.app.action.cursor.thumb.drag_thumb_action.DragThumbActionImp;
import com.qxdzbc.p6.app.action.cursor.thumb.drag_thumb_action.DragThumbActionImp_Factory;
import com.qxdzbc.p6.app.action.cursor.thumb.drag_thumb_action.EndThumbDragAction;
import com.qxdzbc.p6.app.action.cursor.thumb.drag_thumb_action.EndThumbDragActionImp;
import com.qxdzbc.p6.app.action.cursor.thumb.drag_thumb_action.EndThumbDragActionImp_Factory;
import com.qxdzbc.p6.app.action.cursor.undo_on_cursor.UndoOnCursorActionImp;
import com.qxdzbc.p6.app.action.cursor.undo_on_cursor.UndoOnCursorActionImp_Factory;
import com.qxdzbc.p6.app.action.range.paste_range.applier.PasteRangeApplierImp;
import com.qxdzbc.p6.app.action.range.paste_range.applier.PasteRangeApplierImp_Factory;
import com.qxdzbc.p6.app.action.range.paste_range.rm.PasteRangeRMImp;
import com.qxdzbc.p6.app.action.range.paste_range.rm.PasteRangeRMImp_Factory;
import com.qxdzbc.p6.app.action.range.range_to_clipboard.RangeToClipboardActionImp;
import com.qxdzbc.p6.app.action.range.range_to_clipboard.RangeToClipboardActionImp_Factory;
import com.qxdzbc.p6.app.action.range.range_to_clipboard.applier.RangeToClipboardApplierImp;
import com.qxdzbc.p6.app.action.range.range_to_clipboard.applier.RangeToClipboardApplierImp_Factory;
import com.qxdzbc.p6.app.action.range.range_to_clipboard.applier.RangeToClipboardInternalApplierImp;
import com.qxdzbc.p6.app.action.range.range_to_clipboard.applier.RangeToClipboardInternalApplierImp_Factory;
import com.qxdzbc.p6.app.action.range.range_to_clipboard.rm.CopyRangeToClipboardRMImp;
import com.qxdzbc.p6.app.action.range.range_to_clipboard.rm.CopyRangeToClipboardRMImp_Factory;
import com.qxdzbc.p6.app.action.rpc.AppRpcActionImp;
import com.qxdzbc.p6.app.action.rpc.AppRpcActionImp_Factory;
import com.qxdzbc.p6.app.action.script.ScriptApplierImp;
import com.qxdzbc.p6.app.action.script.ScriptApplierImp_Factory;
import com.qxdzbc.p6.app.action.script.ScriptRMImp;
import com.qxdzbc.p6.app.action.script.ScriptRMImp_Factory;
import com.qxdzbc.p6.app.action.script.new_script.applier.NewScriptApplierImp;
import com.qxdzbc.p6.app.action.script.new_script.applier.NewScriptApplierImp_Factory;
import com.qxdzbc.p6.app.action.script.new_script.rm.NewScriptRMImp;
import com.qxdzbc.p6.app.action.script.new_script.rm.NewScriptRMImp_Factory;
import com.qxdzbc.p6.app.action.script.script_change.applier.ScriptChangeApplierImp;
import com.qxdzbc.p6.app.action.script.script_change.applier.ScriptChangeApplierImp_Factory;
import com.qxdzbc.p6.app.action.window.WindowAction;
import com.qxdzbc.p6.app.action.window.WindowActionImp;
import com.qxdzbc.p6.app.action.window.WindowActionImp_Factory;
import com.qxdzbc.p6.app.action.window.close_window.CloseWindowActionImp;
import com.qxdzbc.p6.app.action.window.close_window.CloseWindowActionImp_Factory;
import com.qxdzbc.p6.app.action.window.pick_active_wb.PickDefaultActiveWbAction;
import com.qxdzbc.p6.app.action.window.pick_active_wb.PickDefaultActiveWbActionImp;
import com.qxdzbc.p6.app.action.window.pick_active_wb.PickDefaultActiveWbActionImp_Factory;
import com.qxdzbc.p6.app.action.workbook.WorkbookAction;
import com.qxdzbc.p6.app.action.workbook.WorkbookActionImp;
import com.qxdzbc.p6.app.action.workbook.WorkbookActionImp_Factory;
import com.qxdzbc.p6.app.action.workbook.add_ws.CreateNewWorksheetActionImp;
import com.qxdzbc.p6.app.action.workbook.add_ws.CreateNewWorksheetActionImp_Factory;
import com.qxdzbc.p6.app.action.workbook.add_ws.applier.CreateNewWorksheetApplierImp;
import com.qxdzbc.p6.app.action.workbook.add_ws.applier.CreateNewWorksheetApplierImp_Factory;
import com.qxdzbc.p6.app.action.workbook.add_ws.rm.CreateNewWorksheetRMImp;
import com.qxdzbc.p6.app.action.workbook.add_ws.rm.CreateNewWorksheetRMImp_Factory;
import com.qxdzbc.p6.app.action.workbook.click_on_ws_tab_item.SwitchWorksheetActionImp;
import com.qxdzbc.p6.app.action.workbook.click_on_ws_tab_item.SwitchWorksheetActionImp_Factory;
import com.qxdzbc.p6.app.action.workbook.delete_worksheet.DeleteWorksheetActionImp;
import com.qxdzbc.p6.app.action.workbook.delete_worksheet.DeleteWorksheetActionImp_Factory;
import com.qxdzbc.p6.app.action.workbook.delete_worksheet.applier.DeleteWorksheetApplierImp;
import com.qxdzbc.p6.app.action.workbook.delete_worksheet.applier.DeleteWorksheetApplierImp_Factory;
import com.qxdzbc.p6.app.action.workbook.delete_worksheet.rm.DeleteWorksheetRMImp;
import com.qxdzbc.p6.app.action.workbook.delete_worksheet.rm.DeleteWorksheetRMImp_Factory;
import com.qxdzbc.p6.app.action.workbook.new_worksheet.NewWorksheetAction;
import com.qxdzbc.p6.app.action.workbook.new_worksheet.NewWorksheetActionImp;
import com.qxdzbc.p6.app.action.workbook.new_worksheet.NewWorksheetActionImp_Factory;
import com.qxdzbc.p6.app.action.workbook.remove_all_ws.RemoveAllWorksheetAction;
import com.qxdzbc.p6.app.action.workbook.remove_all_ws.RemoveAllWorksheetActionImp;
import com.qxdzbc.p6.app.action.workbook.remove_all_ws.RemoveAllWorksheetActionImp_Factory;
import com.qxdzbc.p6.app.action.workbook.rename_ws.RenameWorksheetActionImp;
import com.qxdzbc.p6.app.action.workbook.rename_ws.RenameWorksheetActionImp_Factory;
import com.qxdzbc.p6.app.action.workbook.set_active_ws.SetActiveWorksheetActionImp;
import com.qxdzbc.p6.app.action.workbook.set_active_ws.SetActiveWorksheetActionImp_Factory;
import com.qxdzbc.p6.app.action.workbook.set_active_ws.applier.SetActiveWorksheetApplierImp;
import com.qxdzbc.p6.app.action.workbook.set_active_ws.applier.SetActiveWorksheetApplierImp_Factory;
import com.qxdzbc.p6.app.action.workbook.set_active_ws.rm.SetActiveWorksheetRMImp;
import com.qxdzbc.p6.app.action.workbook.set_active_ws.rm.SetActiveWorksheetRMImp_Factory;
import com.qxdzbc.p6.app.action.worksheet.WorksheetAction;
import com.qxdzbc.p6.app.action.worksheet.WorksheetActionImp;
import com.qxdzbc.p6.app.action.worksheet.WorksheetActionImp_Factory;
import com.qxdzbc.p6.app.action.worksheet.action2.WorksheetAction2;
import com.qxdzbc.p6.app.action.worksheet.action2.WorksheetAction2Imp;
import com.qxdzbc.p6.app.action.worksheet.action2.WorksheetAction2Imp_Factory;
import com.qxdzbc.p6.app.action.worksheet.compute_slider_size.ComputeSliderSizeAction;
import com.qxdzbc.p6.app.action.worksheet.compute_slider_size.ComputeSliderSizeActionImp;
import com.qxdzbc.p6.app.action.worksheet.compute_slider_size.ComputeSliderSizeActionImp_Factory;
import com.qxdzbc.p6.app.action.worksheet.delete_multi.DeleteMultiCellAction;
import com.qxdzbc.p6.app.action.worksheet.delete_multi.DeleteMultiCellActionImp;
import com.qxdzbc.p6.app.action.worksheet.delete_multi.DeleteMultiCellActionImp_Factory;
import com.qxdzbc.p6.app.action.worksheet.delete_multi.applier.DeleteMultiApplierImp;
import com.qxdzbc.p6.app.action.worksheet.delete_multi.applier.DeleteMultiApplierImp_Factory;
import com.qxdzbc.p6.app.action.worksheet.delete_multi.rm.DeleteMultiRMImp;
import com.qxdzbc.p6.app.action.worksheet.delete_multi.rm.DeleteMultiRMImp_Factory;
import com.qxdzbc.p6.app.action.worksheet.load_data.LoadDataAction;
import com.qxdzbc.p6.app.action.worksheet.load_data.LoadDataActionImp;
import com.qxdzbc.p6.app.action.worksheet.load_data.LoadDataActionImp_Factory;
import com.qxdzbc.p6.app.action.worksheet.make_cell_editor_display_text.MakeCellEditorTextAction;
import com.qxdzbc.p6.app.action.worksheet.make_cell_editor_display_text.MakeCellEditorTextActionImp;
import com.qxdzbc.p6.app.action.worksheet.make_cell_editor_display_text.MakeCellEditorTextActionImp_Factory;
import com.qxdzbc.p6.app.action.worksheet.make_slider_follow_cell.MakeSliderFollowCellActionImp;
import com.qxdzbc.p6.app.action.worksheet.make_slider_follow_cell.MakeSliderFollowCellActionImp_Factory;
import com.qxdzbc.p6.app.action.worksheet.mouse_on_ws.MouseOnWorksheetAction;
import com.qxdzbc.p6.app.action.worksheet.mouse_on_ws.MouseOnWorksheetActionImp;
import com.qxdzbc.p6.app.action.worksheet.mouse_on_ws.MouseOnWorksheetActionImp_Factory;
import com.qxdzbc.p6.app.action.worksheet.mouse_on_ws.click_on_cell.ClickOnCellAction;
import com.qxdzbc.p6.app.action.worksheet.mouse_on_ws.click_on_cell.ClickOnCellActionImp;
import com.qxdzbc.p6.app.action.worksheet.mouse_on_ws.click_on_cell.ClickOnCellActionImp_Factory;
import com.qxdzbc.p6.app.action.worksheet.paste_range.PasteRangeActionImp;
import com.qxdzbc.p6.app.action.worksheet.paste_range.PasteRangeActionImp_Factory;
import com.qxdzbc.p6.app.action.worksheet.release_focus.RestoreWindowFocusStateImp;
import com.qxdzbc.p6.app.action.worksheet.release_focus.RestoreWindowFocusStateImp_Factory;
import com.qxdzbc.p6.app.action.worksheet.remove_all_cell.RemoveAllCellAction;
import com.qxdzbc.p6.app.action.worksheet.remove_all_cell.RemoveAllCellActionImp;
import com.qxdzbc.p6.app.action.worksheet.remove_all_cell.RemoveAllCellActionImp_Factory;
import com.qxdzbc.p6.app.action.worksheet.rename_ws.applier.RenameWorksheetApplierImp;
import com.qxdzbc.p6.app.action.worksheet.rename_ws.applier.RenameWorksheetApplierImp_Factory;
import com.qxdzbc.p6.app.action.worksheet.rename_ws.applier.RenameWorksheetInternalApplierImp;
import com.qxdzbc.p6.app.action.worksheet.rename_ws.applier.RenameWorksheetInternalApplierImp_Factory;
import com.qxdzbc.p6.app.action.worksheet.rename_ws.rm.RenameWorksheetRMImp;
import com.qxdzbc.p6.app.action.worksheet.rename_ws.rm.RenameWorksheetRMImp_Factory;
import com.qxdzbc.p6.app.app_context.AppContext;
import com.qxdzbc.p6.app.app_context.AppContextImp;
import com.qxdzbc.p6.app.app_context.AppContextImp_Factory;
import com.qxdzbc.p6.app.code.PythonCommander;
import com.qxdzbc.p6.app.code.PythonCommanderImp;
import com.qxdzbc.p6.app.code.PythonCommanderImp_Factory;
import com.qxdzbc.p6.app.coderunner.CodeRunner;
import com.qxdzbc.p6.app.coderunner.PythonCodeRunner;
import com.qxdzbc.p6.app.coderunner.PythonCodeRunner_Factory;
import com.qxdzbc.p6.app.common.formatter.RangeAddressFormatter;
import com.qxdzbc.p6.app.common.formatter.RangeAddressFormatterImp;
import com.qxdzbc.p6.app.common.formatter.RangeAddressFormatterImp_Factory;
import com.qxdzbc.p6.app.communication.event.P6EventTable;
import com.qxdzbc.p6.app.document.range.LazyRangeFactory;
import com.qxdzbc.p6.app.document.range.LazyRangeFactory_Impl;
import com.qxdzbc.p6.app.document.range.LazyRange_Factory;
import com.qxdzbc.p6.app.document.range.copy_paste.RangeCopierImp;
import com.qxdzbc.p6.app.document.range.copy_paste.RangeCopierImp_Factory;
import com.qxdzbc.p6.app.document.range.copy_paste.RangePasterImp;
import com.qxdzbc.p6.app.document.range.copy_paste.RangePasterImp_Factory;
import com.qxdzbc.p6.app.document.range.copy_paste.RangeRangePasterImp;
import com.qxdzbc.p6.app.document.range.copy_paste.RangeRangePasterImp_Factory;
import com.qxdzbc.p6.app.document.range.copy_paste.SingleCellPaster;
import com.qxdzbc.p6.app.document.range.copy_paste.SingleCellPaster_Factory;
import com.qxdzbc.p6.app.document.script.ScriptContainer;
import com.qxdzbc.p6.app.document.wb_container.WorkbookContainer;
import com.qxdzbc.p6.app.document.wb_container.WorkbookContainerImp;
import com.qxdzbc.p6.app.document.wb_container.WorkbookContainerImp_Factory;
import com.qxdzbc.p6.app.document.workbook.AutoNameWbFactory;
import com.qxdzbc.p6.app.document.workbook.AutoNameWbFactory_Factory;
import com.qxdzbc.p6.app.document.workbook.WorkbookFactory;
import com.qxdzbc.p6.app.document.worksheet.WsNameGeneratorImp;
import com.qxdzbc.p6.app.document.worksheet.WsNameGeneratorImp_Factory;
import com.qxdzbc.p6.app.file.loader.P6FileLoader;
import com.qxdzbc.p6.app.file.loader.P6FileLoaderImp;
import com.qxdzbc.p6.app.file.loader.P6FileLoaderImp_Factory;
import com.qxdzbc.p6.app.file.saver.P6SaverImp;
import com.qxdzbc.p6.app.file.saver.P6SaverImp_Factory;
import com.qxdzbc.p6.app.oddity.ErrorContainer;
import com.qxdzbc.p6.di.CoroutineModule_Companion_ActionDispatcherDefaultFactory;
import com.qxdzbc.p6.di.MsgApiModule_Companion_KernelContextFactory;
import com.qxdzbc.p6.di.MsgApiModule_Companion_KernelContextReadOnlyFactory;
import com.qxdzbc.p6.di.MsgApiModule_Companion_KernelServiceManagerFactory;
import com.qxdzbc.p6.di.MsgApiModule_Companion_ZContextFactory;
import com.qxdzbc.p6.di.P6Module_Companion_BFalseFactory;
import com.qxdzbc.p6.di.P6Module_Companion_BTrueFactory;
import com.qxdzbc.p6.di.P6Module_Companion_DefaultColRangeFactory;
import com.qxdzbc.p6.di.P6Module_Companion_DefaultRowRangeFactory;
import com.qxdzbc.p6.di.P6Module_Companion_EventServerPortFactory;
import com.qxdzbc.p6.di.P6Module_Companion_EventServerSocketFactory;
import com.qxdzbc.p6.di.P6Module_Companion_FileUtilFactory;
import com.qxdzbc.p6.di.P6Module_Companion_NullIntFactory;
import com.qxdzbc.p6.di.P6Module_Companion_P6EventTableFactory;
import com.qxdzbc.p6.di.TranslatorModule_Companion_FunctionMapFactory;
import com.qxdzbc.p6.di.TranslatorModule_Companion_FunctionMapMsFactory;
import com.qxdzbc.p6.di.UtilModule_Companion_BinaryCopierFactory;
import com.qxdzbc.p6.di.UtilModule_Companion_GsonFactory;
import com.qxdzbc.p6.di.UtilModule_Companion_ReadFileFunctionFactory;
import com.qxdzbc.p6.di.state.app_state.AppStateModule_Companion_AppOddityContainerMsFactory;
import com.qxdzbc.p6.di.state.app_state.AppStateModule_Companion_AppScriptContFactory;
import com.qxdzbc.p6.di.state.app_state.AppStateModule_Companion_AppStateMsFactory;
import com.qxdzbc.p6.di.state.app_state.AppStateModule_Companion_CellEditorStateMsFactory;
import com.qxdzbc.p6.di.state.app_state.AppStateModule_Companion_CentralScriptContainerFactory;
import com.qxdzbc.p6.di.state.app_state.AppStateModule_Companion_CodeEditorStateFactory;
import com.qxdzbc.p6.di.state.app_state.AppStateModule_Companion_DocumentContainerMsFactory;
import com.qxdzbc.p6.di.state.app_state.AppStateModule_Companion_InitActiveWindowPointerFactory;
import com.qxdzbc.p6.di.state.app_state.AppStateModule_Companion_InitSingleTranslatorMapFactory;
import com.qxdzbc.p6.di.state.app_state.AppStateModule_Companion_KernelStatusMsFactory;
import com.qxdzbc.p6.di.state.app_state.AppStateModule_Companion_StateContainerMsFactory;
import com.qxdzbc.p6.di.state.app_state.AppStateModule_Companion_StateContainerStFactory;
import com.qxdzbc.p6.di.state.app_state.AppStateModule_Companion_SubAppStateContainerMsFactory;
import com.qxdzbc.p6.di.state.app_state.AppStateModule_Companion_TranslatorContainerMsFactory;
import com.qxdzbc.p6.di.state.app_state.AppStateModule_Companion_TranslatorContainerStFactory;
import com.qxdzbc.p6.di.state.app_state.AppStateModule_Companion_TranslatorMapMsFactory;
import com.qxdzbc.p6.di.state.app_state.AppStateModule_Companion_WbContainerFactory;
import com.qxdzbc.p6.di.state.app_state.AppStateModule_Companion_WindowStateMapFactory;
import com.qxdzbc.p6.di.state.app_state.AppStateModule_Companion_WorkbookStateContMsFactory;
import com.qxdzbc.p6.di.state.app_state.CellFormatTableModule_Companion_AlignmentFormatTableFactory;
import com.qxdzbc.p6.di.state.app_state.CellFormatTableModule_Companion_BoolFormatTableFactory;
import com.qxdzbc.p6.di.state.app_state.CellFormatTableModule_Companion_CellFormatTableMsFactory;
import com.qxdzbc.p6.di.state.app_state.CellFormatTableModule_Companion_ColorTableFactory;
import com.qxdzbc.p6.di.state.app_state.CellFormatTableModule_Companion_FloatFormatTableFactory;
import com.qxdzbc.p6.di.state.wb.WorkbookStateModule_Companion_DFactory;
import com.qxdzbc.p6.di.state.wb.WorkbookStateModule_Companion_DefaultScriptContMsFactory;
import com.qxdzbc.p6.di.state.wb.WorkbookStateModule_Companion_ZFactory;
import com.qxdzbc.p6.di.state.ws.WorksheetStateModule_Companion_ClipboardRangeFactory;
import com.qxdzbc.p6.di.state.ws.WorksheetStateModule_Companion_DefaultActiveWorksheetPointerFactory;
import com.qxdzbc.p6.di.state.ws.WorksheetStateModule_Companion_DefaultCellStateContainerFactory;
import com.qxdzbc.p6.di.state.ws.WorksheetStateModule_Companion_DefaultLayoutCoorMsFactory;
import com.qxdzbc.p6.di.state.ws.WorksheetStateModule_Companion_DefaultRangeConstraintFactory;
import com.qxdzbc.p6.di.state.ws.WorksheetStateModule_Companion_DefaultVisibleColRangeFactory;
import com.qxdzbc.p6.di.state.ws.WorksheetStateModule_Companion_DefaultVisibleRowRangeFactory;
import com.qxdzbc.p6.di.state.ws.WorksheetStateModule_Companion_EcSetFactory;
import com.qxdzbc.p6.di.state.ws.WorksheetStateModule_Companion_ErSetFactory;
import com.qxdzbc.p6.di.state.ws.WorksheetStateModule_Companion_NullRangeAddressFactory;
import com.qxdzbc.p6.di.state.ws.WorksheetStateModule_Companion_ResizeColBarStateMsFactory;
import com.qxdzbc.p6.di.state.ws.WorksheetStateModule_Companion_ResizeRowBarStateMsFactory;
import com.qxdzbc.p6.di.state.ws.WorksheetStateModule_Companion_SelectRectStateFactory;
import com.qxdzbc.p6.di.state.ws.WorksheetStateModule_Companion_SelectRectStateMsFactory;
import com.qxdzbc.p6.di.status_bar.StatusBarModule_Companion_KernelStatusItemStateMsFactory;
import com.qxdzbc.p6.di.status_bar.StatusBarModule_Companion_RPCStatusItemStateMsFactory;
import com.qxdzbc.p6.di.status_bar.StatusBarModule_Companion_StatusBarStateMsFactory;
import com.qxdzbc.p6.formula.translator.antlr.FormulaBaseVisitor;
import com.qxdzbc.p6.message.api.connection.kernel_context.KernelContext;
import com.qxdzbc.p6.message.api.connection.kernel_context.KernelContextReadOnly;
import com.qxdzbc.p6.message.api.connection.kernel_context.KernelStatus;
import com.qxdzbc.p6.message.api.connection.kernel_services.KernelServiceManager;
import com.qxdzbc.p6.message.di.MessageApiComponent;
import com.qxdzbc.p6.rpc.MsP6RpcServer;
import com.qxdzbc.p6.rpc.MsP6RpcServer_Factory;
import com.qxdzbc.p6.rpc.P6RpcServer;
import com.qxdzbc.p6.rpc.P6RpcServerImp;
import com.qxdzbc.p6.rpc.P6RpcServerImp_Factory;
import com.qxdzbc.p6.rpc.app.AppRpcService;
import com.qxdzbc.p6.rpc.app.AppRpcService_Factory;
import com.qxdzbc.p6.rpc.cell.CellRpcActionsImp;
import com.qxdzbc.p6.rpc.cell.CellRpcActionsImp_Factory;
import com.qxdzbc.p6.rpc.cell.CellRpcService;
import com.qxdzbc.p6.rpc.cell.CellRpcService_Factory;
import com.qxdzbc.p6.rpc.workbook.WorkbookRpcActionsImp;
import com.qxdzbc.p6.rpc.workbook.WorkbookRpcActionsImp_Factory;
import com.qxdzbc.p6.rpc.workbook.WorkbookRpcService;
import com.qxdzbc.p6.rpc.workbook.WorkbookRpcService_Factory;
import com.qxdzbc.p6.rpc.worksheet.WorksheetRpcActionImp;
import com.qxdzbc.p6.rpc.worksheet.WorksheetRpcActionImp_Factory;
import com.qxdzbc.p6.rpc.worksheet.WorksheetRpcService;
import com.qxdzbc.p6.rpc.worksheet.WorksheetRpcService_Factory;
import com.qxdzbc.p6.translator.P6Translator;
import com.qxdzbc.p6.translator.TranslatorMap;
import com.qxdzbc.p6.translator.formula.FunctionMap;
import com.qxdzbc.p6.translator.formula.function_def.P6FunctionDefinitions;
import com.qxdzbc.p6.translator.formula.function_def.P6FunctionDefinitionsImp;
import com.qxdzbc.p6.translator.formula.function_def.P6FunctionDefinitionsImp_Factory;
import com.qxdzbc.p6.translator.jvm_translator.CellLiteralParser;
import com.qxdzbc.p6.translator.jvm_translator.CellLiteralParserImp;
import com.qxdzbc.p6.translator.jvm_translator.CellLiteralParserImp_Factory;
import com.qxdzbc.p6.translator.jvm_translator.JvmFormulaTranslatorFactory;
import com.qxdzbc.p6.translator.jvm_translator.JvmFormulaTranslatorFactory_Impl;
import com.qxdzbc.p6.translator.jvm_translator.JvmFormulaTranslator_Factory;
import com.qxdzbc.p6.translator.jvm_translator.JvmFormulaVisitorFactory;
import com.qxdzbc.p6.translator.jvm_translator.JvmFormulaVisitorFactory_Impl;
import com.qxdzbc.p6.translator.jvm_translator.JvmFormulaVisitor_Factory;
import com.qxdzbc.p6.translator.jvm_translator.tree_extractor.TreeExtractor;
import com.qxdzbc.p6.translator.jvm_translator.tree_extractor.TreeExtractorImp_Factory;
import com.qxdzbc.p6.translator.partial_text_element_extractor.PartialFormulaTreeExtractor;
import com.qxdzbc.p6.translator.partial_text_element_extractor.PartialFormulaTreeExtractor_Factory;
import com.qxdzbc.p6.translator.partial_text_element_extractor.PartialTextElementTranslator;
import com.qxdzbc.p6.translator.partial_text_element_extractor.PartialTextElementTranslator_Factory;
import com.qxdzbc.p6.translator.partial_text_element_extractor.TextElementResult;
import com.qxdzbc.p6.translator.partial_text_element_extractor.TextElementVisitor;
import com.qxdzbc.p6.translator.partial_text_element_extractor.TextElementVisitor_Factory;
import com.qxdzbc.p6.ui.app.ActiveWindowPointer;
import com.qxdzbc.p6.ui.app.action.AppAction;
import com.qxdzbc.p6.ui.app.action.AppActionImp;
import com.qxdzbc.p6.ui.app.action.AppActionImp_Factory;
import com.qxdzbc.p6.ui.app.action.AppActionTable;
import com.qxdzbc.p6.ui.app.action.AppActionTableImp;
import com.qxdzbc.p6.ui.app.action.AppActionTableImp_Factory;
import com.qxdzbc.p6.ui.app.cell_editor.actions.CellEditorAction;
import com.qxdzbc.p6.ui.app.cell_editor.actions.CellEditorActionImp;
import com.qxdzbc.p6.ui.app.cell_editor.actions.CellEditorActionImp_Factory;
import com.qxdzbc.p6.ui.app.cell_editor.actions.differ.TextDiffer;
import com.qxdzbc.p6.ui.app.cell_editor.actions.differ.TextDifferImp;
import com.qxdzbc.p6.ui.app.cell_editor.actions.differ.TextDifferImp_Factory;
import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorState;
import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorStateImp;
import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorStateImp_Factory;
import com.qxdzbc.p6.ui.app.error_router.ErrorRouter;
import com.qxdzbc.p6.ui.app.error_router.ErrorRouterImp;
import com.qxdzbc.p6.ui.app.error_router.ErrorRouterImp_Factory;
import com.qxdzbc.p6.ui.app.state.AppState;
import com.qxdzbc.p6.ui.app.state.AppStateImp;
import com.qxdzbc.p6.ui.app.state.AppStateImp_Factory;
import com.qxdzbc.p6.ui.app.state.DocumentContainer;
import com.qxdzbc.p6.ui.app.state.DocumentContainerImp;
import com.qxdzbc.p6.ui.app.state.DocumentContainerImp_Factory;
import com.qxdzbc.p6.ui.app.state.StateContainer;
import com.qxdzbc.p6.ui.app.state.StateContainerImp;
import com.qxdzbc.p6.ui.app.state.StateContainerImp_Factory;
import com.qxdzbc.p6.ui.app.state.SubAppStateContainer;
import com.qxdzbc.p6.ui.app.state.SubAppStateContainerImp;
import com.qxdzbc.p6.ui.app.state.SubAppStateContainerImp_Factory;
import com.qxdzbc.p6.ui.app.state.TranslatorContainer;
import com.qxdzbc.p6.ui.app.state.TranslatorContainerImp;
import com.qxdzbc.p6.ui.app.state.TranslatorContainerImp_Factory;
import com.qxdzbc.p6.ui.common.color_generator.ColorGenerator;
import com.qxdzbc.p6.ui.common.color_generator.FormulaColorGenerator;
import com.qxdzbc.p6.ui.common.color_generator.FormulaColorGeneratorImp;
import com.qxdzbc.p6.ui.common.color_generator.FormulaColorGeneratorImp_Factory;
import com.qxdzbc.p6.ui.common.color_generator.RandomColorGenerator_Factory;
import com.qxdzbc.p6.ui.document.workbook.action.WorkbookActionTable;
import com.qxdzbc.p6.ui.document.workbook.action.WorkbookActionTableImp;
import com.qxdzbc.p6.ui.document.workbook.action.WorkbookActionTableImp_Factory;
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookStateFactory;
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookStateFactory_Impl;
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookStateImp_Factory;
import com.qxdzbc.p6.ui.document.workbook.state.cont.WorkbookStateContainer;
import com.qxdzbc.p6.ui.document.worksheet.action.WorksheetActionTable;
import com.qxdzbc.p6.ui.document.worksheet.action.WorksheetActionTableImp;
import com.qxdzbc.p6.ui.document.worksheet.action.WorksheetActionTableImp_Factory;
import com.qxdzbc.p6.ui.document.worksheet.cursor.actions.CursorAction;
import com.qxdzbc.p6.ui.document.worksheet.cursor.actions.CursorActionImp;
import com.qxdzbc.p6.ui.document.worksheet.cursor.actions.CursorActionImp_Factory;
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorStateFactory;
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorStateFactory_Impl;
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorStateImp_Factory;
import com.qxdzbc.p6.ui.document.worksheet.cursor.thumb.action.ThumbActionImp;
import com.qxdzbc.p6.ui.document.worksheet.cursor.thumb.action.ThumbActionImp_Factory;
import com.qxdzbc.p6.ui.document.worksheet.cursor.thumb.state.ThumbStateFactory;
import com.qxdzbc.p6.ui.document.worksheet.cursor.thumb.state.ThumbStateFactory_Impl;
import com.qxdzbc.p6.ui.document.worksheet.cursor.thumb.state.ThumbStateImp_Factory;
import com.qxdzbc.p6.ui.document.worksheet.ruler.actions.RulerAction;
import com.qxdzbc.p6.ui.document.worksheet.ruler.actions.RulerActionImp;
import com.qxdzbc.p6.ui.document.worksheet.ruler.actions.RulerActionImp_Factory;
import com.qxdzbc.p6.ui.document.worksheet.select_rect.SelectRectState;
import com.qxdzbc.p6.ui.document.worksheet.select_whole_col_for_selected_cell.SelectWholeColumnForAllSelectedCellActionImp;
import com.qxdzbc.p6.ui.document.worksheet.select_whole_col_for_selected_cell.SelectWholeColumnForAllSelectedCellActionImp_Factory;
import com.qxdzbc.p6.ui.document.worksheet.select_whole_row_for_selected_cells.SelectWholeRowForAllSelectedCellActionImp;
import com.qxdzbc.p6.ui.document.worksheet.select_whole_row_for_selected_cells.SelectWholeRowForAllSelectedCellActionImp_Factory;
import com.qxdzbc.p6.ui.document.worksheet.slider.GridSliderImp;
import com.qxdzbc.p6.ui.document.worksheet.slider.GridSliderImp_Factory;
import com.qxdzbc.p6.ui.document.worksheet.slider.LimitedGridSliderFactory;
import com.qxdzbc.p6.ui.document.worksheet.slider.LimitedGridSliderFactory_Impl;
import com.qxdzbc.p6.ui.document.worksheet.slider.LimitedSlider_Factory;
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetStateFactory;
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetStateFactory_Impl;
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetStateImp_Factory;
import com.qxdzbc.p6.ui.format.CellFormatTable;
import com.qxdzbc.p6.ui.format.CellFormatTableImp;
import com.qxdzbc.p6.ui.format.CellFormatTableImp_Factory;
import com.qxdzbc.p6.ui.kernel.KernelAction;
import com.qxdzbc.p6.ui.kernel.KernelActionImp;
import com.qxdzbc.p6.ui.kernel.KernelActionImp_Factory;
import com.qxdzbc.p6.ui.kernel.MsKernelContext;
import com.qxdzbc.p6.ui.kernel.MsKernelContext_Factory;
import com.qxdzbc.p6.ui.script_editor.ScriptEditorErrorRouter;
import com.qxdzbc.p6.ui.script_editor.ScriptEditorErrorRouterImp;
import com.qxdzbc.p6.ui.script_editor.ScriptEditorErrorRouterImp_Factory;
import com.qxdzbc.p6.ui.script_editor.action.CodeEditorActionImp;
import com.qxdzbc.p6.ui.script_editor.action.CodeEditorActionImp_Factory;
import com.qxdzbc.p6.ui.script_editor.action.CodeEditorActionTable;
import com.qxdzbc.p6.ui.script_editor.action.CodeEditorActionTableImp;
import com.qxdzbc.p6.ui.script_editor.action.CodeEditorActionTableImp_Factory;
import com.qxdzbc.p6.ui.script_editor.code_container.CentralScriptContainer;
import com.qxdzbc.p6.ui.script_editor.script_tree.action.ScriptTreeActionImp;
import com.qxdzbc.p6.ui.script_editor.script_tree.action.ScriptTreeActionImp_Factory;
import com.qxdzbc.p6.ui.script_editor.state.CodeEditorState;
import com.qxdzbc.p6.ui.window.action.WindowActionTable;
import com.qxdzbc.p6.ui.window.action.WindowActionTableImp;
import com.qxdzbc.p6.ui.window.action.WindowActionTableImp_Factory;
import com.qxdzbc.p6.ui.window.menu.action.CodeMenuActionImp;
import com.qxdzbc.p6.ui.window.menu.action.CodeMenuActionImp_Factory;
import com.qxdzbc.p6.ui.window.menu.action.FileMenuActionImp;
import com.qxdzbc.p6.ui.window.menu.action.FileMenuActionImp_Factory;
import com.qxdzbc.p6.ui.window.move_to_wb.MoveToWbAction;
import com.qxdzbc.p6.ui.window.move_to_wb.MoveToWbActionImp;
import com.qxdzbc.p6.ui.window.move_to_wb.MoveToWbActionImp_Factory;
import com.qxdzbc.p6.ui.window.state.OuterWindowState;
import com.qxdzbc.p6.ui.window.state.OuterWindowStateFactory;
import com.qxdzbc.p6.ui.window.state.OuterWindowStateFactory_Impl;
import com.qxdzbc.p6.ui.window.state.OuterWindowStateImp_Factory;
import com.qxdzbc.p6.ui.window.state.WindowStateFactory;
import com.qxdzbc.p6.ui.window.state.WindowStateFactory_Impl;
import com.qxdzbc.p6.ui.window.state.WindowStateImp_Factory;
import com.qxdzbc.p6.ui.window.status_bar.StatusBarState;
import com.qxdzbc.p6.ui.window.status_bar.StatusBarStateImp;
import com.qxdzbc.p6.ui.window.status_bar.StatusBarStateImp_Factory;
import com.qxdzbc.p6.ui.window.status_bar.kernel_status.KernelStatusItemState;
import com.qxdzbc.p6.ui.window.status_bar.kernel_status.KernelStatusItemStateImp;
import com.qxdzbc.p6.ui.window.status_bar.kernel_status.KernelStatusItemStateImp_Factory;
import com.qxdzbc.p6.ui.window.status_bar.rpc_status.RPCStatusViewState;
import com.qxdzbc.p6.ui.window.workbook_tab.bar.WorkbookTabBarAction;
import com.qxdzbc.p6.ui.window.workbook_tab.bar.WorkbookTabBarActionImp;
import com.qxdzbc.p6.ui.window.workbook_tab.bar.WorkbookTabBarActionImp_Factory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DelegateFactory;
import dagger.internal.DoubleCheck;
import dagger.internal.InstanceFactory;
import dagger.internal.Preconditions;
import java.nio.file.Path;
import java.util.Map;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import kotlin.jvm.functions.Function1;
import kotlinx.coroutines.CoroutineScope;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class DaggerTestComponent {
  private DaggerTestComponent() {
  }

  public static TestComponent.Builder builder() {
    return new Builder();
  }

  private static final class Builder implements TestComponent.Builder {
    private MessageApiComponent messageApiComponent;

    private String username;

    private CoroutineScope applicationCoroutineScope;

    private ApplicationScope applicationScope;

    @Override
    public Builder messageApiComponent(MessageApiComponent component) {
      this.messageApiComponent = Preconditions.checkNotNull(component);
      return this;
    }

    @Override
    public Builder username(String u) {
      this.username = Preconditions.checkNotNull(u);
      return this;
    }

    @Override
    public Builder applicationCoroutineScope(CoroutineScope scope) {
      this.applicationCoroutineScope = Preconditions.checkNotNull(scope);
      return this;
    }

    @Override
    public Builder applicationScope(ApplicationScope appScope) {
      this.applicationScope = appScope;
      return this;
    }

    @Override
    public TestComponent build() {
      Preconditions.checkBuilderRequirement(messageApiComponent, MessageApiComponent.class);
      Preconditions.checkBuilderRequirement(username, String.class);
      Preconditions.checkBuilderRequirement(applicationCoroutineScope, CoroutineScope.class);
      return new TestComponentImpl(messageApiComponent, username, applicationCoroutineScope, applicationScope);
    }
  }

  private static final class TestComponentImpl implements TestComponent {
    private final ApplicationScope applicationScope;

    private final MessageApiComponent messageApiComponent;

    private final CoroutineScope applicationCoroutineScope;

    private final TestComponentImpl testComponentImpl = this;

    private Provider<MutableState<ErrorContainer>> AppOddityContainerMsProvider;

    private Provider<MutableState<ActiveWindowPointer>> InitActiveWindowPointerProvider;

    private Provider<MutableState<ScriptContainer>> AppScriptContProvider;

    private Provider<MutableState<SelectRectState>> SelectRectStateMsProvider;

    private WorksheetStateImp_Factory worksheetStateImpProvider;

    private Provider<WorksheetStateFactory> worksheetStateFactoryProvider;

    private Provider<GridSliderImp> gridSliderImpProvider;

    private LimitedSlider_Factory limitedSliderProvider;

    private Provider<LimitedGridSliderFactory> limitedGridSliderFactoryProvider;

    private Provider<TreeExtractor> PartialFormulaTreeExtractorProvider;

    private Provider<FormulaBaseVisitor<TextElementResult>> TextElementVisitorProvider;

    private Provider<CellEditorStateImp> cellEditorStateImpProvider;

    private Provider<MutableState<CellEditorState>> CellEditorStateMsProvider;

    private CursorStateImp_Factory cursorStateImpProvider;

    private Provider<CursorStateFactory> cursorStateFactoryProvider;

    private ThumbStateImp_Factory thumbStateImpProvider;

    private Provider<ThumbStateFactory> thumbStateFactoryProvider;

    private WorkbookStateImp_Factory workbookStateImpProvider;

    private Provider<WorkbookStateFactory> workbookStateFactoryProvider;

    private Provider<MutableState<WorkbookStateContainer>> WorkbookStateContMsProvider;

    private Provider<MutableState<CentralScriptContainer>> CentralScriptContainerProvider;

    private Provider<WorkbookContainerImp> workbookContainerImpProvider;

    private Provider<MutableState<WorkbookContainer>> WbContainerProvider;

    private Provider<MutableState<CodeEditorState>> CodeEditorStateProvider;

    private Provider<MessageApiComponent> messageApiComponentProvider;

    private Provider<KernelContext> KernelContextProvider;

    private Provider<MutableState<KernelStatus>> KernelStatusMsProvider;

    private Provider<MsKernelContext> msKernelContextProvider;

    private Provider<KernelStatusItemStateImp> kernelStatusItemStateImpProvider;

    private Provider<MutableState<KernelStatusItemState>> KernelStatusItemStateMsProvider;

    private Provider<MutableState<RPCStatusViewState>> RPCStatusItemStateMsProvider;

    private Provider<StatusBarStateImp> statusBarStateImpProvider;

    private Provider<MutableState<StatusBarState>> StatusBarStateMsProvider;

    private Provider<ColorGenerator> ColorGeneratorProvider;

    private Provider<FormulaColorGeneratorImp> formulaColorGeneratorImpProvider;

    private Provider<FormulaColorGenerator> FormulaColorProvider;

    private WindowStateImp_Factory windowStateImpProvider;

    private Provider<WindowStateFactory> windowStateFactoryProvider;

    private Provider<MutableState<Map<String, MutableState<OuterWindowState>>>> WindowStateMapProvider;

    private OuterWindowStateImp_Factory outerWindowStateImpProvider;

    private Provider<OuterWindowStateFactory> outerWindowStateFactoryProvider;

    private Provider<CellFormatTableImp> cellFormatTableImpProvider;

    private Provider<MutableState<CellFormatTable>> CellFormatTableMsProvider;

    private Provider<SubAppStateContainerImp> subAppStateContainerImpProvider;

    private Provider<MutableState<SubAppStateContainer>> SubAppStateContainerMsProvider;

    private Provider<MutableState<DocumentContainer>> DocumentContainerMsProvider;

    private LazyRange_Factory lazyRangeProvider;

    private Provider<LazyRangeFactory> lazyRangeFactoryProvider;

    private Provider<DocumentContainerImp> documentContainerImpProvider;

    private Provider<MutableState<TranslatorMap>> TranslatorMapMsProvider;

    private Provider<TreeExtractor> TreeExtractorProvider;

    private JvmFormulaTranslator_Factory jvmFormulaTranslatorProvider;

    private Provider<JvmFormulaTranslatorFactory> jvmFormulaTranslatorFactoryProvider;

    private Provider<MutableState<AppState>> appStateMsProvider;

    private Provider<P6FunctionDefinitionsImp> p6FunctionDefinitionsImpProvider;

    private Provider<P6FunctionDefinitions> P6FunctionDefsProvider;

    private Provider<FunctionMap> FunctionMapProvider;

    private Provider<MutableState<FunctionMap>> FunctionMapMsProvider;

    private JvmFormulaVisitor_Factory jvmFormulaVisitorProvider;

    private Provider<JvmFormulaVisitorFactory> jvmFormulaVisitorFactoryProvider;

    private Provider<TranslatorContainerImp> translatorContainerImpProvider;

    private Provider<MutableState<TranslatorContainer>> TranslatorContainerMsProvider;

    private Provider<AppStateImp> appStateImpProvider;

    private Provider<StateContainerImp> stateContainerImpProvider;

    private Provider<MutableState<StateContainer>> StateContainerMsProvider;

    private Provider<RestoreWindowFocusStateImp> restoreWindowFocusStateImpProvider;

    private Provider<State<StateContainer>> StateContainerStProvider;

    private Provider<MoveToWbActionImp> moveToWbActionImpProvider;

    private Provider<ErrorRouterImp> errorRouterImpProvider;

    private Provider<ErrorRouter> ErrorRouterProvider;

    private Provider<NewWorksheetActionImp> newWorksheetActionImpProvider;

    private Provider<DeleteWorksheetRMImp> deleteWorksheetRMImpProvider;

    private Provider<DeleteWorksheetApplierImp> deleteWorksheetApplierImpProvider;

    private Provider<DeleteWorksheetActionImp> deleteWorksheetActionImpProvider;

    private Provider<RenameWorksheetRMImp> renameWorksheetRMImpProvider;

    private Provider<RenameWorksheetInternalApplierImp> renameWorksheetInternalApplierImpProvider;

    private Provider<P6EventTable> P6EventTableProvider;

    private Provider<ErrorApplierImp> errorApplierImpProvider;

    private Provider<BaseApplierImp> baseApplierImpProvider;

    private Provider<RenameWorksheetApplierImp> renameWorksheetApplierImpProvider;

    private Provider<RenameWorksheetActionImp> renameWorksheetActionImpProvider;

    private Provider<SetActiveWorksheetRMImp> setActiveWorksheetRMImpProvider;

    private Provider<SetActiveWorksheetApplierImp> setActiveWorksheetApplierImpProvider;

    private Provider<SetActiveWorksheetActionImp> setActiveWorksheetActionImpProvider;

    private Provider<SwitchWorksheetActionImp> switchWorksheetActionImpProvider;

    private Provider<WorkbookActionImp> workbookActionImpProvider;

    private Provider<RangeAddressFormatterImp> rangeAddressFormatterImpProvider;

    private Provider<MakeCellEditorTextActionImp> makeCellEditorTextActionImpProvider;

    private Provider<BuildAnnotatedTextActionImp> buildAnnotatedTextActionImpProvider;

    private Provider<ColorFormulaInCellEditorActionImp> colorFormulaInCellEditorActionImpProvider;

    private Provider<UpdateRangeSelectorTextImp> updateRangeSelectorTextImpProvider;

    private Provider<CellLiteralParserImp> cellLiteralParserImpProvider;

    private Provider<CellRMImp> cellRMImpProvider;

    private Provider<State<TranslatorContainer>> TranslatorContainerStProvider;

    private Provider<UpdateCellActionImp> updateCellActionImpProvider;

    private Provider<CloseCellEditorActionImp> closeCellEditorActionImpProvider;

    private Provider<RunFormulaOrSaveValueToCellActionImp> runFormulaOrSaveValueToCellActionImpProvider;

    private Provider<ClickOnCellActionImp> clickOnCellActionImpProvider;

    private Provider<MouseOnWorksheetActionImp> mouseOnWorksheetActionImpProvider;

    private Provider<ComputeSliderSizeActionImp> computeSliderSizeActionImpProvider;

    private Provider<MakeSliderFollowCellActionImp> makeSliderFollowCellActionImpProvider;

    private Provider<WorksheetAction2Imp> worksheetAction2ImpProvider;

    private Provider<BinaryCopier> BinaryCopierProvider;

    private Provider<RangeCopierImp> rangeCopierImpProvider;

    private Provider<CopyRangeToClipboardRMImp> copyRangeToClipboardRMImpProvider;

    private Provider<RangeToClipboardInternalApplierImp> rangeToClipboardInternalApplierImpProvider;

    private Provider<RangeToClipboardApplierImp> rangeToClipboardApplierImpProvider;

    private Provider<RangeToClipboardActionImp> rangeToClipboardActionImpProvider;

    private Provider<DeleteMultiRMImp> deleteMultiRMImpProvider;

    private Provider<DeleteMultiApplierImp> deleteMultiApplierImpProvider;

    private Provider<CoroutineScope> applicationCoroutineScopeProvider;

    private Provider<MultiCellUpdateActionImp> multiCellUpdateActionImpProvider;

    private Provider<DeleteMultiCellActionImp> deleteMultiCellActionImpProvider;

    private Provider<WorksheetActionImp> worksheetActionImpProvider;

    private Provider<SingleCellPaster> singleCellPasterProvider;

    private Provider<RangeRangePasterImp> rangeRangePasterImpProvider;

    private Provider<RangePasterImp> rangePasterImpProvider;

    private Provider<PasteRangeRMImp> pasteRangeRMImpProvider;

    private Provider<WorkbookUpdateCommonApplierImp> workbookUpdateCommonApplierImpProvider;

    private Provider<PasteRangeApplierImp> pasteRangeApplierImpProvider;

    private Provider<PasteRangeActionImp> pasteRangeActionImpProvider;

    private Provider<PasteRangeToCursorImp> pasteRangeToCursorImpProvider;

    private Provider<SelectWholeColumnForAllSelectedCellActionImp> selectWholeColumnForAllSelectedCellActionImpProvider;

    private Provider<SelectWholeRowForAllSelectedCellActionImp> selectWholeRowForAllSelectedCellActionImpProvider;

    private Provider<OpenCellEditorImp> openCellEditorImpProvider;

    private Provider<CopyCursorRangeToClipboardActionImp> copyCursorRangeToClipboardActionImpProvider;

    private Provider<UndoOnCursorActionImp> undoOnCursorActionImpProvider;

    private Provider<CellEditorActionImp> cellEditorActionImpProvider;

    private Provider<HandleCursorKeyboardEventActionImp> handleCursorKeyboardEventActionImpProvider;

    private Provider<TextDifferImp> textDifferImpProvider;

    private Provider<PartialTextElementTranslator> partialTextElementTranslatorProvider;

    private Provider<P6Translator<TextElementResult>> PartialCellRangeExtractorProvider;

    private Provider<CycleFormulaLockStateImp> cycleFormulaLockStateImpProvider;

    private Provider<CopyCellActionImp> copyCellActionImpProvider;

    private Provider<EndThumbDragActionImp> endThumbDragActionImpProvider;

    private Provider<DragThumbActionImp> dragThumbActionImpProvider;

    private Provider<ThumbActionImp> thumbActionImpProvider;

    private Provider<CursorActionImp> cursorActionImpProvider;

    private Provider<CellRpcActionsImp> cellRpcActionsImpProvider;

    private Provider<CellRpcService> cellRpcServiceProvider;

    private Provider<CreateNewWorksheetRMImp> createNewWorksheetRMImpProvider;

    private Provider<CreateNewWorksheetApplierImp> createNewWorksheetApplierImpProvider;

    private Provider<CreateNewWorksheetActionImp> createNewWorksheetActionImpProvider;

    private Provider<ReplaceWorkbookKeyActionImp> replaceWorkbookKeyActionImpProvider;

    private Provider<WsNameGeneratorImp> wsNameGeneratorImpProvider;

    private Provider<AutoNameWbFactory> autoNameWbFactoryProvider;

    private Provider<CreateNewWbRMImp> createNewWbRMImpProvider;

    private Provider<PickDefaultActiveWbActionImp> pickDefaultActiveWbActionImpProvider;

    private Provider<CreateNewWorkbookApplierImp> createNewWorkbookApplierImpProvider;

    private Provider<CreateNewWorkbookActionImp> createNewWorkbookActionImpProvider;

    private Provider<SetActiveWorkbookActionImp> setActiveWorkbookActionImpProvider;

    private Provider<P6SaverImp> p6SaverImpProvider;

    private Provider<SaveWorkbookActionImp> saveWorkbookActionImpProvider;

    private Provider<Function1<Path, byte[]>> readFileFunctionProvider;

    private Provider<P6FileLoaderImp> p6FileLoaderImpProvider;

    private Provider<LoadWorkbookRMImp> loadWorkbookRMImpProvider;

    private Provider<LoadWorkbookInternalApplierImp> loadWorkbookInternalApplierImpProvider;

    private Provider<LoadWorkbookApplierImp> loadWorkbookApplierImpProvider;

    private Provider<LoadWorkbookActionImp> loadWorkbookActionImpProvider;

    private Provider<CloseWorkbookRMImp> closeWorkbookRMImpProvider;

    private Provider<CloseWorkbookInternalApplierImp> closeWorkbookInternalApplierImpProvider;

    private Provider<CloseWorkbookApplierImp> closeWorkbookApplierImpProvider;

    private Provider<CloseWorkbookActionImp> closeWorkbookActionImpProvider;

    private Provider<GetWorkbookActionImp> getWorkbookActionImpProvider;

    private Provider<RemoveAllWorksheetActionImp> removeAllWorksheetActionImpProvider;

    private Provider<WorkbookRpcActionsImp> workbookRpcActionsImpProvider;

    private Provider<WorkbookRpcService> workbookRpcServiceProvider;

    private Provider<AppRpcActionImp> appRpcActionImpProvider;

    private Provider<AppRpcService> appRpcServiceProvider;

    private Provider<LoadDataActionImp> loadDataActionImpProvider;

    private Provider<RemoveAllCellActionImp> removeAllCellActionImpProvider;

    private Provider<WorksheetRpcActionImp> worksheetRpcActionImpProvider;

    private Provider<WorksheetRpcService> worksheetRpcServiceProvider;

    private Provider<P6RpcServerImp> p6RpcServerImpProvider;

    private Provider<MsP6RpcServer> msP6RpcServerProvider;

    private Provider<String> usernameProvider;

    private Provider<KernelContextReadOnly> KernelContextReadOnlyProvider;

    private Provider<PythonCodeRunner> pythonCodeRunnerProvider;

    private Provider<CodeRunner> CodeRunnerProvider;

    private Provider<ScriptEditorErrorRouterImp> scriptEditorErrorRouterImpProvider;

    private Provider<ScriptEditorErrorRouter> ScriptEditorErrorRouterProvider;

    private Provider<NewScriptRMImp> newScriptRMImpProvider;

    private Provider<ScriptRMImp> scriptRMImpProvider;

    private Provider<NewScriptApplierImp> newScriptApplierImpProvider;

    private Provider<ScriptChangeApplierImp> scriptChangeApplierImpProvider;

    private Provider<ScriptApplierImp> scriptApplierImpProvider;

    private Provider<CodeEditorActionImp> codeEditorActionImpProvider;

    private Provider<ScriptTreeActionImp> scriptTreeActionImpProvider;

    private Provider<CodeEditorActionTableImp> codeEditorActionTableImpProvider;

    private Provider<ApplicationScope> applicationScopeProvider;

    private Provider<AppActionImp> appActionImpProvider;

    private Provider<AppAction> AppActionProvider;

    private Provider<AppActionTableImp> appActionTableImpProvider;

    private Provider<KernelServiceManager> KernelServiceManagerProvider;

    private Provider<Gson> gsonProvider;

    private Provider<FileUtil> FileUtilProvider;

    private Provider<RestartKernelRMImp> restartKernelRMImpProvider;

    private Provider<AppRMImp> appRMImpProvider;

    private Provider<RestartKernelApplierImp> restartKernelApplierImpProvider;

    private Provider<KernelActionImp> kernelActionImpProvider;

    private Provider<KernelAction> KernelActionProvider;

    private Provider<SetActiveWindowActionImp> setActiveWindowActionImpProvider;

    private Provider<CloseWindowActionImp> closeWindowActionImpProvider;

    private Provider<MakeSavePathImp> makeSavePathImpProvider;

    private Provider<WindowActionImp> windowActionImpProvider;

    private Provider<FileMenuActionImp> fileMenuActionImpProvider;

    private Provider<RulerActionImp> rulerActionImpProvider;

    private Provider<WorksheetActionTableImp> worksheetActionTableImpProvider;

    private Provider<WorkbookActionTableImp> workbookActionTableImpProvider;

    private Provider<WorkbookTabBarActionImp> workbookTabBarActionImpProvider;

    private Provider<CodeMenuActionImp> codeMenuActionImpProvider;

    private Provider<WindowActionTableImp> windowActionTableImpProvider;

    private Provider<PythonCommanderImp> pythonCommanderImpProvider;

    private Provider<PythonCommander> BackEndCommanderProvider;

    private Provider<AppContextImp> appContextImpProvider;

    private Provider<AppContext> AppContextProvider;

    private Provider<ZContext> ZContextProvider;

    private Provider<Integer> eventServerPortProvider;

    private Provider<ZMQ.Socket> eventServerSocketProvider;

    private Provider<UpdateCellFormatActionImp> updateCellFormatActionImpProvider;

    private TestComponentImpl(MessageApiComponent messageApiComponentParam, String usernameParam,
        CoroutineScope applicationCoroutineScopeParam, ApplicationScope applicationScopeParam) {
      this.applicationScope = applicationScopeParam;
      this.messageApiComponent = messageApiComponentParam;
      this.applicationCoroutineScope = applicationCoroutineScopeParam;
      initialize(messageApiComponentParam, usernameParam, applicationCoroutineScopeParam, applicationScopeParam);
      initialize2(messageApiComponentParam, usernameParam, applicationCoroutineScopeParam, applicationScopeParam);
      initialize3(messageApiComponentParam, usernameParam, applicationCoroutineScopeParam, applicationScopeParam);

    }

    private UpdateCellActionImp updateCellActionImp() {
      return new UpdateCellActionImp(cellRMImpProvider.get(), StateContainerStProvider.get(), TranslatorContainerStProvider.get(), ErrorRouterProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final MessageApiComponent messageApiComponentParam,
        final String usernameParam, final CoroutineScope applicationCoroutineScopeParam,
        final ApplicationScope applicationScopeParam) {
      this.AppOddityContainerMsProvider = DoubleCheck.provider(AppStateModule_Companion_AppOddityContainerMsFactory.create());
      this.InitActiveWindowPointerProvider = DoubleCheck.provider(AppStateModule_Companion_InitActiveWindowPointerFactory.create());
      this.AppScriptContProvider = DoubleCheck.provider(AppStateModule_Companion_AppScriptContFactory.create());
      this.SelectRectStateMsProvider = WorksheetStateModule_Companion_SelectRectStateMsFactory.create(WorksheetStateModule_Companion_SelectRectStateFactory.create());
      this.worksheetStateImpProvider = WorksheetStateImp_Factory.create(WorksheetStateModule_Companion_DefaultLayoutCoorMsFactory.create(), WorksheetStateModule_Companion_DefaultLayoutCoorMsFactory.create(), WorksheetStateModule_Companion_DefaultCellStateContainerFactory.create(), SelectRectStateMsProvider, WorksheetStateModule_Companion_ResizeColBarStateMsFactory.create(), WorksheetStateModule_Companion_ResizeRowBarStateMsFactory.create(), P6Module_Companion_DefaultColRangeFactory.create(), P6Module_Companion_DefaultRowRangeFactory.create());
      this.worksheetStateFactoryProvider = WorksheetStateFactory_Impl.create(worksheetStateImpProvider);
      this.gridSliderImpProvider = GridSliderImp_Factory.create(WorksheetStateModule_Companion_DefaultVisibleColRangeFactory.create(), WorksheetStateModule_Companion_DefaultVisibleRowRangeFactory.create(), P6Module_Companion_NullIntFactory.create(), P6Module_Companion_NullIntFactory.create());
      this.limitedSliderProvider = LimitedSlider_Factory.create(((Provider) gridSliderImpProvider), P6Module_Companion_DefaultColRangeFactory.create(), P6Module_Companion_DefaultRowRangeFactory.create());
      this.limitedGridSliderFactoryProvider = LimitedGridSliderFactory_Impl.create(limitedSliderProvider);
      this.PartialFormulaTreeExtractorProvider = DoubleCheck.provider((Provider) PartialFormulaTreeExtractor_Factory.create());
      this.TextElementVisitorProvider = DoubleCheck.provider((Provider) TextElementVisitor_Factory.create());
      this.cellEditorStateImpProvider = CellEditorStateImp_Factory.create(PartialFormulaTreeExtractorProvider, TextElementVisitorProvider);
      this.CellEditorStateMsProvider = DoubleCheck.provider(AppStateModule_Companion_CellEditorStateMsFactory.create(((Provider) cellEditorStateImpProvider)));
      this.cursorStateImpProvider = CursorStateImp_Factory.create(CellEditorStateMsProvider, WorksheetStateModule_Companion_NullRangeAddressFactory.create(), WorksheetStateModule_Companion_EcSetFactory.create(), WorksheetStateModule_Companion_ErSetFactory.create(), WorksheetStateModule_Companion_DefaultRangeConstraintFactory.create(), WorksheetStateModule_Companion_ClipboardRangeFactory.create());
      this.cursorStateFactoryProvider = CursorStateFactory_Impl.create(cursorStateImpProvider);
      this.thumbStateImpProvider = ThumbStateImp_Factory.create(WorksheetStateModule_Companion_SelectRectStateFactory.create());
      this.thumbStateFactoryProvider = ThumbStateFactory_Impl.create(thumbStateImpProvider);
      this.workbookStateImpProvider = WorkbookStateImp_Factory.create(WorkbookStateModule_Companion_ZFactory.create(), WorksheetStateModule_Companion_DefaultActiveWorksheetPointerFactory.create(), P6Module_Companion_BTrueFactory.create(), P6Module_Companion_BTrueFactory.create(), WorkbookStateModule_Companion_DFactory.create(), worksheetStateFactoryProvider, limitedGridSliderFactoryProvider, cursorStateFactoryProvider, thumbStateFactoryProvider, WorkbookStateModule_Companion_DefaultScriptContMsFactory.create());
      this.workbookStateFactoryProvider = WorkbookStateFactory_Impl.create(workbookStateImpProvider);
      this.WorkbookStateContMsProvider = DoubleCheck.provider(AppStateModule_Companion_WorkbookStateContMsFactory.create(workbookStateFactoryProvider));
      this.CentralScriptContainerProvider = DoubleCheck.provider(AppStateModule_Companion_CentralScriptContainerFactory.create(AppScriptContProvider, WorkbookStateContMsProvider));
      this.workbookContainerImpProvider = WorkbookContainerImp_Factory.create(WorkbookStateContMsProvider, workbookStateFactoryProvider);
      this.WbContainerProvider = DoubleCheck.provider(AppStateModule_Companion_WbContainerFactory.create(workbookContainerImpProvider));
      this.CodeEditorStateProvider = DoubleCheck.provider(AppStateModule_Companion_CodeEditorStateFactory.create(WbContainerProvider, CentralScriptContainerProvider));
      this.messageApiComponentProvider = InstanceFactory.create(messageApiComponentParam);
      this.KernelContextProvider = DoubleCheck.provider(MsgApiModule_Companion_KernelContextFactory.create(messageApiComponentProvider));
      this.KernelStatusMsProvider = DoubleCheck.provider(AppStateModule_Companion_KernelStatusMsFactory.create(KernelContextProvider));
      this.msKernelContextProvider = DoubleCheck.provider(MsKernelContext_Factory.create(KernelContextProvider, KernelStatusMsProvider));
      this.kernelStatusItemStateImpProvider = KernelStatusItemStateImp_Factory.create(P6Module_Companion_BFalseFactory.create(), ((Provider) msKernelContextProvider), KernelStatusMsProvider);
      this.KernelStatusItemStateMsProvider = DoubleCheck.provider(StatusBarModule_Companion_KernelStatusItemStateMsFactory.create(((Provider) kernelStatusItemStateImpProvider)));
      this.RPCStatusItemStateMsProvider = DoubleCheck.provider(StatusBarModule_Companion_RPCStatusItemStateMsFactory.create());
      this.statusBarStateImpProvider = StatusBarStateImp_Factory.create(KernelStatusItemStateMsProvider, RPCStatusItemStateMsProvider);
      this.StatusBarStateMsProvider = DoubleCheck.provider(StatusBarModule_Companion_StatusBarStateMsFactory.create(((Provider) statusBarStateImpProvider)));
      this.ColorGeneratorProvider = DoubleCheck.provider((Provider) RandomColorGenerator_Factory.create());
      this.formulaColorGeneratorImpProvider = FormulaColorGeneratorImp_Factory.create(ColorGeneratorProvider);
      this.FormulaColorProvider = DoubleCheck.provider((Provider) formulaColorGeneratorImpProvider);
      this.windowStateImpProvider = WindowStateImp_Factory.create(WbContainerProvider, WorkbookStateContMsProvider, StatusBarStateMsProvider, ((Provider) msKernelContextProvider), workbookStateFactoryProvider, WindowStateModuleForTest_Companion_FocusStateMsFactory.create(), FormulaColorProvider);
      this.windowStateFactoryProvider = WindowStateFactory_Impl.create(windowStateImpProvider);
      this.WindowStateMapProvider = DoubleCheck.provider(AppStateModule_Companion_WindowStateMapFactory.create());
      this.outerWindowStateImpProvider = OuterWindowStateImp_Factory.create();
      this.outerWindowStateFactoryProvider = OuterWindowStateFactory_Impl.create(outerWindowStateImpProvider);
      this.cellFormatTableImpProvider = CellFormatTableImp_Factory.create(CellFormatTableModule_Companion_FloatFormatTableFactory.create(), CellFormatTableModule_Companion_ColorTableFactory.create(), CellFormatTableModule_Companion_BoolFormatTableFactory.create(), CellFormatTableModule_Companion_AlignmentFormatTableFactory.create());
      this.CellFormatTableMsProvider = DoubleCheck.provider(CellFormatTableModule_Companion_CellFormatTableMsFactory.create(cellFormatTableImpProvider));
      this.subAppStateContainerImpProvider = SubAppStateContainerImp_Factory.create(WindowStateMapProvider, WorkbookStateContMsProvider, windowStateFactoryProvider, outerWindowStateFactoryProvider, workbookStateFactoryProvider, CellFormatTableMsProvider);
      this.SubAppStateContainerMsProvider = DoubleCheck.provider(AppStateModule_Companion_SubAppStateContainerMsFactory.create(((Provider) subAppStateContainerImpProvider)));
      this.DocumentContainerMsProvider = new DelegateFactory<>();
      this.lazyRangeProvider = LazyRange_Factory.create(DocumentContainerMsProvider);
      this.lazyRangeFactoryProvider = LazyRangeFactory_Impl.create(lazyRangeProvider);
      this.documentContainerImpProvider = DocumentContainerImp_Factory.create(WbContainerProvider, lazyRangeFactoryProvider);
      DelegateFactory.setDelegate(DocumentContainerMsProvider, DoubleCheck.provider(AppStateModule_Companion_DocumentContainerMsFactory.create(((Provider) documentContainerImpProvider))));
      this.TranslatorMapMsProvider = DoubleCheck.provider(AppStateModule_Companion_TranslatorMapMsFactory.create());
      this.TreeExtractorProvider = DoubleCheck.provider((Provider) TreeExtractorImp_Factory.create());
      this.jvmFormulaTranslatorProvider = JvmFormulaTranslator_Factory.create(TreeExtractorProvider);
      this.jvmFormulaTranslatorFactoryProvider = JvmFormulaTranslatorFactory_Impl.create(jvmFormulaTranslatorProvider);
      this.appStateMsProvider = new DelegateFactory<>();
      this.p6FunctionDefinitionsImpProvider = P6FunctionDefinitionsImp_Factory.create(appStateMsProvider, ((Provider) DocumentContainerMsProvider));
      this.P6FunctionDefsProvider = DoubleCheck.provider((Provider) p6FunctionDefinitionsImpProvider);
      this.FunctionMapProvider = TranslatorModule_Companion_FunctionMapFactory.create(P6FunctionDefsProvider);
      this.FunctionMapMsProvider = DoubleCheck.provider(TranslatorModule_Companion_FunctionMapMsFactory.create(FunctionMapProvider));
      this.jvmFormulaVisitorProvider = JvmFormulaVisitor_Factory.create(FunctionMapMsProvider, ((Provider) DocumentContainerMsProvider));
      this.jvmFormulaVisitorFactoryProvider = JvmFormulaVisitorFactory_Impl.create(jvmFormulaVisitorProvider);
      this.translatorContainerImpProvider = TranslatorContainerImp_Factory.create(TranslatorMapMsProvider, AppStateModule_Companion_InitSingleTranslatorMapFactory.create(), jvmFormulaTranslatorFactoryProvider, jvmFormulaVisitorFactoryProvider);
      this.TranslatorContainerMsProvider = DoubleCheck.provider(AppStateModule_Companion_TranslatorContainerMsFactory.create(((Provider) translatorContainerImpProvider)));
      this.appStateImpProvider = AppStateImp_Factory.create(P6Module_Companion_BFalseFactory.create(), AppOddityContainerMsProvider, InitActiveWindowPointerProvider, AppScriptContProvider, CentralScriptContainerProvider, CodeEditorStateProvider, windowStateFactoryProvider, workbookStateFactoryProvider, SubAppStateContainerMsProvider, DocumentContainerMsProvider, TranslatorContainerMsProvider, CellEditorStateMsProvider);
      DelegateFactory.setDelegate(appStateMsProvider, DoubleCheck.provider(AppStateModule_Companion_AppStateMsFactory.create(appStateImpProvider)));
      this.stateContainerImpProvider = StateContainerImp_Factory.create(appStateMsProvider, DocumentContainerMsProvider, SubAppStateContainerMsProvider);
      this.StateContainerMsProvider = DoubleCheck.provider(AppStateModule_Companion_StateContainerMsFactory.create(stateContainerImpProvider));
      this.restoreWindowFocusStateImpProvider = DoubleCheck.provider(RestoreWindowFocusStateImp_Factory.create(StateContainerMsProvider, CellEditorStateMsProvider));
      this.StateContainerStProvider = DoubleCheck.provider(AppStateModule_Companion_StateContainerStFactory.create(StateContainerMsProvider));
      this.moveToWbActionImpProvider = DoubleCheck.provider(MoveToWbActionImp_Factory.create(((Provider) restoreWindowFocusStateImpProvider), StateContainerStProvider, InitActiveWindowPointerProvider));
      this.errorRouterImpProvider = ErrorRouterImp_Factory.create(StateContainerMsProvider, CodeEditorStateProvider, AppOddityContainerMsProvider);
      this.ErrorRouterProvider = DoubleCheck.provider((Provider) errorRouterImpProvider);
      this.newWorksheetActionImpProvider = DoubleCheck.provider(NewWorksheetActionImp_Factory.create(ErrorRouterProvider, appStateMsProvider, StateContainerMsProvider, DocumentContainerMsProvider));
      this.deleteWorksheetRMImpProvider = DoubleCheck.provider(DeleteWorksheetRMImp_Factory.create(DocumentContainerMsProvider));
      this.deleteWorksheetApplierImpProvider = DoubleCheck.provider(DeleteWorksheetApplierImp_Factory.create(DocumentContainerMsProvider, SubAppStateContainerMsProvider, TranslatorContainerMsProvider));
      this.deleteWorksheetActionImpProvider = DoubleCheck.provider(DeleteWorksheetActionImp_Factory.create(((Provider) deleteWorksheetRMImpProvider), ((Provider) deleteWorksheetApplierImpProvider), DocumentContainerMsProvider));
      this.renameWorksheetRMImpProvider = DoubleCheck.provider(RenameWorksheetRMImp_Factory.create());
      this.renameWorksheetInternalApplierImpProvider = DoubleCheck.provider(RenameWorksheetInternalApplierImp_Factory.create(appStateMsProvider, DocumentContainerMsProvider, ErrorRouterProvider));
      this.P6EventTableProvider = DoubleCheck.provider(P6Module_Companion_P6EventTableFactory.create());
      this.errorApplierImpProvider = DoubleCheck.provider(ErrorApplierImp_Factory.create(ErrorRouterProvider, P6EventTableProvider));
      this.baseApplierImpProvider = DoubleCheck.provider(BaseApplierImp_Factory.create(((Provider) errorApplierImpProvider), ErrorRouterProvider));
      this.renameWorksheetApplierImpProvider = DoubleCheck.provider(RenameWorksheetApplierImp_Factory.create(((Provider) renameWorksheetInternalApplierImpProvider), ((Provider) baseApplierImpProvider)));
      this.renameWorksheetActionImpProvider = DoubleCheck.provider(RenameWorksheetActionImp_Factory.create(((Provider) renameWorksheetRMImpProvider), ((Provider) renameWorksheetApplierImpProvider), SubAppStateContainerMsProvider));
      this.setActiveWorksheetRMImpProvider = DoubleCheck.provider(SetActiveWorksheetRMImp_Factory.create(SubAppStateContainerMsProvider, InitActiveWindowPointerProvider, DocumentContainerMsProvider));
      this.setActiveWorksheetApplierImpProvider = DoubleCheck.provider(SetActiveWorksheetApplierImp_Factory.create(ErrorRouterProvider, SubAppStateContainerMsProvider, InitActiveWindowPointerProvider));
      this.setActiveWorksheetActionImpProvider = DoubleCheck.provider(SetActiveWorksheetActionImp_Factory.create(((Provider) setActiveWorksheetRMImpProvider), ((Provider) setActiveWorksheetApplierImpProvider)));
      this.switchWorksheetActionImpProvider = DoubleCheck.provider(SwitchWorksheetActionImp_Factory.create(((Provider) setActiveWorksheetActionImpProvider), ((Provider) restoreWindowFocusStateImpProvider), appStateMsProvider, SubAppStateContainerMsProvider));
      this.workbookActionImpProvider = DoubleCheck.provider(WorkbookActionImp_Factory.create(((Provider) newWorksheetActionImpProvider), ((Provider) deleteWorksheetActionImpProvider), ((Provider) renameWorksheetActionImpProvider), ((Provider) setActiveWorksheetActionImpProvider), ((Provider) restoreWindowFocusStateImpProvider), ((Provider) switchWorksheetActionImpProvider)));
      this.rangeAddressFormatterImpProvider = DoubleCheck.provider(RangeAddressFormatterImp_Factory.create());
      this.makeCellEditorTextActionImpProvider = DoubleCheck.provider(MakeCellEditorTextActionImp_Factory.create(StateContainerStProvider, ((Provider) rangeAddressFormatterImpProvider)));
      this.buildAnnotatedTextActionImpProvider = DoubleCheck.provider(BuildAnnotatedTextActionImp_Factory.create());
      this.colorFormulaInCellEditorActionImpProvider = DoubleCheck.provider(ColorFormulaInCellEditorActionImp_Factory.create(StateContainerStProvider, FormulaColorProvider, ((Provider) buildAnnotatedTextActionImpProvider)));
      this.updateRangeSelectorTextImpProvider = DoubleCheck.provider(UpdateRangeSelectorTextImp_Factory.create(CellEditorStateMsProvider, ((Provider) makeCellEditorTextActionImpProvider), TreeExtractorProvider, ((Provider) colorFormulaInCellEditorActionImpProvider)));
      this.cellLiteralParserImpProvider = DoubleCheck.provider(CellLiteralParserImp_Factory.create());
      this.cellRMImpProvider = DoubleCheck.provider(CellRMImp_Factory.create());
      this.TranslatorContainerStProvider = DoubleCheck.provider(AppStateModule_Companion_TranslatorContainerStFactory.create(TranslatorContainerMsProvider));
      this.updateCellActionImpProvider = UpdateCellActionImp_Factory.create(((Provider) cellRMImpProvider), StateContainerStProvider, TranslatorContainerStProvider, ErrorRouterProvider);
      this.closeCellEditorActionImpProvider = DoubleCheck.provider(CloseCellEditorActionImp_Factory.create(StateContainerMsProvider, PartialFormulaTreeExtractorProvider));
      this.runFormulaOrSaveValueToCellActionImpProvider = DoubleCheck.provider(RunFormulaOrSaveValueToCellActionImp_Factory.create(((Provider) cellLiteralParserImpProvider), ((Provider) updateCellActionImpProvider), StateContainerMsProvider, PartialFormulaTreeExtractorProvider, ((Provider) closeCellEditorActionImpProvider)));
      this.clickOnCellActionImpProvider = DoubleCheck.provider(ClickOnCellActionImp_Factory.create(appStateMsProvider, ((Provider) SubAppStateContainerMsProvider), ((Provider) restoreWindowFocusStateImpProvider), ((Provider) updateRangeSelectorTextImpProvider), ((Provider) runFormulaOrSaveValueToCellActionImpProvider)));
    }

    @SuppressWarnings("unchecked")
    private void initialize2(final MessageApiComponent messageApiComponentParam,
        final String usernameParam, final CoroutineScope applicationCoroutineScopeParam,
        final ApplicationScope applicationScopeParam) {
      this.mouseOnWorksheetActionImpProvider = DoubleCheck.provider(MouseOnWorksheetActionImp_Factory.create(StateContainerMsProvider, ((Provider) clickOnCellActionImpProvider), ((Provider) makeCellEditorTextActionImpProvider), ((Provider) updateRangeSelectorTextImpProvider), CellEditorStateMsProvider));
      this.computeSliderSizeActionImpProvider = DoubleCheck.provider(ComputeSliderSizeActionImp_Factory.create(StateContainerStProvider));
      this.makeSliderFollowCellActionImpProvider = DoubleCheck.provider(MakeSliderFollowCellActionImp_Factory.create(StateContainerStProvider, ErrorRouterProvider));
      this.worksheetAction2ImpProvider = DoubleCheck.provider(WorksheetAction2Imp_Factory.create(((Provider) mouseOnWorksheetActionImpProvider), StateContainerMsProvider, ((Provider) computeSliderSizeActionImpProvider), ((Provider) makeSliderFollowCellActionImpProvider)));
      this.BinaryCopierProvider = DoubleCheck.provider(UtilModule_Companion_BinaryCopierFactory.create());
      this.rangeCopierImpProvider = DoubleCheck.provider(RangeCopierImp_Factory.create(BinaryCopierProvider));
      this.copyRangeToClipboardRMImpProvider = DoubleCheck.provider(CopyRangeToClipboardRMImp_Factory.create(((Provider) rangeCopierImpProvider), SubAppStateContainerMsProvider, DocumentContainerMsProvider));
      this.rangeToClipboardInternalApplierImpProvider = DoubleCheck.provider(RangeToClipboardInternalApplierImp_Factory.create(StateContainerMsProvider));
      this.rangeToClipboardApplierImpProvider = DoubleCheck.provider(RangeToClipboardApplierImp_Factory.create(((Provider) rangeToClipboardInternalApplierImpProvider), ((Provider) baseApplierImpProvider), ErrorRouterProvider));
      this.rangeToClipboardActionImpProvider = DoubleCheck.provider(RangeToClipboardActionImp_Factory.create(((Provider) copyRangeToClipboardRMImpProvider), ((Provider) rangeToClipboardApplierImpProvider)));
      this.deleteMultiRMImpProvider = DoubleCheck.provider(DeleteMultiRMImp_Factory.create(appStateMsProvider, StateContainerStProvider));
      this.deleteMultiApplierImpProvider = DoubleCheck.provider(DeleteMultiApplierImp_Factory.create(SubAppStateContainerMsProvider, ErrorRouterProvider, WbContainerProvider));
      this.applicationCoroutineScopeProvider = InstanceFactory.create(applicationCoroutineScopeParam);
      this.multiCellUpdateActionImpProvider = DoubleCheck.provider(MultiCellUpdateActionImp_Factory.create(StateContainerStProvider, TranslatorContainerStProvider, ErrorRouterProvider, applicationCoroutineScopeProvider));
      this.deleteMultiCellActionImpProvider = DoubleCheck.provider(DeleteMultiCellActionImp_Factory.create(((Provider) deleteMultiRMImpProvider), ((Provider) deleteMultiApplierImpProvider), appStateMsProvider, DocumentContainerMsProvider, ((Provider) multiCellUpdateActionImpProvider), SubAppStateContainerMsProvider));
      this.worksheetActionImpProvider = DoubleCheck.provider(WorksheetActionImp_Factory.create(((Provider) worksheetAction2ImpProvider), ((Provider) rangeToClipboardActionImpProvider), ((Provider) deleteMultiCellActionImpProvider), ((Provider) restoreWindowFocusStateImpProvider)));
      this.singleCellPasterProvider = SingleCellPaster_Factory.create(StateContainerStProvider, TranslatorContainerMsProvider);
      this.rangeRangePasterImpProvider = RangeRangePasterImp_Factory.create(StateContainerStProvider, TranslatorContainerMsProvider);
      this.rangePasterImpProvider = DoubleCheck.provider(RangePasterImp_Factory.create(singleCellPasterProvider, rangeRangePasterImpProvider));
      this.pasteRangeRMImpProvider = DoubleCheck.provider(PasteRangeRMImp_Factory.create(StateContainerStProvider, ((Provider) rangePasterImpProvider)));
      this.workbookUpdateCommonApplierImpProvider = DoubleCheck.provider(WorkbookUpdateCommonApplierImp_Factory.create(StateContainerMsProvider, ((Provider) baseApplierImpProvider)));
      this.pasteRangeApplierImpProvider = DoubleCheck.provider(PasteRangeApplierImp_Factory.create(((Provider) workbookUpdateCommonApplierImpProvider), SubAppStateContainerMsProvider, DocumentContainerMsProvider));
      this.pasteRangeActionImpProvider = DoubleCheck.provider(PasteRangeActionImp_Factory.create(StateContainerStProvider, ((Provider) pasteRangeRMImpProvider), ((Provider) pasteRangeApplierImpProvider)));
      this.pasteRangeToCursorImpProvider = DoubleCheck.provider(PasteRangeToCursorImp_Factory.create(StateContainerStProvider, ((Provider) pasteRangeActionImpProvider)));
      this.selectWholeColumnForAllSelectedCellActionImpProvider = DoubleCheck.provider(SelectWholeColumnForAllSelectedCellActionImp_Factory.create(StateContainerStProvider));
      this.selectWholeRowForAllSelectedCellActionImpProvider = DoubleCheck.provider(SelectWholeRowForAllSelectedCellActionImp_Factory.create(StateContainerStProvider));
      this.openCellEditorImpProvider = DoubleCheck.provider(OpenCellEditorImp_Factory.create(StateContainerStProvider, ((Provider) DocumentContainerMsProvider), appStateMsProvider, ErrorRouterProvider));
      this.copyCursorRangeToClipboardActionImpProvider = DoubleCheck.provider(CopyCursorRangeToClipboardActionImp_Factory.create(((Provider) worksheetActionImpProvider), ErrorRouterProvider, StateContainerStProvider));
      this.undoOnCursorActionImpProvider = DoubleCheck.provider(UndoOnCursorActionImp_Factory.create(StateContainerStProvider));
      this.cellEditorActionImpProvider = new DelegateFactory<>();
      this.handleCursorKeyboardEventActionImpProvider = DoubleCheck.provider(HandleCursorKeyboardEventActionImp_Factory.create(((Provider) worksheetActionImpProvider), ((Provider) openCellEditorImpProvider), StateContainerStProvider, ((Provider) pasteRangeToCursorImpProvider), ((Provider) selectWholeColumnForAllSelectedCellActionImpProvider), ((Provider) selectWholeRowForAllSelectedCellActionImpProvider), ((Provider) makeSliderFollowCellActionImpProvider), ((Provider) copyCursorRangeToClipboardActionImpProvider), ((Provider) undoOnCursorActionImpProvider), ((Provider) cellEditorActionImpProvider)));
      this.textDifferImpProvider = DoubleCheck.provider(TextDifferImp_Factory.create());
      this.partialTextElementTranslatorProvider = PartialTextElementTranslator_Factory.create(TextElementVisitor_Factory.create(), PartialFormulaTreeExtractorProvider);
      this.PartialCellRangeExtractorProvider = DoubleCheck.provider((Provider) partialTextElementTranslatorProvider);
      this.cycleFormulaLockStateImpProvider = DoubleCheck.provider(CycleFormulaLockStateImp_Factory.create(StateContainerStProvider, PartialCellRangeExtractorProvider));
      DelegateFactory.setDelegate(cellEditorActionImpProvider, DoubleCheck.provider(CellEditorActionImp_Factory.create(((Provider) cellLiteralParserImpProvider), ((Provider) updateCellActionImpProvider), ((Provider) handleCursorKeyboardEventActionImpProvider), ((Provider) makeCellEditorTextActionImpProvider), ((Provider) openCellEditorImpProvider), StateContainerMsProvider, ((Provider) textDifferImpProvider), ((Provider) cycleFormulaLockStateImpProvider), PartialFormulaTreeExtractorProvider, ((Provider) colorFormulaInCellEditorActionImpProvider), ((Provider) closeCellEditorActionImpProvider), ((Provider) runFormulaOrSaveValueToCellActionImpProvider))));
      this.copyCellActionImpProvider = DoubleCheck.provider(CopyCellActionImp_Factory.create(StateContainerStProvider));
      this.endThumbDragActionImpProvider = DoubleCheck.provider(EndThumbDragActionImp_Factory.create(StateContainerStProvider, ((Provider) copyCellActionImpProvider), ((Provider) multiCellUpdateActionImpProvider)));
      this.dragThumbActionImpProvider = DoubleCheck.provider(DragThumbActionImp_Factory.create(StateContainerStProvider, ((Provider) endThumbDragActionImpProvider)));
      this.thumbActionImpProvider = DoubleCheck.provider(ThumbActionImp_Factory.create(((Provider) dragThumbActionImpProvider)));
      this.cursorActionImpProvider = DoubleCheck.provider(CursorActionImp_Factory.create(((Provider) worksheetActionImpProvider), StateContainerStProvider, FormulaColorProvider, ((Provider) pasteRangeToCursorImpProvider), ((Provider) selectWholeColumnForAllSelectedCellActionImpProvider), ((Provider) selectWholeRowForAllSelectedCellActionImpProvider), ((Provider) cellEditorActionImpProvider), ((Provider) thumbActionImpProvider), ((Provider) handleCursorKeyboardEventActionImpProvider), ((Provider) copyCursorRangeToClipboardActionImpProvider), ((Provider) undoOnCursorActionImpProvider)));
      this.cellRpcActionsImpProvider = DoubleCheck.provider(CellRpcActionsImp_Factory.create(((Provider) updateCellActionImpProvider), ((Provider) copyCellActionImpProvider)));
      this.cellRpcServiceProvider = DoubleCheck.provider(CellRpcService_Factory.create(StateContainerStProvider, ((Provider) cellRpcActionsImpProvider), CoroutineModule_Companion_ActionDispatcherDefaultFactory.create()));
      this.createNewWorksheetRMImpProvider = DoubleCheck.provider(CreateNewWorksheetRMImp_Factory.create(DocumentContainerMsProvider));
      this.createNewWorksheetApplierImpProvider = DoubleCheck.provider(CreateNewWorksheetApplierImp_Factory.create(SubAppStateContainerMsProvider, WbContainerProvider));
      this.createNewWorksheetActionImpProvider = DoubleCheck.provider(CreateNewWorksheetActionImp_Factory.create(((Provider) createNewWorksheetRMImpProvider), ((Provider) createNewWorksheetApplierImpProvider)));
      this.replaceWorkbookKeyActionImpProvider = ReplaceWorkbookKeyActionImp_Factory.create(StateContainerMsProvider, ErrorRouterProvider);
      this.wsNameGeneratorImpProvider = DoubleCheck.provider(WsNameGeneratorImp_Factory.create());
      this.autoNameWbFactoryProvider = DoubleCheck.provider(AutoNameWbFactory_Factory.create(WbContainerProvider, ((Provider) wsNameGeneratorImpProvider)));
      this.createNewWbRMImpProvider = DoubleCheck.provider(CreateNewWbRMImp_Factory.create(((Provider) autoNameWbFactoryProvider)));
      this.pickDefaultActiveWbActionImpProvider = DoubleCheck.provider(PickDefaultActiveWbActionImp_Factory.create(StateContainerStProvider));
      this.createNewWorkbookApplierImpProvider = DoubleCheck.provider(CreateNewWorkbookApplierImp_Factory.create(((Provider) baseApplierImpProvider), StateContainerMsProvider, ((Provider) pickDefaultActiveWbActionImpProvider)));
      this.createNewWorkbookActionImpProvider = DoubleCheck.provider(CreateNewWorkbookActionImp_Factory.create(((Provider) createNewWbRMImpProvider), ((Provider) createNewWorkbookApplierImpProvider)));
      this.setActiveWorkbookActionImpProvider = DoubleCheck.provider(SetActiveWorkbookActionImp_Factory.create(StateContainerStProvider, appStateMsProvider));
      this.p6SaverImpProvider = DoubleCheck.provider(P6SaverImp_Factory.create());
      this.saveWorkbookActionImpProvider = DoubleCheck.provider(SaveWorkbookActionImp_Factory.create(StateContainerStProvider, ErrorRouterProvider, ((Provider) p6SaverImpProvider), ((Provider) replaceWorkbookKeyActionImpProvider)));
      this.readFileFunctionProvider = DoubleCheck.provider(UtilModule_Companion_ReadFileFunctionFactory.create());
      this.p6FileLoaderImpProvider = DoubleCheck.provider(P6FileLoaderImp_Factory.create(TranslatorContainerStProvider, readFileFunctionProvider));
      this.loadWorkbookRMImpProvider = DoubleCheck.provider(LoadWorkbookRMImp_Factory.create(((Provider) p6FileLoaderImpProvider)));
      this.loadWorkbookInternalApplierImpProvider = DoubleCheck.provider(LoadWorkbookInternalApplierImp_Factory.create(StateContainerMsProvider));
      this.loadWorkbookApplierImpProvider = DoubleCheck.provider(LoadWorkbookApplierImp_Factory.create(((Provider) loadWorkbookInternalApplierImpProvider), ((Provider) baseApplierImpProvider)));
      this.loadWorkbookActionImpProvider = DoubleCheck.provider(LoadWorkbookActionImp_Factory.create(StateContainerMsProvider, ErrorRouterProvider, ((Provider) loadWorkbookRMImpProvider), ((Provider) loadWorkbookApplierImpProvider)));
      this.closeWorkbookRMImpProvider = DoubleCheck.provider(CloseWorkbookRMImp_Factory.create(StateContainerMsProvider));
      this.closeWorkbookInternalApplierImpProvider = DoubleCheck.provider(CloseWorkbookInternalApplierImp_Factory.create(appStateMsProvider, StateContainerMsProvider, ((Provider) pickDefaultActiveWbActionImpProvider)));
      this.closeWorkbookApplierImpProvider = DoubleCheck.provider(CloseWorkbookApplierImp_Factory.create(((Provider) closeWorkbookInternalApplierImpProvider), ((Provider) baseApplierImpProvider)));
      this.closeWorkbookActionImpProvider = DoubleCheck.provider(CloseWorkbookActionImp_Factory.create(((Provider) closeWorkbookRMImpProvider), ((Provider) closeWorkbookApplierImpProvider), StateContainerMsProvider, ((Provider) pickDefaultActiveWbActionImpProvider)));
      this.getWorkbookActionImpProvider = DoubleCheck.provider(GetWorkbookActionImp_Factory.create(StateContainerStProvider));
      this.removeAllWorksheetActionImpProvider = DoubleCheck.provider(RemoveAllWorksheetActionImp_Factory.create(StateContainerStProvider));
      this.workbookRpcActionsImpProvider = DoubleCheck.provider(WorkbookRpcActionsImp_Factory.create(((Provider) createNewWorksheetActionImpProvider), ((Provider) replaceWorkbookKeyActionImpProvider), ((Provider) newWorksheetActionImpProvider), ((Provider) deleteWorksheetActionImpProvider), ((Provider) renameWorksheetActionImpProvider), ((Provider) setActiveWorksheetActionImpProvider), ((Provider) createNewWorkbookActionImpProvider), ((Provider) setActiveWorkbookActionImpProvider), ((Provider) saveWorkbookActionImpProvider), ((Provider) loadWorkbookActionImpProvider), ((Provider) closeWorkbookActionImpProvider), ((Provider) getWorkbookActionImpProvider), ((Provider) removeAllWorksheetActionImpProvider)));
      this.workbookRpcServiceProvider = DoubleCheck.provider(WorkbookRpcService_Factory.create(((Provider) translatorContainerImpProvider), ((Provider) workbookRpcActionsImpProvider), DocumentContainerMsProvider, SubAppStateContainerMsProvider, applicationCoroutineScopeProvider, CoroutineModule_Companion_ActionDispatcherDefaultFactory.create()));
      this.appRpcActionImpProvider = DoubleCheck.provider(AppRpcActionImp_Factory.create(((Provider) createNewWorkbookActionImpProvider), ((Provider) setActiveWorkbookActionImpProvider), ((Provider) saveWorkbookActionImpProvider), ((Provider) loadWorkbookActionImpProvider), ((Provider) closeWorkbookActionImpProvider), ((Provider) getWorkbookActionImpProvider)));
      this.appRpcServiceProvider = DoubleCheck.provider(AppRpcService_Factory.create(appStateMsProvider, StateContainerStProvider, ((Provider) appRpcActionImpProvider), CoroutineModule_Companion_ActionDispatcherDefaultFactory.create()));
      this.loadDataActionImpProvider = DoubleCheck.provider(LoadDataActionImp_Factory.create(StateContainerStProvider, ErrorRouterProvider, TranslatorContainerStProvider));
      this.removeAllCellActionImpProvider = DoubleCheck.provider(RemoveAllCellActionImp_Factory.create(StateContainerStProvider));
      this.worksheetRpcActionImpProvider = DoubleCheck.provider(WorksheetRpcActionImp_Factory.create(((Provider) pasteRangeActionImpProvider), ((Provider) updateCellActionImpProvider), ((Provider) deleteMultiCellActionImpProvider), ((Provider) loadDataActionImpProvider), ((Provider) removeAllCellActionImpProvider), ((Provider) multiCellUpdateActionImpProvider)));
      this.worksheetRpcServiceProvider = DoubleCheck.provider(WorksheetRpcService_Factory.create(StateContainerStProvider, ((Provider) worksheetRpcActionImpProvider), CoroutineModule_Companion_ActionDispatcherDefaultFactory.create()));
      this.p6RpcServerImpProvider = DoubleCheck.provider(P6RpcServerImp_Factory.create(((Provider) cellRpcServiceProvider), ((Provider) workbookRpcServiceProvider), ((Provider) appRpcServiceProvider), ((Provider) worksheetRpcServiceProvider)));
      this.msP6RpcServerProvider = DoubleCheck.provider(MsP6RpcServer_Factory.create(((Provider) p6RpcServerImpProvider), RPCStatusItemStateMsProvider));
      this.usernameProvider = InstanceFactory.create(usernameParam);
      this.KernelContextReadOnlyProvider = DoubleCheck.provider(MsgApiModule_Companion_KernelContextReadOnlyFactory.create(KernelContextProvider));
      this.pythonCodeRunnerProvider = PythonCodeRunner_Factory.create(usernameProvider, KernelContextReadOnlyProvider);
      this.CodeRunnerProvider = DoubleCheck.provider((Provider) pythonCodeRunnerProvider);
      this.scriptEditorErrorRouterImpProvider = ScriptEditorErrorRouterImp_Factory.create(CodeEditorStateProvider, ErrorRouterProvider);
      this.ScriptEditorErrorRouterProvider = DoubleCheck.provider((Provider) scriptEditorErrorRouterImpProvider);
      this.newScriptRMImpProvider = DoubleCheck.provider(NewScriptRMImp_Factory.create(appStateMsProvider));
      this.scriptRMImpProvider = DoubleCheck.provider(ScriptRMImp_Factory.create(((Provider) newScriptRMImpProvider)));
      this.newScriptApplierImpProvider = DoubleCheck.provider(NewScriptApplierImp_Factory.create(CodeEditorStateProvider, ScriptEditorErrorRouterProvider));
      this.scriptChangeApplierImpProvider = DoubleCheck.provider(ScriptChangeApplierImp_Factory.create(((Provider) baseApplierImpProvider)));
      this.scriptApplierImpProvider = DoubleCheck.provider(ScriptApplierImp_Factory.create(((Provider) newScriptApplierImpProvider), ((Provider) scriptChangeApplierImpProvider)));
      this.codeEditorActionImpProvider = DoubleCheck.provider(CodeEditorActionImp_Factory.create(CodeEditorStateProvider, CodeRunnerProvider, applicationCoroutineScopeProvider, ScriptEditorErrorRouterProvider, ((Provider) scriptRMImpProvider), ((Provider) scriptApplierImpProvider)));
      this.scriptTreeActionImpProvider = DoubleCheck.provider(ScriptTreeActionImp_Factory.create(((Provider) codeEditorActionImpProvider), CodeEditorStateProvider));
      this.codeEditorActionTableImpProvider = DoubleCheck.provider(CodeEditorActionTableImp_Factory.create(((Provider) codeEditorActionImpProvider), ((Provider) scriptTreeActionImpProvider)));
      this.applicationScopeProvider = InstanceFactory.createNullable(applicationScopeParam);
      this.appActionImpProvider = AppActionImp_Factory.create(applicationScopeProvider, appStateMsProvider);
      this.AppActionProvider = DoubleCheck.provider((Provider) appActionImpProvider);
      this.appActionTableImpProvider = DoubleCheck.provider(AppActionTableImp_Factory.create());
      this.KernelServiceManagerProvider = DoubleCheck.provider(MsgApiModule_Companion_KernelServiceManagerFactory.create(messageApiComponentProvider));
      this.gsonProvider = DoubleCheck.provider(UtilModule_Companion_GsonFactory.create());
      this.FileUtilProvider = DoubleCheck.provider(P6Module_Companion_FileUtilFactory.create());
      this.restartKernelRMImpProvider = DoubleCheck.provider(RestartKernelRMImp_Factory.create(((Provider) msKernelContextProvider), KernelServiceManagerProvider, gsonProvider, FileUtilProvider));
    }

    @SuppressWarnings("unchecked")
    private void initialize3(final MessageApiComponent messageApiComponentParam,
        final String usernameParam, final CoroutineScope applicationCoroutineScopeParam,
        final ApplicationScope applicationScopeParam) {
      this.appRMImpProvider = DoubleCheck.provider(AppRMImp_Factory.create(((Provider) createNewWbRMImpProvider), ((Provider) loadWorkbookRMImpProvider), ((Provider) setActiveWorksheetRMImpProvider), ((Provider) restartKernelRMImpProvider)));
      this.restartKernelApplierImpProvider = RestartKernelApplierImp_Factory.create(appStateMsProvider, StateContainerMsProvider);
      this.kernelActionImpProvider = KernelActionImp_Factory.create(((Provider) appRMImpProvider), ((Provider) restartKernelApplierImpProvider));
      this.KernelActionProvider = DoubleCheck.provider((Provider) kernelActionImpProvider);
      this.setActiveWindowActionImpProvider = DoubleCheck.provider(SetActiveWindowActionImp_Factory.create(InitActiveWindowPointerProvider, StateContainerStProvider));
      this.closeWindowActionImpProvider = DoubleCheck.provider(CloseWindowActionImp_Factory.create(applicationScopeProvider, SubAppStateContainerMsProvider));
      this.makeSavePathImpProvider = DoubleCheck.provider(MakeSavePathImp_Factory.create());
      this.windowActionImpProvider = DoubleCheck.provider(WindowActionImp_Factory.create(applicationScopeProvider, KernelActionProvider, ((Provider) closeWorkbookActionImpProvider), SubAppStateContainerMsProvider, ((Provider) createNewWorkbookActionImpProvider), ((Provider) saveWorkbookActionImpProvider), ((Provider) loadWorkbookActionImpProvider), ((Provider) setActiveWindowActionImpProvider), ((Provider) closeWindowActionImpProvider), ((Provider) makeSavePathImpProvider)));
      this.fileMenuActionImpProvider = DoubleCheck.provider(FileMenuActionImp_Factory.create(((Provider) windowActionImpProvider), StateContainerMsProvider, ((Provider) closeWorkbookActionImpProvider)));
      this.rulerActionImpProvider = DoubleCheck.provider(RulerActionImp_Factory.create(StateContainerMsProvider, ((Provider) updateRangeSelectorTextImpProvider)));
      this.worksheetActionTableImpProvider = DoubleCheck.provider(WorksheetActionTableImp_Factory.create(((Provider) cursorActionImpProvider), ((Provider) cellEditorActionImpProvider), ((Provider) rulerActionImpProvider), ((Provider) worksheetActionImpProvider), ((Provider) thumbActionImpProvider)));
      this.workbookActionTableImpProvider = DoubleCheck.provider(WorkbookActionTableImp_Factory.create(((Provider) workbookActionImpProvider), ((Provider) worksheetActionTableImpProvider)));
      this.workbookTabBarActionImpProvider = DoubleCheck.provider(WorkbookTabBarActionImp_Factory.create(((Provider) windowActionImpProvider), ((Provider) moveToWbActionImpProvider)));
      this.codeMenuActionImpProvider = DoubleCheck.provider(CodeMenuActionImp_Factory.create(((Provider) windowActionImpProvider), AppActionProvider, ((Provider) msKernelContextProvider)));
      this.windowActionTableImpProvider = DoubleCheck.provider(WindowActionTableImp_Factory.create(((Provider) fileMenuActionImpProvider), ((Provider) workbookActionTableImpProvider), ((Provider) workbookTabBarActionImpProvider), ((Provider) codeMenuActionImpProvider), ((Provider) codeEditorActionImpProvider), ((Provider) windowActionImpProvider)));
      this.pythonCommanderImpProvider = PythonCommanderImp_Factory.create(CodeRunnerProvider);
      this.BackEndCommanderProvider = DoubleCheck.provider((Provider) pythonCommanderImpProvider);
      this.appContextImpProvider = AppContextImp_Factory.create(usernameProvider, ((Provider) msKernelContextProvider), CodeRunnerProvider, BackEndCommanderProvider);
      this.AppContextProvider = DoubleCheck.provider((Provider) appContextImpProvider);
      this.ZContextProvider = DoubleCheck.provider(MsgApiModule_Companion_ZContextFactory.create(messageApiComponentProvider));
      this.eventServerPortProvider = DoubleCheck.provider(P6Module_Companion_EventServerPortFactory.create());
      this.eventServerSocketProvider = DoubleCheck.provider(P6Module_Companion_EventServerSocketFactory.create(ZContextProvider, eventServerPortProvider));
      this.updateCellFormatActionImpProvider = DoubleCheck.provider(UpdateCellFormatActionImp_Factory.create(CellFormatTableMsProvider, StateContainerStProvider));
    }

    @Override
    public MoveToWbAction moveToWbAction() {
      return moveToWbActionImpProvider.get();
    }

    @Override
    public WorkbookAction workbookAction() {
      return workbookActionImpProvider.get();
    }

    @Override
    public CursorAction cursorAction() {
      return cursorActionImpProvider.get();
    }

    @Override
    public CursorStateFactory cursorStateFactory() {
      return cursorStateFactoryProvider.get();
    }

    @Override
    public ErrorRouter errorRouter() {
      return ErrorRouterProvider.get();
    }

    @Override
    public WorkbookStateFactory workbookStateFactory() {
      return workbookStateFactoryProvider.get();
    }

    @Override
    public WorksheetStateFactory worksheetStateFactory() {
      return worksheetStateFactoryProvider.get();
    }

    @Override
    public LimitedGridSliderFactory gridSliderFactory() {
      return limitedGridSliderFactoryProvider.get();
    }

    @Override
    public P6RpcServer p6RpcServer() {
      return msP6RpcServerProvider.get();
    }

    @Override
    public WindowStateFactory windowStateFactory() {
      return windowStateFactoryProvider.get();
    }

    @Override
    public OuterWindowStateFactory outerWindowStateFactory() {
      return outerWindowStateFactoryProvider.get();
    }

    @Override
    public CodeEditorActionTable codeEditorActionTable() {
      return codeEditorActionTableImpProvider.get();
    }

    @Override
    public P6EventTable p6EventTable() {
      return P6EventTableProvider.get();
    }

    @Override
    public AppAction appAction() {
      return AppActionProvider.get();
    }

    @Override
    public AppActionTable getAppActionTable() {
      return appActionTableImpProvider.get();
    }

    @Override
    public ApplicationScope applicationScope() {
      return applicationScope;
    }

    @Override
    public WindowActionTable windowActionTable() {
      return windowActionTableImpProvider.get();
    }

    @Override
    public WindowAction windowAction() {
      return windowActionImpProvider.get();
    }

    @Override
    public WorkbookActionTable workbookActionTable() {
      return workbookActionTableImpProvider.get();
    }

    @Override
    public WorksheetActionTable worksheetActionTable() {
      return worksheetActionTableImpProvider.get();
    }

    @Override
    public WorksheetAction wsAction() {
      return worksheetActionImpProvider.get();
    }

    @Override
    public MutableState<AppState> appStateMs() {
      return appStateMsProvider.get();
    }

    @Override
    public MutableState<WorkbookContainer> wbContainerMs() {
      return WbContainerProvider.get();
    }

    @Override
    public AppContext appContext() {
      return AppContextProvider.get();
    }

    @Override
    public MessageApiComponent msgApiComponent() {
      return messageApiComponent;
    }

    @Override
    public ZContext zContext() {
      return ZContextProvider.get();
    }

    @Override
    public KernelContext kernelContext() {
      return KernelContextProvider.get();
    }

    @Override
    public CodeRunner codeRunner() {
      return CodeRunnerProvider.get();
    }

    @Override
    public PythonCommander backEndCommander() {
      return BackEndCommanderProvider.get();
    }

    @Override
    public Gson gson() {
      return gsonProvider.get();
    }

    @Override
    public CellRM cellRequestMaker() {
      return cellRMImpProvider.get();
    }

    @Override
    public AppRM appRequestMaker() {
      return appRMImpProvider.get();
    }

    @Override
    public KernelServiceManager kernelServiceManager() {
      return KernelServiceManagerProvider.get();
    }

    @Override
    public ZMQ.Socket eventServerSocket() {
      return eventServerSocketProvider.get();
    }

    @Override
    public int eventServerPort() {
      return eventServerPortProvider.get();
    }

    @Override
    public CoroutineScope executionScope() {
      return applicationCoroutineScope;
    }

    @Override
    public WorkbookTabBarAction wbTabBarAction() {
      return workbookTabBarActionImpProvider.get();
    }

    @Override
    public CellEditorAction cellEditorAction() {
      return cellEditorActionImpProvider.get();
    }

    @Override
    public CellLiteralParser cellLiteralParser() {
      return cellLiteralParserImpProvider.get();
    }

    @Override
    public UpdateCellAction cellViewAction() {
      return updateCellActionImp();
    }

    @Override
    public MakeCellEditorTextAction makeDisplayText() {
      return makeCellEditorTextActionImpProvider.get();
    }

    @Override
    public OpenCellEditorAction openCellEditorAction() {
      return openCellEditorImpProvider.get();
    }

    @Override
    public JvmFormulaTranslatorFactory translatorFactory() {
      return jvmFormulaTranslatorFactoryProvider.get();
    }

    @Override
    public JvmFormulaVisitorFactory visitorFactory2() {
      return jvmFormulaVisitorFactoryProvider.get();
    }

    @Override
    public ClickOnCellAction clickOnCellAction() {
      return clickOnCellActionImpProvider.get();
    }

    @Override
    public MouseOnWorksheetAction mouseOnWsAction() {
      return mouseOnWorksheetActionImpProvider.get();
    }

    @Override
    public MutableState<StateContainer> stateContMs() {
      return StateContainerMsProvider.get();
    }

    @Override
    public WorkbookFactory workbookFactory() {
      return autoNameWbFactoryProvider.get();
    }

    @Override
    public CreateNewWorkbookAction createNewWbAction() {
      return createNewWorkbookActionImpProvider.get();
    }

    @Override
    public SetActiveWorkbookAction setActiveWorkbookAction() {
      return setActiveWorkbookActionImpProvider.get();
    }

    @Override
    public P6FileLoader fileLoader() {
      return p6FileLoaderImpProvider.get();
    }

    @Override
    public LoadWorkbookAction loadWbAction() {
      return loadWorkbookActionImpProvider.get();
    }

    @Override
    public PickDefaultActiveWbAction pickDefaultActiveWbAction() {
      return pickDefaultActiveWbActionImpProvider.get();
    }

    @Override
    public SaveWorkbookAction saveWbAction() {
      return saveWorkbookActionImpProvider.get();
    }

    @Override
    public GetWorkbookAction getWorkbookAction() {
      return getWorkbookActionImpProvider.get();
    }

    @Override
    public BaseApplier baseApplier() {
      return baseApplierImpProvider.get();
    }

    @Override
    public CloseWorkbookActionImp closeWbAct() {
      return closeWorkbookActionImpProvider.get();
    }

    @Override
    public SetActiveWindowAction setActiveWindowAction() {
      return setActiveWindowActionImpProvider.get();
    }

    @Override
    public UpdateCellAction updateCellAction() {
      return updateCellActionImp();
    }

    @Override
    public LoadDataAction loadDataAction() {
      return loadDataActionImpProvider.get();
    }

    @Override
    public RemoveAllCellAction removeAllCellAction() {
      return removeAllCellActionImpProvider.get();
    }

    @Override
    public RemoveAllWorksheetAction removeAllWorksheetAction() {
      return removeAllWorksheetActionImpProvider.get();
    }

    @Override
    public MultiCellUpdateAction multiCellUpdateAction() {
      return multiCellUpdateActionImpProvider.get();
    }

    @Override
    public NewWorksheetAction newWorksheetAction() {
      return newWorksheetActionImpProvider.get();
    }

    @Override
    public ComputeSliderSizeAction computeSliderSizeAction() {
      return computeSliderSizeActionImpProvider.get();
    }

    @Override
    public RulerAction rulerAction() {
      return rulerActionImpProvider.get();
    }

    @Override
    public WorksheetAction2 worksheetAction2() {
      return worksheetAction2ImpProvider.get();
    }

    @Override
    public CellEditorAction cursorEditorAction() {
      return cellEditorActionImpProvider.get();
    }

    @Override
    public TextDiffer textDiffer() {
      return textDifferImpProvider.get();
    }

    @Override
    public ThumbStateFactory thumbStateFactory() {
      return thumbStateFactoryProvider.get();
    }

    @Override
    public DragThumbAction dragThumbAction() {
      return dragThumbActionImpProvider.get();
    }

    @Override
    public EndThumbDragAction endThumbDragAction() {
      return endThumbDragActionImpProvider.get();
    }

    @Override
    public CopyCellAction copyCellAction() {
      return copyCellActionImpProvider.get();
    }

    @Override
    public PartialTextElementTranslator partialTextElementExtractor() {
      return new PartialTextElementTranslator(new TextElementVisitor(), PartialFormulaTreeExtractorProvider.get());
    }

    @Override
    public CycleFormulaLockStateAction cycleFormulaLockStateAct() {
      return cycleFormulaLockStateImpProvider.get();
    }

    @Override
    public PartialFormulaTreeExtractor partialFormulaTreeExtractor() {
      return new PartialFormulaTreeExtractor();
    }

    @Override
    public ColorFormulaInCellEditorActionImp colorFormulaActionImp() {
      return colorFormulaInCellEditorActionImpProvider.get();
    }

    @Override
    public ColorFormulaInCellEditorAction colorFormulaAction() {
      return colorFormulaInCellEditorActionImpProvider.get();
    }

    @Override
    public RangeAddressFormatter rangeFormatter() {
      return rangeAddressFormatterImpProvider.get();
    }

    @Override
    public DeleteMultiCellAction deleteMultiCellAction() {
      return deleteMultiCellActionImpProvider.get();
    }

    @Override
    public FileMenuActionImp fileMenuActionImp() {
      return fileMenuActionImpProvider.get();
    }

    @Override
    public CloseCellEditorActionImp closeCellEditorAction() {
      return closeCellEditorActionImpProvider.get();
    }

    @Override
    public RunFormulaOrSaveValueToCellActionImp runFormulaOrSaveValueToCellAction() {
      return runFormulaOrSaveValueToCellActionImpProvider.get();
    }

    @Override
    public UpdateCellFormatActionImp updateCellFormatAction() {
      return updateCellFormatActionImpProvider.get();
    }
  }
}
