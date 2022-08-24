package com.qxdzbc.p6.app.command

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
     * pop the top command
     */
    fun pop():Command?
}


