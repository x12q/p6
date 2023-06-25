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
enum class RangeSelectorAllowState_Backup {
    /**
     * For when the cell editor is closed
     */
    NOT_AVAILABLE{
        override fun transit(
            text: String,
            inputChar: Char?,
            inputIndex: Int?,
            textCursorIndex:Int?,
            moveTextCursorWithMouse:Boolean,
            moveTextCursorWithKeyboard:Boolean,
        ): RangeSelectorAllowState_Backup {
            return this
        }
    },

    /**
     * For when the cell editor just open. This immediately handle state transition to ALLOW.
     *
     */
    START{
        override fun transit(
            text: String,
            inputChar: Char?,
            inputIndex: Int?,
            textCursorIndex:Int?,
            moveTextCursorWithMouse:Boolean,
            moveTextCursorWithKeyboard:Boolean,
        ): RangeSelectorAllowState_Backup {
            return ALLOW.transit(text, inputChar, inputIndex, textCursorIndex, moveTextCursorWithMouse, moveTextCursorWithKeyboard)
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
            moveTextCursorWithMouse: Boolean,
            moveTextCursorWithKeyboard: Boolean
        ): RangeSelectorAllowState_Backup {
            if(TextUtils.isFormula(text)){
                if(!moveTextCursorWithKeyboard && !moveTextCursorWithMouse){
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
     * Allow range selector with both mouse and keyboard navigation when:
     * -
     */
    ALLOW{
        override fun transit(
            text: String,
            inputChar: Char?,
            inputIndex: Int?,
            textCursorIndex:Int?,
            moveTextCursorWithMouse:Boolean,
            moveTextCursorWithKeyboard:Boolean,
        ): RangeSelectorAllowState_Backup {
            if(TextUtils.isFormula(text)){
                val newTextWasIntroduce = !moveTextCursorWithKeyboard && !moveTextCursorWithMouse
                if(newTextWasIntroduce){
                    if(inputChar in rangeSelectorActivationChars){
                        // Next char is an activation char -> allow, else -> not allow
                        // Next char does not exist -> that is end of line -> allow
                        val nextChar = inputIndex?.let{ text.getOrNull(it+1) }
                        if(nextChar==null){
                            return ALLOW
                        }else{
                            if(nextChar in rangeSelectorActivationChars){
                                return ALLOW
                            }else{
                                return DISALLOW
                            }
                        }
                    }else{
                        return DISALLOW
                    }
                }else{
                    // no new text was introduced => the text cursor was moved.
//                    val charIndex:Int? = textCursorIndex?.minus(1)
//                    val charAtCursor:Char? = charIndex?.let{
//                        text.getOrNull(it)
//                    }
//                    if(charAtCursor in rangeSelectorActivationChars){
//                        return ALLOW_MOUSE
//                    }else{
//                        return DISALLOW
//                    }
                    return DISALLOW
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
            moveTextCursorWithMouse:Boolean,
            moveTextCursorWithKeyboard:Boolean,
        ): RangeSelectorAllowState_Backup {
            // TODO this logic need update
            return ALLOW.transit(text, inputChar, inputIndex, textCursorIndex, moveTextCursorWithMouse, moveTextCursorWithKeyboard)
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
     * [inputChar] the latest input character. [inputChar] is already inside [text], but it could be any character, not just the last because users can edit formulas at any position within the formula text. [inputChar] can be null when transit is based on the change of text cursor and no new character is introduced into the text.
     *
     * [inputIndex] index of [inputChar] in [text]. If [inputChar] is null, [inputIndex] is also null, and indicates that no new character has been inputted.
     *
     * [textCursorIndex] text index of the text cursor in [text]. This is the index of the character that is on the left side of the text cursor. If [inputChar] is null, [textCursorIndex] is also null, indicating no new character has been inputted.
     *
     * TODO add null check for [inputChar],[inputIndex],[textCursorIndex]. They must be either all null, or all not null
     *
     * [moveTextCursorWithMouse] denotes if the text cursor was moved using mouses.
     *
     * [moveTextCursorWithKeyboard] denotes if the text cursor was moved using keyboard.
     */
    abstract fun transit(
        text:String,
        inputChar:Char?=null,
        inputIndex:Int?=null,
        textCursorIndex:Int?=null,
        moveTextCursorWithMouse:Boolean=false,
        moveTextCursorWithKeyboard:Boolean=false,
    ): RangeSelectorAllowState_Backup

    /**
     * Transit by inputting new text character.
     * [inputChar] is null when no new character was inputted.
     * [inputIndex] is null when no new character was inputted.
     */
    fun transitWithInput(text:String, inputChar: Char?=null, inputIndex: Int?=null):RangeSelectorAllowState_Backup{
        return this.transit(text,inputChar, inputIndex,inputIndex)
    }

    /**
     * Transit by moving the text cursor, either by clicking different position of the text
     * [cursorIndex] current index of the text cursor
     */
    fun transitByMovingTextCursor(
        text:String,
        cursorIndex: Int?,
        moveTextCursorWithMouse: Boolean= true,
        moveTextCursorWithKeyboard: Boolean=false,
    ):RangeSelectorAllowState_Backup{
        return transit(text,null,null,cursorIndex, moveTextCursorWithMouse, moveTextCursorWithKeyboard)
    }

    /**
     * Transit by moving the text cursor.
     * [selection] denote the position of the text cursor
     */
    fun transitByMovingTextCursor(
        text:String,
        selection: TextRange,
        moveTextCursorWithMouse: Boolean = true,
        moveTextCursorWithKeyboard: Boolean = false,
    ):RangeSelectorAllowState_Backup{
        if(selection.end == selection.start){
            return transitByMovingTextCursor(text,selection.end, moveTextCursorWithMouse, moveTextCursorWithKeyboard)
        }else{
            return DISALLOW
        }
    }
    fun isAllowed():Boolean{
        return this == ALLOW || this == ALLOW_MOUSE
    }
}
