package test

import androidx.compose.runtime.MutableState
import androidx.compose.ui.window.ApplicationScope
import com.google.gson.Gson
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.action.app.AppRM
import com.qxdzbc.p6.app.action.app.close_wb.CloseWorkbookAction
import com.qxdzbc.p6.app.action.app.create_new_wb.CreateNewWorkbookAction
import com.qxdzbc.p6.app.action.app.get_wb.GetWorkbookAction
import com.qxdzbc.p6.app.action.app.load_wb.LoadWorkbookAction
import com.qxdzbc.p6.app.action.app.save_wb.SaveWorkbookAction
import com.qxdzbc.p6.app.action.app.set_active_wb.SetActiveWorkbookAction
import com.qxdzbc.p6.app.action.app.set_active_wd.SetActiveWindowAction
import com.qxdzbc.p6.app.action.applier.BaseApplier
import com.qxdzbc.p6.app.action.cell.CellRM
import com.qxdzbc.p6.app.action.cell.cell_update.UpdateCellAction
import com.qxdzbc.p6.app.action.cell.copy_cell.CopyCellAction
import com.qxdzbc.p6.app.action.cell.multi_cell_update.MultiCellUpdateAction
import com.qxdzbc.p6.app.action.cell_editor.color_formula.ColorFormulaInCellEditorAction
import com.qxdzbc.p6.app.action.cell_editor.color_formula.ColorFormulaInCellEditorActionImp
import com.qxdzbc.p6.app.action.cell_editor.cycle_formula_lock_state.CycleFormulaLockStateAction
import com.qxdzbc.p6.app.action.cell_editor.open_cell_editor.OpenCellEditorAction
import com.qxdzbc.p6.app.action.window.WindowAction
import com.qxdzbc.p6.app.action.window.pick_active_wb.PickDefaultActiveWbAction
import com.qxdzbc.p6.app.action.workbook.WorkbookAction
import com.qxdzbc.p6.app.action.workbook.new_worksheet.NewWorksheetAction
import com.qxdzbc.p6.app.action.workbook.remove_all_ws.RemoveAllWorksheetAction
import com.qxdzbc.p6.app.action.worksheet.WorksheetAction
import com.qxdzbc.p6.app.action.worksheet.action2.WorksheetAction2
import com.qxdzbc.p6.app.action.worksheet.compute_slider_size.ComputeSliderSizeAction
import com.qxdzbc.p6.app.action.worksheet.delete_multi.DeleteMultiCellAction
import com.qxdzbc.p6.app.action.worksheet.load_data.LoadDataAction
import com.qxdzbc.p6.app.action.worksheet.make_cell_editor_display_text.MakeCellEditorTextAction
import com.qxdzbc.p6.app.action.worksheet.mouse_on_ws.MouseOnWorksheetAction
import com.qxdzbc.p6.app.action.worksheet.mouse_on_ws.click_on_cell.ClickOnCell
import com.qxdzbc.p6.app.action.worksheet.remove_all_cell.RemoveAllCellAction
import com.qxdzbc.p6.app.app_context.AppContext
import com.qxdzbc.p6.app.code.PythonCommander
import com.qxdzbc.p6.app.coderunner.CodeRunner
import com.qxdzbc.p6.app.common.formatter.RangeAddressFormatter
import com.qxdzbc.p6.app.communication.event.P6EventTable
import com.qxdzbc.p6.app.document.wb_container.WorkbookContainer
import com.qxdzbc.p6.app.document.workbook.WorkbookFactory
import com.qxdzbc.p6.app.file.loader.P6FileLoader
import com.qxdzbc.p6.di.*
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.di.rpc.MsRpcServerQualifier
import com.qxdzbc.p6.message.api.connection.kernel_context.KernelContext
import com.qxdzbc.p6.message.api.connection.kernel_services.KernelServiceManager
import com.qxdzbc.p6.message.di.MessageApiComponent
import com.qxdzbc.p6.rpc.P6RpcServer
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnitErrors
import com.qxdzbc.p6.translator.jvm_translator.CellLiteralParser
import com.qxdzbc.p6.translator.jvm_translator.JvmFormulaTranslatorFactory
import com.qxdzbc.p6.translator.jvm_translator.JvmFormulaVisitorFactory
import com.qxdzbc.p6.translator.partial_text_element_extractor.PartialFormulaTreeExtractor
import com.qxdzbc.p6.translator.partial_text_element_extractor.PartialTextElementExtractor
import com.qxdzbc.p6.ui.app.action.AppAction
import com.qxdzbc.p6.ui.app.action.AppActionTable
import com.qxdzbc.p6.ui.app.cell_editor.actions.CellEditorAction
import com.qxdzbc.p6.ui.app.cell_editor.actions.differ.TextDiffer
import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.document.workbook.action.WorkbookActionTable
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookStateFactory
import com.qxdzbc.p6.ui.document.worksheet.action.WorksheetActionTable
import com.qxdzbc.p6.ui.document.worksheet.cursor.actions.CursorAction
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorStateFactory
import com.qxdzbc.p6.ui.document.worksheet.cursor.thumb.action.DragThumbAction
import com.qxdzbc.p6.ui.document.worksheet.cursor.thumb.action.EndThumbDragAction
import com.qxdzbc.p6.ui.document.worksheet.cursor.thumb.state.ThumbStateFactory
import com.qxdzbc.p6.ui.document.worksheet.ruler.actions.RulerAction
import com.qxdzbc.p6.ui.document.worksheet.slider.LimitedGridSliderFactory
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetStateFactory
import com.qxdzbc.p6.ui.script_editor.action.CodeEditorActionTable
import com.qxdzbc.p6.ui.window.action.WindowActionTable
import com.qxdzbc.p6.ui.window.move_to_wb.MoveToWbAction
import com.qxdzbc.p6.ui.window.state.OuterWindowStateFactory
import com.qxdzbc.p6.ui.window.state.WindowStateFactory
import com.qxdzbc.p6.ui.window.workbook_tab.bar.WorkbookTabBarAction
import com.squareup.anvil.annotations.MergeComponent
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.CoroutineScope
import org.zeromq.ZContext
import org.zeromq.ZMQ

@MergeComponent(
    scope = P6AnvilScope::class,
    modules = [
        P6Module::class,
    ],
)
@P6Singleton
interface TestComponent {
    @P6Singleton
    fun moveToWbAction(): MoveToWbAction

    @P6Singleton
    fun workbookAction(): WorkbookAction

    @P6Singleton
    fun cursorAction(): CursorAction

    @P6Singleton
    fun cursorStateFactory(): CursorStateFactory

    @P6Singleton
    fun errorRouter(): ErrorRouter

    @P6Singleton
    fun workbookStateFactory(): WorkbookStateFactory

    @P6Singleton
    fun worksheetStateFactory(): WorksheetStateFactory

    @P6Singleton
    fun gridSliderFactory(): LimitedGridSliderFactory

    @P6Singleton
    @MsRpcServerQualifier
    fun p6RpcServer(): P6RpcServer

    @P6Singleton
    fun windowStateFactory(): WindowStateFactory

    @P6Singleton
    fun outerWindowStateFactory(): OuterWindowStateFactory

    @P6Singleton
    fun codeEditorActionTable(): CodeEditorActionTable

    fun p6EventTable(): P6EventTable

    fun appAction(): AppAction

    val appActionTable: AppActionTable

    fun applicationScope(): ApplicationScope?

    @P6Singleton
    fun windowActionTable(): WindowActionTable

    @P6Singleton
    fun windowAction(): WindowAction

    @P6Singleton
    fun workbookActionTable(): WorkbookActionTable

    @P6Singleton
    fun worksheetActionTable(): WorksheetActionTable

    @P6Singleton
    fun wsAction(): WorksheetAction

    @P6Singleton
    fun appStateMs(): Ms<AppState>

    @P6Singleton
    fun wbContainerMs(): Ms<WorkbookContainer>

    @P6Singleton
    fun appContext(): AppContext

    @P6Singleton
    fun msgApiComponent(): MessageApiComponent

    @P6Singleton
    fun zContext(): ZContext

    @P6Singleton
    fun kernelContext(): KernelContext

    @P6Singleton
    fun codeRunner(): CodeRunner

    @P6Singleton
    fun backEndCommander(): PythonCommander

    @P6Singleton
    fun gson(): Gson

    @P6Singleton
    fun cellRequestMaker(): CellRM

    @P6Singleton
    fun appRequestMaker(): AppRM

    @P6Singleton
    fun kernelServiceManager(): KernelServiceManager

    @P6Singleton
    @EventServerSocket
    fun eventServerSocket(): ZMQ.Socket

    @EventServerPort
    fun eventServerPort(): Int

    @AppCoroutineScope
    fun executionScope(): CoroutineScope

    @P6Singleton
    fun wbTabBarAction(): WorkbookTabBarAction

    @P6Singleton
    fun cellEditorAction(): CellEditorAction

    @P6Singleton
    fun cellLiteralParser(): CellLiteralParser

    @P6Singleton
    fun cellViewAction(): UpdateCellAction

    @P6Singleton
    fun makeDisplayText(): MakeCellEditorTextAction

    @P6Singleton
    fun openCellEditorAction(): OpenCellEditorAction
    fun translatorFactory(): JvmFormulaTranslatorFactory
    fun visitorFactory2(): JvmFormulaVisitorFactory

    fun clickOnCellAction(): ClickOnCell
    fun mouseOnWsAction(): MouseOnWorksheetAction
    fun stateContMs(): MutableState<StateContainer>
    fun workbookFactory(): WorkbookFactory
    fun createNewWbAction(): CreateNewWorkbookAction
    fun setActiveWorkbookAction(): SetActiveWorkbookAction
    fun fileLoader(): P6FileLoader
    fun loadWbAction(): LoadWorkbookAction
    fun pickDefaultActiveWbAction(): PickDefaultActiveWbAction
    fun saveWbAction(): SaveWorkbookAction
    fun getWorkbookAction(): GetWorkbookAction
    fun baseApplier(): BaseApplier
    fun closeWbAct(): CloseWorkbookAction
    fun setActiveWindowAction(): SetActiveWindowAction
    fun updateCellAction(): UpdateCellAction
    fun loadDataAction(): LoadDataAction
    fun removeAllCellAction(): RemoveAllCellAction
    fun removeAllWorksheetAction(): RemoveAllWorksheetAction
    fun multiCellUpdateAction(): MultiCellUpdateAction
    fun newWorksheetAction(): NewWorksheetAction
    fun computeSliderSizeAction(): ComputeSliderSizeAction
    fun rulerAction(): RulerAction
    fun worksheetAction2(): WorksheetAction2
    fun cursorEditorAction(): CellEditorAction
    fun textDiffer(): TextDiffer
    fun thumbStateFactory(): ThumbStateFactory
    fun dragThumbAction(): DragThumbAction
    fun endThumbDragAction(): EndThumbDragAction
    fun copyCellAction(): CopyCellAction
    fun partialTextElementExtractor(): PartialTextElementExtractor
    fun cycleFormulaLockStateAct(): CycleFormulaLockStateAction
    fun partialFormulaTreeExtractor(): PartialFormulaTreeExtractor
    fun colorFormulaActionImp(): ColorFormulaInCellEditorActionImp
    fun colorFormulaAction(): ColorFormulaInCellEditorAction
    fun rangeFormatter(): RangeAddressFormatter
    fun deleteMultiCellAction(): DeleteMultiCellAction
//    fun exUnitErrors(): ExUnitErrors

    @Component.Builder
    interface Builder {
        fun build(): TestComponent

        @BindsInstance
        fun messageApiComponent(@P6Singleton component: MessageApiComponent): Builder

        @BindsInstance
        fun username(@Username u: String): Builder

        @BindsInstance
        fun applicationCoroutineScope(@AppCoroutineScope scope: CoroutineScope): Builder

        @BindsInstance
        fun applicationScope(appScope: ApplicationScope?): Builder
    }
}
