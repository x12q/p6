package com.qxdzbc.p6.di

import androidx.compose.ui.window.ApplicationScope
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
import com.qxdzbc.p6.ui.window.di.WindowFocusStateModule
import com.qxdzbc.p6.ui.document.workbook.action.WorkbookActionTable
import com.qxdzbc.p6.ui.document.workbook.state.factory.WorkbookStateFactory
import com.qxdzbc.p6.ui.document.worksheet.action.WorksheetActionTable
import com.qxdzbc.p6.ui.document.worksheet.cursor.actions.CursorAction
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorStateFactory
import com.qxdzbc.p6.ui.document.worksheet.slider.LimitedGridSliderFactory
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetStateFactory
import com.qxdzbc.p6.ui.window.action.WindowActionTable
import com.qxdzbc.p6.ui.window.action.move_focus_to_wb.MoveFocusToWbAction
import com.qxdzbc.p6.ui.window.di.comp.WindowComponent
import com.qxdzbc.p6.ui.window.state.OuterWindowStateFactory
import com.qxdzbc.p6.ui.window.state.WindowStateFactory
import com.squareup.anvil.annotations.MergeComponent
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton


@Singleton
@MergeComponent(
    scope = P6AnvilScope::class,
    modules = [
        P6Module::class,
        WindowFocusStateModule::class,
        ModuleForSubComponentsForP6Component::class,
    ],
)
interface P6Component {


    fun windowCompBuilder():WindowComponent.Builder

    @Singleton
    fun appState():AppState

    @Singleton
    fun moveToWbAction(): MoveFocusToWbAction

    @Singleton
    fun workbookAction(): WorkbookAction

    @Singleton
    fun cursorAction(): CursorAction

    @Singleton
    fun cursorStateFactory(): CursorStateFactory

    @Singleton
    fun errorRouter(): ErrorRouter

    @Singleton
    fun workbookStateFactory(): WorkbookStateFactory

    @Singleton
    fun worksheetStateFactory(): WorksheetStateFactory

    @Singleton
    fun gridSliderFactory(): LimitedGridSliderFactory

    @Singleton
    @MsRpcServerQualifier
    fun p6RpcServer(): P6RpcServer

    @Singleton
    fun windowStateFactory(): WindowStateFactory

    @Singleton
    fun outerWindowStateFactory(): OuterWindowStateFactory

    fun appAction(): AppAction

    val appActionTable: AppActionTable

    fun applicationScope(): ApplicationScope?

    @Singleton
    fun windowActionTable(): WindowActionTable

    @Singleton
    fun windowAction(): WindowAction

    @Singleton
    fun workbookActionTable(): WorkbookActionTable

    @Singleton
    fun worksheetActionTable(): WorksheetActionTable

    @Singleton
    fun wsAction(): WorksheetAction
//
//    @Singleton
//    fun appStateMs(): Ms<AppState>

    @Singleton
    fun wbContainer(): WorkbookContainer

    @Singleton
    fun appContext(): AppContext

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
