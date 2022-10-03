package com.qxdzbc.p6.ui.app.cell_editor

object CellEditorUtils {
    /**
     * characters that signal the enabling of range selector
     */
    val activationChars = arrayOf('(','+','-','*','/',':','=')
    fun allowSelector(formula:String):Boolean{
        val isFunction = formula.firstOrNull() == '='
        if(isFunction){
            if(formula.length==1){
                return true
            }else{
                val last:Char? = formula.lastOrNull()
                return last in activationChars
            }
        }else{
            return false
        }
    }
}
