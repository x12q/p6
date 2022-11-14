package com.qxdzbc.p6.app.action.cursor.undo_on_cursor

import androidx.compose.runtime.getValue
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class UndoOnCursorActionImp @Inject constructor(
    private val stateContSt:St<@JvmSuppressWildcards StateContainer>,
)  : UndoOnCursorAction {
    private val sc by stateContSt
    override fun undoOnCursor(wbws: WbWs) {
        val commandStack = sc.getWbState(wbws.wbKey)?.commandStack
        if(commandStack!=null){
            val command = commandStack.pop()
            command?.undo()
        }
    }
}
