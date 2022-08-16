package com.emeraldblast.p6.app.command

/**
 * A command is an encapsulation containing all the information/data needed to perform certain action, and undo that action.
 */
interface Command{
    /**
     * create a reversed command of this command
     */
    fun reverse():Command
    fun run()
    fun undo()
}

