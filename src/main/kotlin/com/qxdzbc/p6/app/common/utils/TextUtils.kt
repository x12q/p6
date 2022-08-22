package com.qxdzbc.p6.app.common.utils

object TextUtils {

    fun cleanExecutionResultText(text:String):String{
        if(text.isEmpty()){
            return text
        }
        if(text.startsWith('\u0027')){
            // \u0027 is single quote (')
            val afterRemoveSingleQuote = removeFirstAndLastChar(text)
            val rt = afterRemoveSingleQuote.replace("\\\\\"","\\\"")
            return rt
        }else{
            return text
        }

    }
    fun removeFirstAndLastChar(str:String):String{
        if (str.isEmpty() || str.length <=2){
            return ""
        }
        val z = str.substring(1,str.length-1)
        return z
    }
}
