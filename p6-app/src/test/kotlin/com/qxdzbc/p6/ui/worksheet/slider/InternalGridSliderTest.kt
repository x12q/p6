package com.qxdzbc.p6.ui.worksheet.slider

import kotlin.test.Test
import kotlin.test.assertEquals


internal class InternalGridSliderTest {

    val slider = InternalGridSlider(
        visibleColRange = IntRange(5, 10),
        visibleRowRange = IntRange(3, 20),
    )


    @Test
    fun constructorTest() {
        assertEquals(5, slider.firstVisibleCol)
        assertEquals(10, slider.lastVisibleCol)
        assertEquals(3, slider.firstVisibleRow)
        assertEquals(20, slider.lastVisibleRow)
    }

    @Test
    fun shiftLeft() {
        val s1 = slider.shiftLeft(2)
        assertEquals(slider.firstVisibleCol - 2, s1.firstVisibleCol)
        assertEquals(slider.lastVisibleCol - 2, s1.lastVisibleCol)
        assertEquals(slider.visibleRowRange, s1.visibleRowRange)
    }

    @Test
    fun shiftRight() {
        val s1 = slider.shiftRight(2)
        assertEquals(slider.firstVisibleCol + 2, s1.firstVisibleCol)
        assertEquals(slider.lastVisibleCol + 2, s1.lastVisibleCol)
        assertEquals(slider.visibleRowRange, s1.visibleRowRange)
    }

    @Test
    fun shiftUp() {
        val s1 = slider.shiftUp(2)
        assertEquals(slider.firstVisibleRow - 2, s1.firstVisibleRow)
        assertEquals(slider.lastVisibleRow - 2, s1.lastVisibleRow)
        assertEquals(slider.visibleColRange, s1.visibleColRange)
    }

    @Test
    fun shiftDown() {
        val s1 = slider.shiftDown(2)
        assertEquals(slider.firstVisibleRow + 2, s1.firstVisibleRow)
        assertEquals(slider.lastVisibleRow + 2, s1.lastVisibleRow)
        assertEquals(slider.visibleColRange, s1.visibleColRange)
    }
}
