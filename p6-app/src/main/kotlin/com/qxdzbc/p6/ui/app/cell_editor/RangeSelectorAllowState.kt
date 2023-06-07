package com.qxdzbc.p6.ui.app.cell_editor

import androidx.compose.ui.text.TextRange
import com.qxdzbc.p6.app.common.utils.TextUtils

/**
 * Allow state of range selector.
 * A range selector state is decide by:
 *  - the just-input character
 *  - the before and after cursor texts.
 *  - when a range selector is turned off, it can't be turned on again until a new character is inputted.??
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
            textCursorIndex:Int?,
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
            textCursorIndex:Int?,
            moveCursorWithMouse:Boolean,
            moveCursorWithKeyboard:Boolean,
        ): RangeSelectorAllowState {
            return ALLOW.transit(text, inputChar, inputIndex, textCursorIndex, moveCursorWithMouse, moveCursorWithKeyboard)
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
            textCursorIndex: Int?,
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
                    val charIndex:Int? = textCursorIndex?.minus(1)
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
     * Allow range selector with both mouse and keyboard navigation.
     */
    ALLOW{
        override fun transit(
            text: String,
            inputChar: Char?,
            inputIndex: Int?,
            textCursorIndex:Int?,
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
                    val charIndex:Int? = textCursorIndex?.minus(1)
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
     * Range selector is on, but is disallowed to move or do anything
     */
    DISALLOW{
        override fun transit(
            text: String,
            inputChar: Char?,
            inputIndex: Int?,
            textCursorIndex:Int?,
            moveCursorWithMouse:Boolean,
            moveCursorWithKeyboard:Boolean,
        ): RangeSelectorAllowState {
            // TODO this logic need update
            return ALLOW.transit(text, inputChar, inputIndex, textCursorIndex, moveCursorWithMouse, moveCursorWithKeyboard)
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
     * [inputChar] the latest input character. [inputChar] is already inside [text], but it could be any character, not just the last because users can edit formulas at any point in the formula text. [inputChar] can be null when transit is based on the change of text cursor and no new character is introduced into the text.
     *
     * [inputIndex] index of [inputChar] in [text]. If [inputChar] is null, [inputIndex] is also null, and indicates that no new character has been inputted.
     *
     * [textCursorIndex] text index of the text cursor in [text]. This is the index of the character that is on the left side of the text cursor. If [inputChar] is null, [textCursorIndex] is also null, indicating no new character has been inputted.
     *
     * TODO add null check for [inputChar],[inputIndex],[textCursorIndex]. They must be either all null, or all not null
     *
     * [moveCursorWithMouse] denotes if the range selector cursor was moved using mouses.
     *
     * [moveCursorWithKeyboard] denotes if the range selector cursor was moved using keyboard.
     */
    abstract fun transit(
        text:String,
        inputChar:Char?=null,
        inputIndex:Int?=null,
        textCursorIndex:Int?=null,
        moveCursorWithMouse:Boolean=false,
        moveCursorWithKeyboard:Boolean=false,
    ): RangeSelectorAllowState

    /**
     * Transit by inputting new text character.
     * [inputChar] is null when no new character was inputted.
     * [inputIndex] is null when no new character was inputted.
     */
    fun transitWithInput(text:String, inputChar: Char?=null, inputIndex: Int?=null):RangeSelectorAllowState{
        return this.transit(text,inputChar, inputIndex,inputIndex)
    }

    /**
     * Transit by moving the text cursor, either by clicking different position of the text
     * [cursorIndex] current index of the text cursor
     */
    fun transitByMovingTextCursor(
        text:String,
        cursorIndex: Int?,
        moveCursorWithMouse: Boolean= true,
        moveCursorWithKeyboard: Boolean=false,
    ):RangeSelectorAllowState{
        return transit(text,null,null,cursorIndex, moveCursorWithMouse, moveCursorWithKeyboard)
    }

    /**
     * Transit by moving the text cursor.
     * [selection] denote the position of the text cursor
     */
    fun transitByMovingTextCursor(
        text:String,
        selection: TextRange,
        moveCursorWithMouse: Boolean = true,
        moveCursorWithKeyboard: Boolean = false,
    ):RangeSelectorAllowState{
        if(selection.end == selection.start){
            return transitByMovingTextCursor(text,selection.end, moveCursorWithMouse, moveCursorWithKeyboard)
        }else{
            return DISALLOW
        }
    }
    fun isAllowed():Boolean{
        return this == ALLOW || this == ALLOW_MOUSE
    }
}
