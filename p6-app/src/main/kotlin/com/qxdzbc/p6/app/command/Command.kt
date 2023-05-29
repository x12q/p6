package com.qxdzbc.p6.app.command

/**
 * A command is an encapsulation containing all the information/data needed to perform and undo an action.
 */
interface Command{
    /**
     * create a reversed command of this command
     */
    fun reverse():Command
    fun run()
    fun undo()
}

