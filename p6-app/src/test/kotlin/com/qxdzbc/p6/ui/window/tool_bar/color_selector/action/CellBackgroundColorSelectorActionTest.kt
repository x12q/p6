package com.qxdzbc.p6.ui.window.tool_bar.color_selector.action

import androidx.compose.ui.graphics.Color
import com.qxdzbc.p6.ui.window.tool_bar.color_selector.state.ColorSelectorStateImp
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import test.BaseAppStateTest
import kotlin.test.*

internal class CellBackgroundColorSelectorActionTest :BaseAppStateTest(){

    lateinit var action:CellBackgroundColorSelectorAction
    val color = Color(123)
    val e = ColorSelectorStateImp(color)
    @BeforeTest
    fun b(){
        action = CellBackgroundColorSelectorAction(
            ts.sc,
            mock(),
            mock()
        )
    }

    @Test
    fun clearColor() {
        ts.sc.getCellBackgroundColorSelectorStateMs(ts.window1Id)!!.value = e
        ts.sc.getCellBackgroundColorSelectorState(ts.window1Id) shouldBe e

        action.clearColor(ts.window1Id)
        ts.sc.getCellBackgroundColorSelectorState(ts.window1Id) shouldBe ColorSelectorStateImp(null)
    }

    @Test
    fun pickColor() {
        ts.sc.getCellBackgroundColorSelectorState(ts.window1Id) shouldNotBe e
        action.pickColor(ts.window1Id,color)
        ts.sc.getCellBackgroundColorSelectorState(ts.window1Id) shouldBe e
        verify(action.updateCellFormatAction,times(1)).setBackgroundColorOnSelectedCells(color,undoable=true)

    }
}
