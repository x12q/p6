package com.qxdzbc.p6.ui.app.cell_editor

import androidx.compose.ui.text.TextRange
import com.qxdzbc.p6.common.utils.TextUtils
import com.qxdzbc.p6.ui.app.cell_editor.actions.differ.TextDiffResult

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
    NOT_AVAILABLE {
        override fun transit(
            text: String,
            inputChar: Char?,
            inputIndex: Int?,
            textCursorIndex: Int?,
            moveTextCursorWithMouse: Boolean,
            moveTextCursorWithKeyboard: Boolean,
        ): RangeSelectorAllowState {
            return this
        }

        override fun transit(textDiffResult: TextDiffResult): RangeSelectorAllowState {
            return this
        }
    },

    /**
     * For when the cell editor just open. This immediately pass state transition handling to ALLOW.
     */
    START {
        override fun transit(
            text: String,
            inputChar: Char?,
            inputIndex: Int?,
            textCursorIndex: Int?,
            moveTextCursorWithMouse: Boolean,
            moveTextCursorWithKeyboard: Boolean,
        ): RangeSelectorAllowState {
            return commonTransit(
                text,
                inputChar,
                inputIndex,
                textCursorIndex,
                moveTextCursorWithMouse,
                moveTextCursorWithKeyboard
            )
        }

        override fun transit(textDiffResult: TextDiffResult): RangeSelectorAllowState {
            return this
        }
    },

    /**
     * Allow range selector but only with mouse action (click on cell, shift+click, drag-select on range).
     * ALLOW_MOUSE can transit to ALLOW_MOUSE, ALLOW, and DISALLOW.
     *  - ALLOW: when a new activation char was input, and the text cursor is either at the end of the line or between 2 activation chars.
     *  - ALLOW_MOUSE:
     *      - when the text cursor was moved, and it is at an activation char.
     *      - when a new activation char was input, but the text cursor is not at the end of the line, and not between 2 activation chars.
     *  - else: DISALLOW
     *
     */
    ALLOW_MOUSE {
        override fun transit(
            text: String,
            inputChar: Char?,
            inputIndex: Int?,
            textCursorIndex: Int?,
            moveTextCursorWithMouse: Boolean,
            moveTextCursorWithKeyboard: Boolean
        ): RangeSelectorAllowState {
            return commonTransit(
                text,
                inputChar,
                inputIndex,
                textCursorIndex,
                moveTextCursorWithMouse,
                moveTextCursorWithKeyboard
            )
        }

        override fun transit(textDiffResult: TextDiffResult): RangeSelectorAllowState {
            return this
        }
    },

    /**
     * ALLOW range selector with both mouse and keyboard navigation.
     * ALLOW can transit to DISALLOW, and ALLOW_MOUSE.
     *  - ALLOW: when a new activation char was input, and the text cursor is either at the end of the line or between 2 activation chars.
     *  - ALLOW_MOUSE:
     *      - when the text cursor was moved, and it is at an activation char.
     *      - when a new activation char was input, but the text cursor is not at the end of the line, and not between 2 activation chars.
     *  - else: DISALLOW
     */
    ALLOW {
        override fun transit(
            text: String,
            inputChar: Char?,
            inputIndex: Int?,
            textCursorIndex: Int?,
            moveTextCursorWithMouse: Boolean,
            moveTextCursorWithKeyboard: Boolean,
        ): RangeSelectorAllowState {
            return commonTransit(
                text,
                inputChar,
                inputIndex,
                textCursorIndex,
                moveTextCursorWithMouse,
                moveTextCursorWithKeyboard
            )
        }

        override fun transit(textDiffResult: TextDiffResult): RangeSelectorAllowState {
            return this
        }
    },

    /**
     * Range selector is on, but is disallowed to move or do anything.
     * DISALLOW can transit to DISALLOW, ALLOW, and ALLOW_MOUSE
     *  - ALLOW: when a new activation char was input, and the text cursor is either at the end of the line or between 2 activation chars.
     *  - ALLOW_MOUSE:
     *      - when the text cursor was moved, and it is at an activation char.
     *      - when a new activation char was input, but the text cursor is not at the end of the line, and not between 2 activation chars.
     *  - else: DISALLOW
     */
    DISALLOW {
        override fun transit(
            text: String,
            inputChar: Char?,
            inputIndex: Int?,
            textCursorIndex: Int?,
            moveTextCursorWithMouse: Boolean,
            moveTextCursorWithKeyboard: Boolean,
        ): RangeSelectorAllowState {
            return commonTransit(
                text,
                inputChar,
                inputIndex,
                textCursorIndex,
                moveTextCursorWithMouse,
                moveTextCursorWithKeyboard
            )
        }

        override fun transit(textDiffResult: TextDiffResult): RangeSelectorAllowState {
            return this
        }
    }
    ;

    /**
     * Transit from one range selector state to another.
     *
     * [text] is the current text.
     *
     * [inputChar] the latest input character. [inputChar] is already inside [text], but it could be any character, not just the last because users can edit formulas at any position within the formula text. [inputChar] can be null when transit is based on changing of text cursor and no new character is introduced into the text.
     *
     * [inputIndex] index of [inputChar] in [text]. If [inputChar] is null, [inputIndex] is also null, and indicates that no new character has been inputted.
     *
     * [textCursorIndex] is the text index of the text cursor in [text]. This is the index of the character that is on the right side of the text cursor. If this is null, then the line is empty.
     *
     * [moveTextCursorWithMouse] denotes if the text cursor was moved using mouses.
     *
     * [moveTextCursorWithKeyboard] denotes if the text cursor was moved using keyboard.
     */
    abstract fun transit(
        text: String,
        inputChar: Char?,
        inputIndex: Int?,
        textCursorIndex: Int?,
        moveTextCursorWithMouse: Boolean,
        moveTextCursorWithKeyboard: Boolean,
    ): RangeSelectorAllowState

    abstract fun transit(
        textDiffResult: TextDiffResult
    ): RangeSelectorAllowState

    /**
     * Transit by inputting new text character.
     * [inputChar] is null when no new character was inputted.
     * [inputIndex] is null when no new character was inputted.
     */
    fun transitWithInput(text: String, inputChar: Char? = null, inputIndex: Int? = null): RangeSelectorAllowState {
        return this.transit(
            text = text,
            inputChar = inputChar,
            inputIndex = inputIndex,
            textCursorIndex = inputIndex,
            moveTextCursorWithMouse = false,
            moveTextCursorWithKeyboard = false
        )
    }

    /**
     * Transit by moving the text cursor using the mouse.
     * [cursorIndex] current index of the text cursor
     */
    fun transitByMovingTextCursor(
        text: String,
        cursorIndex: Int?,
    ): RangeSelectorAllowState {
        return transit(
            text = text,
            inputChar = null,
            inputIndex = null,
            textCursorIndex = cursorIndex,
            moveTextCursorWithMouse = true,
            moveTextCursorWithKeyboard = false
        )
    }

    /**
     * Transit by moving the text cursor.
     * [selection] denote the position of the text cursor
     */
    fun transitByMovingTextCursor(
        text: String,
        selection: TextRange,
    ): RangeSelectorAllowState {
        if (selection.end == selection.start) {
            return transitByMovingTextCursor(text = text, cursorIndex = selection.end)
        } else {
            return DISALLOW
        }
    }

    fun isAllowedOrAllowMouse(): Boolean {
        return this == ALLOW || this == ALLOW_MOUSE
    }

    companion object {
        /**
         * Activation characters are ones that allow enabling a range selector while editing.
         */
        val rangeSelectorActivationChars = setOf('(', '+', '-', '*', '/', ':', '=', '^', '%', ',')

        fun commonTransit(
            text: String,
            inputChar: Char?,
            inputIndex: Int?,
            textCursorIndex: Int?,
            moveTextCursorWithMouse: Boolean,
            moveTextCursorWithKeyboard: Boolean,
        ): RangeSelectorAllowState {
            if (TextUtils.isFormula(text)) {
                val hasNewChar = inputChar != null
                val notMoved = !moveTextCursorWithMouse && !moveTextCursorWithKeyboard
                if (hasNewChar && notMoved) {
                    if (inputChar in rangeSelectorActivationChars) {
                        val nextChar = inputIndex?.let { text.getOrNull(it + 1) }
                        if (nextChar in rangeSelectorActivationChars || nextChar == null) {
                            return ALLOW
                        } else {
                            return ALLOW_MOUSE
                        }
                    }
                }

                val wasMoved = moveTextCursorWithKeyboard || moveTextCursorWithMouse
                if (wasMoved) {
                    val charOnTheLeftOfTextCursor = textCursorIndex?.let { text.getOrNull(it - 1) }
                    if (charOnTheLeftOfTextCursor in rangeSelectorActivationChars) {
                        return ALLOW_MOUSE
                    }
                }

                val textWasDeleted = notMoved && !hasNewChar
                // this is when deletion happens
                if (textWasDeleted) {
                    val charOnTheLeftOfTextCursor = textCursorIndex?.let { text.getOrNull(it - 1) }
                    if (charOnTheLeftOfTextCursor in rangeSelectorActivationChars) {
                        return ALLOW_MOUSE
                    }
                }
            }
            return DISALLOW
        }
    }
}
