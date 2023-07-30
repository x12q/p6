package com.qxdzbc.p6.di.state.wb

import androidx.compose.runtime.MutableState
import com.qxdzbc.p6.app.command.CommandStack
import com.qxdzbc.p6.app.command.CommandStacks
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetState
import dagger.Binds
import dagger.Provides
import dagger.Module

@Module
interface WorkbookStateModule {

    companion object
    {
        @Provides
        @DefaultCommandStack
        fun defaultCommandStack():Ms<CommandStack>{
            return ms(CommandStacks.stdCommandStack())
        }
    }

}
