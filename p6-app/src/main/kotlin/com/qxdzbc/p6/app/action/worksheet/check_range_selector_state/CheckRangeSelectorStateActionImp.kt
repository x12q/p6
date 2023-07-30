package com.qxdzbc.p6.app.action.worksheet.check_range_selector_state

import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Deprecated("to be deleted, don't use, kept just in case")
@Singleton
@ContributesBinding(P6AnvilScope::class)
class CheckRangeSelectorStateActionImp @Inject constructor() : CheckRangeSelectorStateAction {
    companion object{
        val rangeSelectorActivationChars = setOf('(','+','-','*','/',':','=','^','%')
        val activationStrs = setOf("()")
    }

    override fun check(selectorText: String, selectorCursorPosition: Int): Boolean {
        val pos = selectorCursorPosition
        val text = selectorText
        if(pos>=text.length){
            return checkAtLength(text)
        }else{
            val subStr = text.substring(0,pos)
            return checkAtLength(subStr)
//            val isFunction = text.firstOrNull() == '='
//            if(isFunction){
//                    val charBeforeCursor = selectorText[pos-1]
//                    val charAfterCursor = selectorText[pos]
//                    val str = "${charBeforeCursor}${charAfterCursor}"
//                    return str in activationStrs
//            }else{
//                return false
//            }
        }
    }

    fun checkAtLength(formula:String):Boolean{
        val isFunction = formula.firstOrNull() == '='
        if(isFunction){
            if(formula.length==1){
                return true
            }else{
                val last:Char? = formula.lastOrNull()
                return last in rangeSelectorActivationChars
            }
        }else{
            return false
        }
    }
}
