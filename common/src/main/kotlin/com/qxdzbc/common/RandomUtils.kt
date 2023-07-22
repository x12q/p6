package com.qxdzbc.common

import java.util.UUID

object RandomUtils {
    fun randomInt(range: IntRange = (1 .. 100)):Int{
        return range.random()
    }

    fun randomUUIDStr():String{
        return UUID.randomUUID().toString()
    }

    fun randomBool():Boolean{
        return (1..100).random() % 2 ==0
    }
}