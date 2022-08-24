package com.qxdzbc.common

interface CanCheckEmpty {
    fun isEmpty():Boolean
    fun isNotEmpty():Boolean{
        return !this.isEmpty()
    }
}
