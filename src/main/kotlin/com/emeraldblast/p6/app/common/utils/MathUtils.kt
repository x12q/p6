package com.emeraldblast.p6.app.common.utils

object MathUtils {
    fun addIntOrDefault(x:Int, y:Int,default:Int = Int.MAX_VALUE):Int{
        return try{
            Math.addExact(x,y)
        }catch (e:ArithmeticException){
            return default
        }
    }
}
