package com.qxdzbc.p6.translator.autocomplete

import javax.inject.Inject

class FormulaAutoCompleterImp @Inject constructor() : FormulaAutoCompleter {

    override fun completeParenthesis(formula: String): String {
        // how many parenthesis to add at the end
        var pCount = 0
        for(c:Char in formula){
            when(c){
                '('->pCount++
                ')' -> pCount--
            }
        }
        pCount = maxOf(pCount, 0)

        return formula + ")".repeat(pCount)
    }
}
