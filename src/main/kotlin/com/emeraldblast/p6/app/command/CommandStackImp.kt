package com.emeraldblast.p6.app.command

import java.util.*
import java.util.concurrent.LinkedBlockingDeque

/**
 * An immutable implementation of [CommandStack]
 */
class CommandStackImp(capacity: Int = 100) : CommandStack {
    private val stack: Deque<Command> = LinkedBlockingDeque(capacity)
    override fun add(command: Command): CommandStack {
        this.stack.push(command)
        return this
    }

    override fun pop(): Command? {
        return this.stack.pollFirst()
    }

    override val size: Int
        get() = stack.size
}

