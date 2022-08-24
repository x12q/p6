package com.qxdzbc.common
object IntRangeUtils{
    /**
     * Add an int to both first and last value of a range, return a new range
     */
    fun IntRange.add(i:Int):IntRange{
        return  this.first+i .. this.last+i
    }

    /**
     * compute the dif between the last and first number in an IntRange
     */
    fun IntRange.dif():Int{
        return this.last-this.first
    }
}

