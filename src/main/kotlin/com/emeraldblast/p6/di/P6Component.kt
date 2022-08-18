package com.emeraldblast.p6.di

import androidx.compose.ui.window.ApplicationScope
import com.emeraldblast.p6.app.app_context.AppContext
import com.emeraldblast.p6.app.code.BackEndCommander
import com.emeraldblast.p6.app.coderunner.CodeRunner
import com.emeraldblast.p6.app.action.app.AppApplier
import com.emeraldblast.p6.app.action.cell.cell_update.applier.CellUpdateApplier
import com.emeraldblast.p6.app.communication.event.P6EventTable
import com.emeraldblast.p6.app.action.app.AppRM
import com.emeraldblast.p6.app.action.cell.CellRM
import com.emeraldblast.p6.app.action.cell_editor.open_cell_editor.OpenCellEditorAction
import com.emeraldblast.p6.app.action.window.WindowAction
import com.emeraldblast.p6.app.action.workbook.WorkbookAction
import com.emeraldblast.p6.app.action.worksheet.WorksheetAction
import com.emeraldblast.p6.app.action.worksheet.WorksheetApplier
import com.emeraldblast.p6.app.action.worksheet.WorksheetRM
import com.emeraldblast.p6.di.action.ActionModule
import com.emeraldblast.p6.di.state.app_state.AppStateModule
import com.emeraldblast.p6.di.state.app_state.AppStateMs
import com.emeraldblast.p6.di.state.app_state.WbContainerMs
import com.emeraldblast.p6.di.applier.ApplierModule
import com.emeraldblast.p6.di.document.DocumentModule
import com.emeraldblast.p6.di.request_maker.RMModule
import com.emeraldblast.p6.di.rpc.RpcModule
import com.emeraldblast.p6.di.status_bar.StatusBarModule
import com.emeraldblast.p6.app.document.wb_container.WorkbookContainer
import com.emeraldblast.p6.di.rpc.ReactiveRpcServerQualifier
import com.emeraldblast.p6.di.state.StateModule
import com.emeraldblast.p6.message.api.connection.kernel_context.KernelContext
import com.emeraldblast.p6.message.api.connection.kernel_services.KernelServiceManager
import com.emeraldblast.p6.message.di.MessageApiComponent
import com.emeraldblast.p6.rpc.P6RpcServer
import com.emeraldblast.p6.translator.jvm_translator.CellLiteralParser
import com.emeraldblast.p6.ui.app.ErrorRouter
import com.emeraldblast.p6.ui.app.action.AppAction
import com.emeraldblast.p6.ui.app.cell_editor.in_cell.actions.CellEditorAction
import com.emeraldblast.p6.app.action.worksheet.make_cell_editor_display_text.MakeCellEditorDisplayText
import com.emeraldblast.p6.ui.app.action.AppActionTable
import com.emeraldblast.p6.ui.app.state.AppState
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.document.cell.action.CellViewAction
import com.emeraldblast.p6.ui.document.workbook.action.WorkbookActionTable
import com.emeraldblast.p6.ui.document.workbook.state.WorkbookStateFactory
import com.emeraldblast.p6.ui.document.worksheet.action.WorksheetActionTable
import com.emeraldblast.p6.ui.document.worksheet.cursor.actions.CursorAction
import com.emeraldblast.p6.ui.document.worksheet.cursor.state.CursorStateFactory
import com.emeraldblast.p6.ui.document.worksheet.slider.LimitedGridSliderFactory
import com.emeraldblast.p6.ui.document.worksheet.state.WorksheetStateFactory
import com.emeraldblast.p6.ui.script_editor.action.CodeEditorActionTable
import com.emeraldblast.p6.ui.window.action.WindowActionTable
import com.emeraldblast.p6.ui.window.move_to_wb.MoveToWbAction
import com.emeraldblast.p6.ui.window.state.WindowStateFactory
import com.emeraldblast.p6.ui.window.workbook_tab.bar.WorkbookTabBarAction
import com.google.gson.Gson
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.CoroutineScope
import org.zeromq.ZContext
import org.zeromq.ZMQ


@P6Singleton
@Component(
    modules = [
        P6Module::class,
        UtilModule::class,
        RMModule::class,
        ApplierModule::class,
        MsgApiModule::class,
        ActionTableModule::class,
        AppStateModule::class,
        DocumentModule::class,
        TranslatorModule::class,
        StatusBarModule::class,
        ActionModule::class,
        RpcModule::class,
        StateModule::class,
    ]
)
interface P6Component {
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
    @ReactiveRpcServerQualifier
    fun p6RpcServer(): P6RpcServer

    @P6Singleton
    fun windowStateFactory(): WindowStateFactory

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
    fun cellEventApplier(): CellUpdateApplier


    @P6Singleton
    fun appEventApplier(): AppApplier

    @P6Singleton
    @AppStateMs
    fun appStateMs(): Ms<AppState>

    @P6Singleton
    @WbContainerMs
    fun wbContainerMs(): Ms<WorkbookContainer>

//    @P6Singleton
//    fun workbookEventApplier(): WorkbookEventApplier


    @P6Singleton
    fun worksheetEventApplier(): WorksheetApplier


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
    fun backEndCommander(): BackEndCommander

    @P6Singleton
    fun gson(): Gson

//    @P6Singleton
//    fun workbookRequestMaker(): WorkbookRM

    @P6Singleton
    fun worksheetRequestMaker(): WorksheetRM

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

    @ApplicationCoroutineScope
    fun executionScope(): CoroutineScope

    @P6Singleton
    fun wbTabBarAction(): WorkbookTabBarAction

    @P6Singleton
    fun cellEditorAction(): CellEditorAction

    @P6Singleton
    fun cellLiteralParser(): CellLiteralParser

    @P6Singleton
    fun cellViewAction(): CellViewAction
    @P6Singleton
    fun makeDisplayText(): MakeCellEditorDisplayText
    @P6Singleton
     fun openCellEditorAction(): OpenCellEditorAction

    @Component.Builder
    interface Builder {
        fun build(): P6Component

        @BindsInstance
        fun messageApiComponent(@P6Singleton component: MessageApiComponent): Builder

        @BindsInstance
        fun username(@com.emeraldblast.p6.di.Username u: String): Builder

        @BindsInstance
        fun applicationCoroutineScope(@com.emeraldblast.p6.di.ApplicationCoroutineScope scope: CoroutineScope): Builder

        @BindsInstance
        fun applicationScope(appScope: ApplicationScope?): Builder
    }
}
