package com.qxdzbc.p6.translator.autocomplete

@Deprecated("dont use, kept just in case")
interface FormulaAutoCompleter {
    /**
     * add missing tailing parenthesis
     */
    fun completeParenthesis(formula:String):String
}
