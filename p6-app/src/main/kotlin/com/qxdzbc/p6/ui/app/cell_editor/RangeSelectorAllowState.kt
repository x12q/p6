package com.qxdzbc.p6.ui.app.cell_editor

import androidx.compose.ui.text.TextRange

enum class RangeSelectorAllowState {
    NOT_AVAILABLE{
        override fun transit(
            text: String,
            inputChar: Char?,
            inputIndex: Int?,
            cursorIndex:Int?,
            moveCursorWithMouse:Boolean,
            moveCursorWithKeyboard:Boolean,
        ): RangeSelectorAllowState {
            return this
        }

    },
    START{
        override fun transit(
            text: String,
            inputChar: Char?,
            inputIndex: Int?,
            cursorIndex:Int?,
            moveCursorWithMouse:Boolean,
            moveCursorWithKeyboard:Boolean,
        ): RangeSelectorAllowState {
            return ALLOW.transit(text, inputChar, inputIndex, cursorIndex, moveCursorWithMouse, moveCursorWithKeyboard)
        }
    },
    ALLOW_MOUSE {
        override fun transit(
            text: String,
            inputChar: Char?,
            inputIndex: Int?,
            cursorIndex: Int?,
            moveCursorWithMouse: Boolean,
            moveCursorWithKeyboard: Boolean
        ): RangeSelectorAllowState {
            if(!moveCursorWithKeyboard && !moveCursorWithMouse){
                if(inputChar in rangeSelectorActivationChars){
                    val inputCharAtTheEnd = inputIndex == text.length-1
                    if(inputCharAtTheEnd){
                        return ALLOW
                    }else{
                        return ALLOW_MOUSE
                    }
                }else{
                    return DISALLOW
                }
            }else{
                val charIndex:Int? = cursorIndex?.minus(1)
                val charAtCursor:Char? = charIndex?.let{
                    text.getOrNull(it)
                }
                if(charAtCursor in rangeSelectorActivationChars){
                    return ALLOW_MOUSE
                }else{
                    return DISALLOW
                }
            }
        }
    },
    ALLOW{
        override fun transit(
            text: String,
            inputChar: Char?,
            inputIndex: Int?,
            cursorIndex:Int?,
            moveCursorWithMouse:Boolean,
            moveCursorWithKeyboard:Boolean,
        ): RangeSelectorAllowState {
            if(!moveCursorWithKeyboard && !moveCursorWithMouse){
                if(inputChar in rangeSelectorActivationChars){
                    return ALLOW
                }else{
                    return DISALLOW
                }
            }else{
                val charIndex:Int? = cursorIndex?.minus(1)
                val charAtCursor:Char? = charIndex?.let{
                    text.getOrNull(it)
                }
                if(charAtCursor in rangeSelectorActivationChars){
                    return ALLOW_MOUSE
                }else{
                    return DISALLOW
                }
            }
        }
    },
    DISALLOW{
        override fun transit(
            text: String,
            inputChar: Char?,
            inputIndex: Int?,
            cursorIndex:Int?,
            moveCursorWithMouse:Boolean,
            moveCursorWithKeyboard:Boolean,
        ): RangeSelectorAllowState {
            return ALLOW.transit(text, inputChar, inputIndex, cursorIndex, moveCursorWithMouse, moveCursorWithKeyboard)
        }
    }
    ;
    companion object{
        val rangeSelectorActivationChars = setOf('(','+','-','*','/',':','=','^','%')
    }
    abstract fun transit(
        text:String,
        inputChar:Char?=null,
        inputIndex:Int?=null,
        cursorIndex:Int?=null,
        moveCursorWithMouse:Boolean=false,
        moveCursorWithKeyboard:Boolean=false,
    ): RangeSelectorAllowState

    fun transitWithInput(text:String, inputChar: Char?=null, inputIndex: Int?=null):RangeSelectorAllowState{
        return this.transit(text,inputChar, inputIndex)
    }
    fun transitWithMovingCursor(
        text:String,
        cursorIndex: Int?,
        moveCursorWithMouse: Boolean= true,
        moveCursorWithKeyboard: Boolean=false,
    ):RangeSelectorAllowState{
        return transit(text,null,null,cursorIndex, moveCursorWithMouse, moveCursorWithKeyboard)
    }

    fun transitWithMovingCursor(
        text:String,
        selection: TextRange,
        moveCursorWithMouse: Boolean = true,
        moveCursorWithKeyboard: Boolean = false,
    ):RangeSelectorAllowState{
        if(selection.end == selection.start){
            return transitWithMovingCursor(text,selection.end, moveCursorWithMouse, moveCursorWithKeyboard)
        }else{
            return DISALLOW
        }
    }
    fun isAllow():Boolean{
        return this == ALLOW || this == ALLOW_MOUSE
    }
}
