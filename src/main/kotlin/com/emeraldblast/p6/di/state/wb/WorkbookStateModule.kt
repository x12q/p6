package com.emeraldblast.p6.di.state.wb

import com.emeraldblast.p6.app.command.CommandStack
import com.emeraldblast.p6.app.command.CommandStacks
import com.emeraldblast.p6.app.document.script.ScriptContainer
import com.emeraldblast.p6.app.document.script.ScriptContainerImp
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.common.compose.ms
import com.emeraldblast.p6.ui.document.worksheet.state.WorksheetState
import dagger.Binds
import dagger.Provides

@dagger.Module
interface WorkbookStateModule {
    @Binds
    fun ScriptContainer(i:ScriptContainerImp): ScriptContainer


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
        @DefaultWsStateList
        fun z():List<Ms<WorksheetState>>{
            return emptyList()
        }
    }

}
