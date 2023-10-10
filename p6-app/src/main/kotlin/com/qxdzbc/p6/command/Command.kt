package com.qxdzbc.p6.command

/**
 * A command is an encapsulation containing all the information/data needed to perform and undo an action.
 * To create new Commands, create anonymous objects from [BaseCommand].
 */
interface Command{
    /**
     * Create a reversed command of this command
     */
    fun reverse(): Command

    /**
     * Run this command
     */
    fun run()

    /**
     * Undo this command
     */
    fun undo()
}

