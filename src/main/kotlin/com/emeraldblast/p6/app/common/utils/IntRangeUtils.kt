package com.emeraldblast.p6.app.common.utils

fun IntRange.add(i:Int):IntRange{
    return  this.first+i .. this.last+i
}

fun IntRange.dif():Int{
    return this.last-this.first
}
