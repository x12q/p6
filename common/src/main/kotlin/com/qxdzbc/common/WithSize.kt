package com.qxdzbc.common


/**
 * Something with size in Int
 */
interface WithSize : CanCheckEmpty{
    val size:Int
    override fun isEmpty():Boolean{
        return size == 0
    }
}
