package com.qxdzbc.p6.common.utils

object TextUtils {
    /**
     * Check if a text is a formula or not.
     */
    fun isFormula(formula: String): Boolean {
        val script: String = formula.trim()
        val isFormula: Boolean = script.startsWith("=")
        return isFormula
    }
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
    fun extractFromSingleQuote(str: String): String {
        return str.substring(1, str.length - 1)
    }
}
