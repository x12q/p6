package com.emeraldblast.p6.app.common.utils

import com.emeraldblast.p6.common.CanCheckEmpty

interface WithSize : CanCheckEmpty{
    val size:Int
    override fun isEmpty():Boolean{
        return size == 0
    }
}
