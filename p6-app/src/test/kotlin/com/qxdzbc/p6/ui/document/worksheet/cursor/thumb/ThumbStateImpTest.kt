package com.qxdzbc.p6.ui.document.worksheet.cursor.thumb

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.document.worksheet.select_rect.SelectRectStateImp
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class ThumbStateImpTest {
    lateinit var s: ThumbStateImp
    val w = 100f
    val h = 30f

    @BeforeTest
    fun b() {
        val celllayoutMap: MutableMap<CellAddress, LayoutCoorWrapper> = mutableMapOf()
        for (c in 1..20) {
            for (r in 1..20) {
                celllayoutMap[CellAddress(c, r)] = mock<LayoutCoorWrapper>() {
                    whenever(it.size) doReturn (DpSize(width = w.dp, height = h.dp))
                    whenever(it.posInWindow) doReturn (Offset(
                        x = w * (c - 1),
                        y = h * (r - 1),
                    ))
                }
            }
        }
        val cursorState = mock<CursorState>() {
            whenever(it.mainCell) doReturn CellAddress("C5")
        }
        s = ThumbStateImp(
            cursorStateSt = ms(cursorState),
            cellLayoutCoorMapSt = ms(celllayoutMap),
        )
    }

    @Test
    fun getCellAtCross() {
        s.getCellAtTheCross().forEach { (c, l) ->
            assertTrue(s.mainCell.colIndex == c.colIndex || s.mainCell.rowIndex == c.rowIndex)
        }
    }

    @Test
    fun `getRelevantCell move down`() {
        // anchor == inside C5, moving point = C8
        val rect = SelectRectStateImp(
            anchorPoint = Offset(x = 2 * w + 3, y = 4 * h + 3),
            movingPoint = Offset(x = 2 * w + 3, y = 7 * h + 1)
        )
        val s1 = s.setSelectRectState(rect)
        val c1 = s1.getRelevantCells()
        assertEquals(
            (5..8).map { CellAddress("C${it}") },
            c1.keys.toList()
        )
    }

    @Test
    fun `getRelevantCell move up`() {
        // anchor == inside C5, moving point = C1
        val rect = SelectRectStateImp(
            anchorPoint = Offset(x = 2 * w + 3, y = 4 * h + 3),
            movingPoint = Offset(x = 2 * w + 3, y = 0 * h + 1)
        )
        val s2 = s.setSelectRectState(rect)
        val c2 = s2.getRelevantCells()
        assertEquals(
            (1..5).map { CellAddress("C${it}") },
            c2.keys.toList()
        )
    }

    @Test
    fun `getRelevantCell move right`() {
        // anchor == inside C5, moving point = E5
        val rect = SelectRectStateImp(
            anchorPoint = Offset(x = 2 * w + 3, y = 4 * h + 3),
            movingPoint = Offset(x = 4 * w + 3, y = 4 * h + 3)
        )
        val s3 = s.setSelectRectState(rect)
        val c3 = s3.getRelevantCells()
        assertEquals(
            (3..5).map { CellAddress(it, 5) },
            c3.keys.toList()
        )
    }

    @Test
    fun `getRelevantCell move left`() {
        // anchor == inside C5, moving point = A5
        val rect = SelectRectStateImp(
            anchorPoint = Offset(x = 2 * w + 3, y = 4 * h + 3),
            movingPoint = Offset(x = 0 * w + 3, y = 4 * h + 3)
        )
        val s3 = s.setSelectRectState(rect)
        val c3 = s3.getRelevantCells()
        assertEquals(
            (1..3).map { CellAddress(it, 5) },
            c3.keys.toList()
        )
    }
}
