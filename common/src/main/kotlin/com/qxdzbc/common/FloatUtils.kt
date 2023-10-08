package com.qxdzbc.common

object FloatUtils {

    /**
     * guard a float [i] within [range]. [varName] is for exception message
     */
    fun guardFloat(i:Float,range:ClosedFloatingPointRange<Float>, varName:String?=null){
        require(i in range){
            "${varName?:"variable"} must be between $range"
        }
    }

    /**
     * Guard float within range [0,1]
     */
    fun guardFloat01(i:Float,varName:String?=null){
        guardFloat(i,0f..1f,varName)
    }
}