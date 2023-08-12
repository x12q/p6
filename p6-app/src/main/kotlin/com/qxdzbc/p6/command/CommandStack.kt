package com.qxdzbc.p6.command

import com.qxdzbc.common.HaveSize

/**
 * A stack of [Command]
 */
interface CommandStack : HaveSize {
    /**
     * Add a command to the top of the stack
     */
    fun add(command: Command): CommandStack

    /**
     * peek the top command
     */
    fun peek(): Command?

    fun removeTop(): CommandStack

    operator fun contains(command: Command):Boolean

    val allCommands:List<Command>
}


