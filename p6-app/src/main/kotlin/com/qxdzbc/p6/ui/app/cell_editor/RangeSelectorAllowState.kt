package com.qxdzbc.p6.ui.app.cell_editor

enum class RangeSelectorAllowState {
    NOT_AVAILABLE{
        override fun transit(
            text: String,
            inputChar: Char,
            inputIndex: Int,
            moveCursor: Boolean
        ): RangeSelectorAllowState {
            return this
        }

    },
    START{
        override fun transit(
            text: String,
            inputChar: Char,
            inputIndex: Int,
            moveCursor: Boolean
        ): RangeSelectorAllowState {
            if(inputChar in rangeSelectorActivationChars){
                return ALLOW
            }else{
                return DISALLOW
            }
        }
    },
    ALLOW{
        override fun transit(
            text: String,
            inputChar: Char,
            inputIndex: Int,
            moveCursor: Boolean
        ): RangeSelectorAllowState {
            val newInputTextIsAtTheEnd = inputIndex == text.length-1
            if(inputChar in rangeSelectorActivationChars && newInputTextIsAtTheEnd){
                if(moveCursor){
                    return DISALLOW
                }else{
                    return this
                }
            }else{
                return DISALLOW
            }
        }
    },
    DISALLOW{
        override fun transit(
            text: String,
            inputChar: Char,
            inputIndex: Int,
            moveCursor: Boolean
        ): RangeSelectorAllowState {
            val newInputTextIsAtTheEnd = inputIndex == text.length-1
            if(inputChar in rangeSelectorActivationChars && newInputTextIsAtTheEnd){
                return ALLOW
            }else{
                return this
            }
        }
    }
    ;
    companion object{
        val rangeSelectorActivationChars = setOf('(','+','-','*','/',':','=','^','%')
    }
    abstract fun transit(
        text:String,
        inputChar:Char,
        inputIndex:Int,
        moveCursor:Boolean,
    ): RangeSelectorAllowState
}
