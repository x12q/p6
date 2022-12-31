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
            ts.stateContMs,
            mock(),
            mock()
        )
    }

    @Test
    fun clearColor() {
        ts.stateCont.getCellBackgroundColorSelectorStateMs(ts.window1Id)!!.value = e
        ts.stateCont.getCellBackgroundColorSelectorState(ts.window1Id) shouldBe e

        action.clearColor(ts.window1Id)
        ts.stateCont.getCellBackgroundColorSelectorState(ts.window1Id) shouldBe ColorSelectorStateImp(null)
    }

    @Test
    fun pickColor() {
        ts.stateCont.getCellBackgroundColorSelectorState(ts.window1Id) shouldNotBe e
        action.pickColor(ts.window1Id,color)
        ts.stateCont.getCellBackgroundColorSelectorState(ts.window1Id) shouldBe e
        verify(action.updateCellFormatAction,times(1)).setBackgroundColorOnSelectedCells(color,undoable=true)

    }
}
