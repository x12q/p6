package com.qxdzbc.p6.app.command

/**
 * An immutable implementation of [CommandStack]
 */
data class ImmutableCommandStack(
    private val capacity:Int = 100,
    override val allCommands : List<Command> = emptyList(),
) : CommandStack {

    override fun add(command: Command): CommandStack {
        val shavedList =if(capacity >0 && allCommands.size >=capacity){
            allCommands.subList(0,capacity-1)
        }else{
            allCommands
        }
        val newList = listOf(command) + shavedList
        return this.copy(allCommands=newList)
    }

    override fun peek(): Command? {
        return this.allCommands.firstOrNull()
    }

    override fun removeTop(): CommandStack {
        if(this.isNotEmpty()){
            return this.copy(
                allCommands = allCommands.subList(1,allCommands.size)
            )
        }else{
            return this
        }
    }

    override operator fun contains(command: Command): Boolean {
        return command in allCommands
    }

    override val size: Int
        get() = allCommands.size
}

