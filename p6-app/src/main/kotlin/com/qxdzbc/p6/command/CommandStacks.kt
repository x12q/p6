package com.qxdzbc.p6.command

object CommandStacks {
    fun stdCommandStack(capacity:Int=100): CommandStack {
        return CommandStackImp(capacity)
    }
}
