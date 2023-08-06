package com.qxdzbc.p6.di.state

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.app.command.CommandStack
import com.qxdzbc.p6.app.command.CommandStacks
import com.qxdzbc.p6.di.state.app_state.AppStateModule
import com.qxdzbc.p6.ui.document.workbook.di.DefaultCommandStack
import com.qxdzbc.p6.ui.document.workbook.di.WorkbookStateModule

import dagger.Module
import dagger.Provides

@Module(
    includes = [
        AppStateModule::class,
    ]
)
interface StateModule

