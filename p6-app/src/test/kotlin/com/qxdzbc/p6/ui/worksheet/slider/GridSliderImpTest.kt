package com.qxdzbc.p6.ui.worksheet.slider

import com.qxdzbc.common.compose.StateUtils
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.ui.worksheet.WorksheetConstants
import com.qxdzbc.p6.ui.worksheet.cursor.state.CursorStateImp
import com.qxdzbc.p6.ui.worksheet.state.WorksheetId
import io.kotest.matchers.shouldBe
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

internal class GridSliderImpTest {

    val s0 = GridSliderImp(
        slider = InternalGridSlider(
            visibleColRange = IntRange(0, 9),
            visibleRowRange = IntRange(3, 20),
        ),
        colLimit = IntRange(0, 100),
        rowLimit = IntRange(0, 120)
    )

    @Test
    fun `updateEdgeSliderLimit - shrink case`(){
        val s0 = GridSliderImp(
            slider = InternalGridSlider(
                visibleColRange = IntRange(1,100),
                visibleRowRange = IntRange(3,120),
            ),
            colLimit = IntRange(0, 100),
            rowLimit = IntRange(0, 120),
        ).copy(
            scrollBarLastCol = 1000,
            scrollBarLastRow = 800,
        )

        val margin = 20
        val s1 = s0.updateScrollBarLimit(margin)
        s1.scrollBarLastCol shouldBe 100+20
        s1.scrollBarLastRow shouldBe 120+20
    }

    @Test
    fun `updateEdgeSliderLimit - expanse case`(){
        val s0 = GridSliderImp(
            slider = InternalGridSlider(
                visibleColRange = IntRange(1,100),
                visibleRowRange = IntRange(3,120),
            ),
            colLimit = IntRange(0, 100),
            rowLimit = IntRange(0, 120),
        ).copy(
            scrollBarLastCol = 100,
            scrollBarLastRow = 100,
        )

        val margin = 20
        val s1 = s0.updateScrollBarLimit(margin)
        s1.scrollBarLastCol shouldBe 100+20
        s1.scrollBarLastRow shouldBe 120+20
    }

    @Test
    fun `updateEdgeSliderLimit non update`(){
        val s0 = GridSliderImp(
            slider = InternalGridSlider(
                visibleColRange = IntRange(1,100),
                visibleRowRange = IntRange(3,120),
            ),
            colLimit = IntRange(0, 100),
            rowLimit = IntRange(0, 120),
        ).copy(
            scrollBarLastCol = 170,
            scrollBarLastRow = 180,
        )

        val margin = 20
        val s1 = s0.updateScrollBarLimit(margin)
        s1 shouldBe s0

    }


    @Test
    fun constructor() {
        assertFailsWith<IllegalArgumentException> {
            GridSliderImp(
                slider = InternalGridSlider(
                    visibleColRange = IntRange(0, 9),
                    visibleRowRange = IntRange(3, 20),
                ),
                colLimit = IntRange(0, 5),
                rowLimit = IntRange(0, 120)
            )
        }

        assertFailsWith<IllegalArgumentException> {
            GridSliderImp(
                slider = InternalGridSlider(
                    visibleColRange = IntRange(0, 9),
                    visibleRowRange = IntRange(3, 20),
                ),
                colLimit = IntRange(7, 5000),
                rowLimit = IntRange(0, 120)
            )
        }

        assertFailsWith<IllegalArgumentException> {
            GridSliderImp(
                slider = InternalGridSlider(
                    visibleColRange = IntRange(0, 9),
                    visibleRowRange = IntRange(3, 20),
                ),
                colLimit = IntRange(0, 100),
                rowLimit = IntRange(9, 120)
            )
        }
        assertFailsWith<IllegalArgumentException> {
            GridSliderImp(
                slider = InternalGridSlider(
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
        assertEquals(IntRange(0, 9), s1.visibleColRangeIncludeMargin)
        assertEquals(0, s1.firstVisibleCol)
        assertEquals(9, s1.lastVisibleCol)
        assertEquals(s0.visibleRowRangeIncludeMargin, s1.visibleRowRangeIncludeMargin)
    }

    @Test
    fun shiftRight() {
        val s1 = s0.shiftRight(1000)
        assertEquals(IntRange(91, 100), s1.visibleColRangeIncludeMargin)
        assertEquals(91, s1.firstVisibleCol)
        assertEquals(100, s1.lastVisibleCol)
        assertEquals(s0.visibleRowRangeIncludeMargin, s1.visibleRowRangeIncludeMargin)
    }

    @Test
    fun shiftUp() {
        val s1 = s0.shiftUp(1000)
        assertEquals(s0.visibleColRangeIncludeMargin, s1.visibleColRangeIncludeMargin)
        assertEquals(IntRange(0, 17), s1.visibleRowRangeIncludeMargin)
        assertEquals(0, s1.firstVisibleRow)
        assertEquals(17, s1.lastVisibleRow)
    }

    @Test
    fun shiftDown() {
        val s1 = s0.shiftDown(1000)
        assertEquals(s0.visibleColRangeIncludeMargin, s1.visibleColRangeIncludeMargin)
        assertEquals(IntRange(103, 120), s1.visibleRowRangeIncludeMargin)
        assertEquals(103, s1.firstVisibleRow)
        assertEquals(120, s1.lastVisibleRow)

    }

    @Test
    fun shiftDown_negative() {
        val s1 = s0.shiftDown(-4)
        assertEquals(s0.visibleColRangeIncludeMargin, s1.visibleColRangeIncludeMargin)
        assertEquals(IntRange(0, 17), s1.visibleRowRangeIncludeMargin)
        assertEquals(0, s1.firstVisibleRow)
        assertEquals(17, s1.lastVisibleRow)
    }


    val wsId = StateUtils.ms(mock<WorksheetId>() {
        whenever(it.wsName) doReturn ""
    })
    @Test
    fun `followCursor no margin`() {
        val gridSlider = GridSliderImp(
            slider = InternalGridSlider(
                visibleColRange = IntRange(5, 10),
                visibleRowRange = IntRange(3, 20),
            ),
            colLimit = WorksheetConstants.defaultColRange,
            rowLimit = WorksheetConstants.defaultRowRange,
        )
        // cursor on left-most col
        val cursor = CursorStateImp.default2(wsId, mock(), mock()).setMainCell(
            CellAddress(gridSlider.firstVisibleCol - 1, 2)
        )
        val m1 = gridSlider.followCursorMainCell(cursor)
        assertNotNull(m1)
        assertEquals(gridSlider.firstVisibleCol - 1, m1.firstVisibleCol)
        assertEquals(gridSlider.lastVisibleCol - 1, m1.lastVisibleCol)
        assertEquals(gridSlider.visibleRowRangeIncludeMargin, m1.visibleRowRangeIncludeMargin)

        // cursor on right-most col
        val cursor2 = CursorStateImp.default2(wsId, mock(), mock()).setMainCell(
            CellAddress(gridSlider.lastVisibleCol + 1, 2)
        )
        val m2 = gridSlider.followCursorMainCell(cursor2)
        assertNotNull(m2)
        assertEquals(gridSlider.firstVisibleCol + 1, m2.firstVisibleCol)
        assertEquals(gridSlider.lastVisibleCol + 1, m2.lastVisibleCol)
        assertEquals(gridSlider.visibleRowRangeIncludeMargin, m2.visibleRowRangeIncludeMargin)

        // cursor on bot row
        val cursor3 = CursorStateImp.default2(wsId, mock(), mock()).setMainCell(
            CellAddress(5, gridSlider.lastVisibleRow + 1)
        )
        val m3 = gridSlider.followCursorMainCell(cursor3)
        assertNotNull(m3)
        assertEquals(gridSlider.lastVisibleRow + 1, m3.lastVisibleRow)
        assertEquals(gridSlider.firstVisibleRow + 1, m3.firstVisibleRow)
        assertEquals(gridSlider.visibleColRangeIncludeMargin, m3.visibleColRangeIncludeMargin)

        // cursor on top row
        val cursor4 = CursorStateImp.default2(wsId, mock(), mock()).setMainCell(
            CellAddress(5, gridSlider.firstVisibleRow - 1)
        )
        val m4 = gridSlider.followCursorMainCell(cursor4)
        assertNotNull(m4)
        assertEquals(gridSlider.lastVisibleRow - 1, m4.lastVisibleRow)
        assertEquals(gridSlider.firstVisibleRow - 1, m4.firstVisibleRow)
        assertEquals(gridSlider.visibleColRangeIncludeMargin, m4.visibleColRangeIncludeMargin)
    }


    @Test
    fun `followCursorMainCell when main cell is one of the margin cells`() {
        val gridSlider =GridSliderImp(
            slider = InternalGridSlider(
                visibleColRange = IntRange(5, 10),
                visibleRowRange = IntRange(3, 20),
            ),
            colLimit = WorksheetConstants.defaultColRange,
            rowLimit = WorksheetConstants.defaultRowRange,
        ).copy(
            marginRow = 20,
            marginCol = 10
        )

//         x: cursor on left-most col
        val cursor = CursorStateImp.default2(wsId, mock(),mock())
            .setMainCell(CellAddress(gridSlider.firstVisibleCol - 1, 2))

        val m1 = gridSlider.followCursorMainCell(cursor)

        assertEquals(gridSlider.firstVisibleCol - 1, m1.firstVisibleCol)
        assertEquals(gridSlider.lastVisibleCol - 1, m1.lastVisibleCol)
        assertEquals(gridSlider.visibleRowRangeIncludeMargin, m1.visibleRowRangeIncludeMargin)

        // x: cursor on the margin col on the right => gridSlider should move its visible range 1 col to
        val cursor2 = CursorStateImp.default2(wsId, mock(),mock()).setMainCell(
            CellAddress(gridSlider.lastVisibleCol, 2)
        )
        val m2 = gridSlider.followCursorMainCell(cursor2)

        m2.firstVisibleCol shouldBe gridSlider.firstVisibleCol + 1
        m2.visibleRowRangeIncludeMargin shouldBe gridSlider.visibleRowRangeIncludeMargin

        // x: cursor on margin row
        val cursor3 = CursorStateImp.default2(wsId, mock(),mock()).setMainCell(
            CellAddress(5, gridSlider.lastVisibleRow)
        )
        val m3 = gridSlider.followCursorMainCell(cursor3)
        assertNotNull(m3)
        assertEquals(gridSlider.lastVisibleRow + 1, m3.lastVisibleRow)
        assertEquals(gridSlider.firstVisibleRow + 1, m3.firstVisibleRow)
        assertEquals(gridSlider.visibleColRangeIncludeMargin, m3.visibleColRangeIncludeMargin)

        // x: cursor on top row
        val cursor4 = CursorStateImp.default2(wsId, mock(),mock()).setMainCell(
            CellAddress(5, gridSlider.firstVisibleRow - 1)
        )
        val m4 = gridSlider.followCursorMainCell(cursor4)
        assertNotNull(m4)
        assertEquals(gridSlider.lastVisibleRow - 1, m4.lastVisibleRow)
        assertEquals(gridSlider.firstVisibleRow - 1, m4.firstVisibleRow)
        assertEquals(gridSlider.visibleColRangeIncludeMargin, m4.visibleColRangeIncludeMargin)
    }

}
