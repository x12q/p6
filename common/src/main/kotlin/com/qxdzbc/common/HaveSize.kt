package com.qxdzbc.common


interface HaveSize : CanCheckEmpty {
    val size:Int
    override fun isEmpty(): Boolean {
        return size==0
    }
}
