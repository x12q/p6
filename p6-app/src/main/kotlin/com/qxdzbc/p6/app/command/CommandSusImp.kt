package com.qxdzbc.p6.app.command

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * A command that can execute suspend function. Haven't found any places that need this.
 */
abstract class CommandSusImp: Command {
    abstract val executionScope:CoroutineScope
    abstract val dispatcher: CoroutineDispatcher
    abstract suspend fun _run()
    abstract suspend fun _undo()


    override fun reverse(): Command {
        return object:CommandSusImp(
        ){
            override val executionScope = this@CommandSusImp.executionScope
            override val dispatcher = this@CommandSusImp.dispatcher
            override suspend fun _run() {
                this@CommandSusImp.undo()
            }
            override suspend fun _undo() {
                this@CommandSusImp.run()
            }
        }
    }

    override fun run() {
        executionScope.launch(dispatcher) {
            _run()
        }
    }

    override fun undo() {
        executionScope.launch(dispatcher) {
            _undo()
        }
    }
}
