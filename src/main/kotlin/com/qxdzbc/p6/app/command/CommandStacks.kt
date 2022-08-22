package com.qxdzbc.p6.app.command

object CommandStacks {
    fun stdCommandStack(capacity:Int=100):CommandStackImp{
        return CommandStackImp(capacity)
    }
}
