package com.qxdzbc.p6.ui.document.worksheet.slider

import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFailsWith

internal class LimitedSliderTest {

    val s0 = LimitedSlider(
        slider = GridSliderImp(
            visibleColRange = IntRange(0, 9),
            visibleRowRange = IntRange(3, 20),
        ),
        colLimit = IntRange(0, 100),
        rowLimit = IntRange(0, 120)
    )

    @Test
    fun constructor() {
        assertFailsWith<IllegalArgumentException> {
            LimitedSlider(
                slider = GridSliderImp(
                    visibleColRange = IntRange(0, 9),
                    visibleRowRange = IntRange(3, 20),
                ),
                colLimit = IntRange(0, 5),
                rowLimit = IntRange(0, 120)
            )
        }

        assertFailsWith<IllegalArgumentException> {
            LimitedSlider(
                slider = GridSliderImp(
                    visibleColRange = IntRange(0, 9),
                    visibleRowRange = IntRange(3, 20),
                ),
                colLimit = IntRange(7, 5000),
                rowLimit = IntRange(0, 120)
            )
        }

        assertFailsWith<IllegalArgumentException> {
            LimitedSlider(
                slider = GridSliderImp(
                    visibleColRange = IntRange(0, 9),
                    visibleRowRange = IntRange(3, 20),
                ),
                colLimit = IntRange(0, 100),
                rowLimit = IntRange(9, 120)
            )
        }
        assertFailsWith<IllegalArgumentException> {
            LimitedSlider(
                slider = GridSliderImp(
                    visibleColRange = IntRange(0, 9),
                    visibleRowRange = IntRange(3, 20),
                ),
                colLimit = IntRange(0, 100),
                rowLimit = IntRange(0, 11)
            )
        }
    }

    @Test
    fun shiftLeft() {
        val s1 = s0.shiftLeft(1000)
        assertEquals(IntRange(0, 9), s1.visibleColRange)
        assertEquals(0, s1.firstVisibleCol)
        assertEquals(9, s1.lastVisibleCol)
        assertEquals(s0.visibleRowRange, s1.visibleRowRange)
    }

    @Test
    fun shiftRight() {
        val s1 = s0.shiftRight(1000)
        assertEquals(IntRange(91, 100), s1.visibleColRange)
        assertEquals(91, s1.firstVisibleCol)
        assertEquals(100, s1.lastVisibleCol)
        assertEquals(s0.visibleRowRange, s1.visibleRowRange)
    }

    @Test
    fun shiftUp() {
        val s1 = s0.shiftUp(1000)
        assertEquals(s0.visibleColRange, s1.visibleColRange)
        assertEquals(IntRange(0, 17), s1.visibleRowRange)
        assertEquals(0, s1.firstVisibleRow)
        assertEquals(17, s1.lastVisibleRow)
    }

    @Test
    fun shiftDown() {
        val s1 = s0.shiftDown(1000)
        assertEquals(s0.visibleColRange, s1.visibleColRange)
        assertEquals(IntRange(103, 120), s1.visibleRowRange)
        assertEquals(103, s1.firstVisibleRow)
        assertEquals(120, s1.lastVisibleRow)

    }

    @Test
    fun shiftDown_negative() {
        val s1 = s0.shiftDown(-4)
        assertEquals(s0.visibleColRange, s1.visibleColRange)
        assertEquals(IntRange(0, 17), s1.visibleRowRange)
        assertEquals(0, s1.firstVisibleRow)
        assertEquals(17, s1.lastVisibleRow)
    }
}
