package com.qxdzbc.p6.ui.app.cell_editor

import com.qxdzbc.common.test_util.TestSplitter
import kotlin.test.*
import com.qxdzbc.p6.ui.app.cell_editor.RangeSelectorAllowState.*
import io.kotest.matchers.shouldBe

internal class RangeSelectorAllowStateTest : TestSplitter() {

    @Test
    fun `test delete from end to act char, should be allow mouse`(){
        val state = START
        state.transit(
            text = "=A1+B2+",
            inputChar = null,
            inputIndex = null,
            textCursorIndex = 7, // text cursor at the end of the line
            moveTextCursorWithMouse = false,
            moveTextCursorWithKeyboard = false
        ) shouldBe ALLOW_MOUSE
    }


    @Test
    fun `test START`() {
        val start = START
        // x: input activation char "+" at the end
        val s1 = start.transit(
            text = "=abc+",
            inputChar = '+',
            inputIndex = 3,
            textCursorIndex = null,
            moveTextCursorWithMouse = false,
            moveTextCursorWithKeyboard = false
        )
        s1 shouldBe ALLOW

        // Input activation char "/" at the end
        val s2 = s1.transit(
            text = "=abc+/",
            inputChar = '/',
            inputIndex = 4,
            textCursorIndex = null,
            moveTextCursorWithMouse = false,
            moveTextCursorWithKeyboard = false
        )
        s2 shouldBe ALLOW
        // Input non-activation char "q" at the end
        s2.transit(
            text = "=abc+/q",
            inputChar = 'q',
            inputIndex = 5,
            textCursorIndex = null,
            moveTextCursorWithMouse = false,
            moveTextCursorWithKeyboard = false
        ) shouldBe DISALLOW
    }

    @Test
    fun `test ALLOW_MOUSE`() {
        val allowMouse = ALLOW_MOUSE

        // Input activation char at the end
        allowMouse.transit(
            text = "=abc+",
            inputChar = '+',
            inputIndex = 4,
            textCursorIndex = null,
            moveTextCursorWithMouse = false,
            moveTextCursorWithKeyboard = false
        ) shouldBe ALLOW

        // Input activation char in the middle
        allowMouse.transit(
            text = "=ab+c",
            inputChar = '+',
            inputIndex = 3,
            textCursorIndex = 4,
            moveTextCursorWithMouse = false,
            moveTextCursorWithKeyboard = false
        ) shouldBe ALLOW_MOUSE

        // Input non-activation char at the end
        allowMouse.transit(
            text = "=abc+q",
            inputChar = 'q',
            inputIndex = 5,
            textCursorIndex = null,
            moveTextCursorWithMouse = false,
            moveTextCursorWithKeyboard = false
        ) shouldBe DISALLOW

        // Move cursor to activation char

        allowMouse.transit(
            text = "=abc+q",
            inputChar = null,
            inputIndex = null,
            textCursorIndex = 5,
            moveTextCursorWithMouse = true,
            moveTextCursorWithKeyboard = false
        ) shouldBe ALLOW_MOUSE

        allowMouse.transit(
            text = "=abc+q",
            inputChar = null,
            inputIndex = null,
            textCursorIndex = 5,
            moveTextCursorWithMouse = false,
            moveTextCursorWithKeyboard = true
        ) shouldBe ALLOW_MOUSE


        // Move cursor to non-activation char
        allowMouse.transit(
            text = "=abc+q",
            inputChar = null,
            inputIndex = null,
            textCursorIndex = 3,
            moveTextCursorWithMouse = false,
            moveTextCursorWithKeyboard = true
        ) shouldBe DISALLOW

        allowMouse.transit(
            text = "=abc+q",
            inputChar = null,
            inputIndex = null,
            textCursorIndex = 3,
            moveTextCursorWithMouse = true,
            moveTextCursorWithKeyboard = false
        ) shouldBe DISALLOW
    }

    @Test
    fun `test ALLOW`() {
        val allow = ALLOW

        // input activation char at the end
        allow.transit(
            text = "=abc+",
            inputChar = '+',
            inputIndex = 3,
            textCursorIndex = null,
            moveTextCursorWithMouse = false,
            moveTextCursorWithKeyboard = false
        ) shouldBe ALLOW

        // input activation char in the middle
        allow.transit(
            text = "=ab+c",
            inputChar = '+',
            inputIndex = 2,
            textCursorIndex = 3,
            moveTextCursorWithMouse = false,
            moveTextCursorWithKeyboard = false
        ) shouldBe ALLOW

        // move cursor to next to an activation char with mouse
        allow.transit(
            text = "=abc+/q",
            inputChar = null,
            inputIndex = null,
            textCursorIndex = 5,
            moveTextCursorWithMouse = true,
            moveTextCursorWithKeyboard = false
        ) shouldBe ALLOW_MOUSE

        // x: move cursor to next to a non-activation char with mouse
        allow.transit(
            text = "=abc+",
            inputChar = null,
            inputIndex = null,
            textCursorIndex = 2,
            moveTextCursorWithMouse = true,
            moveTextCursorWithKeyboard = false
        ) shouldBe DISALLOW


        // Move cursor to next to an activation char with mouse
        allow.transit(
            text = "=abc+/q",
            inputChar = null,
            inputIndex = null,
            textCursorIndex = 5,
            moveTextCursorWithMouse = false,
            moveTextCursorWithKeyboard = true
        ) shouldBe ALLOW_MOUSE

        // Move cursor to next to a non-activation char with mouse
        allow.transit(
            text = "=abc+",
            inputChar = null,
            inputIndex = null,
            textCursorIndex = 3,
            moveTextCursorWithMouse = false,
            moveTextCursorWithKeyboard = true
        ) shouldBe DISALLOW

        allow.transit(
            text = "=A1-+B1",
            inputChar = '-',
            inputIndex = 3,
            textCursorIndex = 4,
            moveTextCursorWithMouse = false,
            moveTextCursorWithKeyboard = false,
        ) shouldBe ALLOW
    }
}
