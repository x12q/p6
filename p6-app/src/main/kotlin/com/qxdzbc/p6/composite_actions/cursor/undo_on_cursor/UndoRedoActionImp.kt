package com.qxdzbc.p6.composite_actions.cursor.undo_on_cursor

import androidx.compose.runtime.getValue
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.composite_actions.common_data_structure.WbWsSt
import com.qxdzbc.p6.command.CommandStack
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class)
class UndoRedoActionImp @Inject constructor(
    private val stateCont:StateContainer,
) : UndoRedoAction {
    private val sc  = stateCont

    override fun undoOnWorksheet(wbwsSt: WbWsSt) {
        val undoStackMs = sc.getUndoStackMs(wbwsSt)
        val redoStackMs = sc.getRedoStackMs(wbwsSt)
        undoOnCursor(undoStackMs, redoStackMs)
    }

    override fun redoOnWorksheet(wbwsSt: WbWsSt) {
        val undoStackMs = sc.getUndoStackMs(wbwsSt)
        val redoStackMs = sc.getRedoStackMs(wbwsSt)
        redoOnCursor(undoStackMs, redoStackMs)
    }

    fun redoOnCursor(undoStackMs: Ms<CommandStack>?, redoStackMs: Ms<CommandStack>?) {
        redoStackMs?.also {
            val redoStack by redoStackMs
            val command = redoStack.peek()
            command?.also {
                command.run()
                val newStack = redoStack.removeTop()
                redoStackMs.value = newStack
                undoStackMs?.also {
                    it.value = it.value.add(command)
                }
            }

        }
    }


    fun undoOnCursor(undoStackMs: Ms<CommandStack>?, redoStackMs: Ms<CommandStack>?) {
        if (undoStackMs != null) {
            val commandStack by undoStackMs
            val command = commandStack.peek()
            command?.also {
                command.undo()
                val newStack = commandStack.removeTop()
                undoStackMs.value = newStack
                redoStackMs?.also {
                    it.value = it.value.add(command)
                }
            }
        }
    }
}
