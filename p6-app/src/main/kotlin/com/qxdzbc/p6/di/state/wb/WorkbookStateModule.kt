package com.qxdzbc.p6.di.state.wb

import androidx.compose.runtime.MutableState
import com.qxdzbc.p6.app.command.CommandStack
import com.qxdzbc.p6.app.command.CommandStacks
import com.qxdzbc.p6.app.document.script.ScriptContainer
import com.qxdzbc.p6.app.document.script.ScriptContainerImp
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetState
import dagger.Binds
import dagger.Provides

@dagger.Module
interface WorkbookStateModule {
//    @Binds
//    fun ScriptContainer(i:ScriptContainerImp): ScriptContainer

    companion object
    {
        @Provides
        @DefaultScriptContMs
        fun DefaultScriptContMs():Ms<ScriptContainer>{
            return ms(ScriptContainerImp())
        }
        @Provides
        @DefaultCommandStack
        fun d():Ms<CommandStack>{
            return ms(CommandStacks.stdCommandStack())
        }
        @Provides
        @DefaultWsStateMap
        fun z():Map<St<String>, MutableState<WorksheetState>>{
            return emptyMap()
        }
    }

}
