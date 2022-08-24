package com.qxdzbc.common


interface WithSize : CanCheckEmpty{
    val size:Int
    override fun isEmpty():Boolean{
        return size == 0
    }
}
