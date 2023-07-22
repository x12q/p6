package com.qxdzbc.p6.di.state.ws.cursor

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils
import dagger.Provides
import org.antlr.v4.runtime.tree.ParseTree
import dagger.Module
@Module
interface CursorStateModule {
    companion object{
        @DefaultCursorParseTree
        @Provides
        fun defaultCursorParseTree():Ms<ParseTree?>{
            return StateUtils.ms(null)
        }
    }
}
