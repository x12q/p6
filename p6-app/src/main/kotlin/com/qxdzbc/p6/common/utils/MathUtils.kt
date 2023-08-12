package com.qxdzbc.p6.common.utils

object MathUtils {
    /**
     * try computing the sum of 2 int, if that fail, return the default value. This is used in preventing overflow problem.
     */
    fun addIntOrDefault(x:Int, y:Int,default:Int = Int.MAX_VALUE):Int{
        return try{
            Math.addExact(x,y)
        }catch (e:ArithmeticException){
            return default
        }
    }
}
