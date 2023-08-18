package com.qxdzbc.common

object FloatUtils {
    fun guardFloat(i:Float,range:ClosedFloatingPointRange<Float>, varName:String?=null){
        require(i in range){
            "${varName?:"variable"} must be between $range"
        }
    }

    fun guardFloat01(i:Float,varName:String?=null){
        guardFloat(i,0f..1f,varName)
    }
}