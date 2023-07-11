package com.qxdzbc.p6.di

import androidx.compose.ui.window.ApplicationScope
import com.qxdzbc.p6.app.action.cell.CellRM
import com.qxdzbc.p6.app.action.window.WindowAction
import com.qxdzbc.p6.app.action.workbook.WorkbookAction
import com.qxdzbc.p6.app.action.worksheet.WorksheetAction
import com.qxdzbc.p6.app.app_context.AppContext
import com.qxdzbc.p6.app.document.wb_container.WorkbookContainer
import com.qxdzbc.p6.di.rpc.MsRpcServerQualifier


import com.qxdzbc.p6.rpc.P6RpcServer
import com.qxdzbc.p6.ui.app.action.AppAction
import com.qxdzbc.p6.ui.app.action.AppActionTable
import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.di.state.window.WindowFocusStateModule
import com.qxdzbc.p6.ui.document.workbook.action.WorkbookActionTable
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookStateFactory
import com.qxdzbc.p6.ui.document.worksheet.action.WorksheetActionTable
import com.qxdzbc.p6.ui.document.worksheet.cursor.actions.CursorAction
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorStateFactory
import com.qxdzbc.p6.ui.document.worksheet.slider.LimitedGridSliderFactory
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetStateFactory
import com.qxdzbc.p6.ui.window.action.WindowActionTable
import com.qxdzbc.p6.ui.window.move_to_wb.MoveToWbAction
import com.qxdzbc.p6.ui.window.state.OuterWindowStateFactory
import com.qxdzbc.p6.ui.window.state.WindowStateFactory
import com.squareup.anvil.annotations.MergeComponent
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.CoroutineScope


@P6Singleton
@MergeComponent(
    scope = P6AnvilScope::class,
    modules = [
        P6Module::class,
        WindowFocusStateModule::class,
    ],
)
interface P6Component {

    @P6Singleton
    fun appState():AppState

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
//
//    @P6Singleton
//    fun appStateMs(): Ms<AppState>

    @P6Singleton
    fun wbContainer(): WorkbookContainer

    @P6Singleton
    fun appContext(): AppContext

    @P6Singleton
    fun cellRequestMaker(): CellRM

    @EventServerPort
    fun eventServerPort(): Int

    @Component.Builder
    interface Builder {
        fun build(): P6Component

        @BindsInstance
        fun username(@Username u: String): Builder

        @BindsInstance
        fun applicationCoroutineScope(@AppCoroutineScope scope: CoroutineScope): Builder

        @BindsInstance
        fun applicationScope(appScope: ApplicationScope?): Builder
    }
}
