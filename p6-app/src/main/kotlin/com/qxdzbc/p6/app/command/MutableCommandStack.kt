package com.qxdzbc.p6.app.command

import java.util.*
import java.util.concurrent.LinkedBlockingDeque

/**
 * A mutable implementation of [CommandStack]
 */
@Deprecated("faulty, don't use, kept just in case")
class MutableCommandStack(capacity: Int = 100) : CommandStack {
    private val stack: Deque<Command> = LinkedBlockingDeque(capacity)
    override fun add(command: Command): CommandStack {
        this.stack.push(command)
        return this
    }

    override fun peek(): Command? {
        return this.stack.peekFirst()
    }

    override fun removeTop(): CommandStack {
        this.stack.pollFirst()
        return this
    }

    override operator fun contains(command: Command): Boolean {
        return command in this.stack
    }

    override val allCommands: List<Command>
        get() = TODO("Not yet implemented")

    override val size: Int
        get() = stack.size
}

