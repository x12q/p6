package com.qxdzbc.p6.ui.app.cell_editor

import androidx.compose.ui.text.TextRange
import com.qxdzbc.p6.app.common.utils.TextUtils

/**
 * Range se
 */
enum class RangeSelectorAllowState {
    /**
     * For when the cell editor is closed
     */
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

    /**
     * For when the cell editor just open
     */
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

    /**
     * Allow range selector but only with mouse action (click on cell, shift+click, drag-select on range)
     */
    ALLOW_MOUSE {
        override fun transit(
            text: String,
            inputChar: Char?,
            inputIndex: Int?,
            cursorIndex: Int?,
            moveCursorWithMouse: Boolean,
            moveCursorWithKeyboard: Boolean
        ): RangeSelectorAllowState {
            if(TextUtils.isFormula(text)){
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

            }else{
                return DISALLOW
            }
        }
    },

    /**
     * Allow range selector with both mouse and keyboard navigation
     */
    ALLOW{
        override fun transit(
            text: String,
            inputChar: Char?,
            inputIndex: Int?,
            cursorIndex:Int?,
            moveCursorWithMouse:Boolean,
            moveCursorWithKeyboard:Boolean,
        ): RangeSelectorAllowState {
            if(TextUtils.isFormula(text)){
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

            }else{
                return DISALLOW
            }
        }
    },

    /**
     * Disallow range selector
     */
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
        /**
         * Activation characters are ones that allow enabling a range selector while editing.
         */
        val rangeSelectorActivationChars = setOf('(','+','-','*','/',':','=','^','%')
    }

    /**
     * Transit from one range selector state to another.
     *
     * [text] is the current text.
     * [inputChar] the latest input character. [inputChar] is already inside [text], but it could be any character, not just the last because users can edit formulas at any point in the formula text.
     *
     * [inputIndex] index of [inputChar] in [text]
     *
     * [cursorIndex] text index of the text cursor in [text]. This is the index of the character that is on the left side of the text cursor.
     *
     * [moveCursorWithMouse] denotes if the cell cursor was moved using mouses.
     *
     * [moveCursorWithKeyboard] denotes if the cell cursor was moved using keyboard.
     */
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
    fun transitWithMovingCellCursor(
        text:String,
        cursorIndex: Int?,
        moveCursorWithMouse: Boolean= true,
        moveCursorWithKeyboard: Boolean=false,
    ):RangeSelectorAllowState{
        return transit(text,null,null,cursorIndex, moveCursorWithMouse, moveCursorWithKeyboard)
    }

    fun transitWithMovingCellCursor(
        text:String,
        selection: TextRange,
        moveCursorWithMouse: Boolean = true,
        moveCursorWithKeyboard: Boolean = false,
    ):RangeSelectorAllowState{
        if(selection.end == selection.start){
            return transitWithMovingCellCursor(text,selection.end, moveCursorWithMouse, moveCursorWithKeyboard)
        }else{
            return DISALLOW
        }
    }
    fun isAllowed():Boolean{
        return this == ALLOW || this == ALLOW_MOUSE
    }
}
