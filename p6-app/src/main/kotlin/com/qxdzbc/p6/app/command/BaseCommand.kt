package com.qxdzbc.p6.app.command

/**
 * The base class of all [Command]
 */
abstract class BaseCommand : Command {
    override fun reverse(): Command {
        val r= this
        return object: Command {
            override fun reverse(): Command {
                return r
            }

            override fun run() {
                return r.undo()
            }

            override fun undo() {
                return r.run()
            }
        }
    }
}
