package com.qxdzbc.p6.app.command

object Commands {

    fun makeCommand(run: () -> Unit, undo: () -> Unit): Command {
        return object : BaseCommand() {
            override fun run() {
                return run()
            }

            override fun undo() {
                return undo()
            }
        }
    }
}
