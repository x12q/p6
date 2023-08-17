package com.qxdzbc.p6.ui.worksheet.cursor.thumb

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toSize
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.common.compose.layout_coor_wrapper.P6LayoutCoor
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.ui.worksheet.cursor.thumb.state.ThumbStateImp
import com.qxdzbc.p6.ui.worksheet.select_rect.SelectRectStateImp
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
    val celllayoutMap: MutableMap<CellAddress, P6LayoutCoor> = mutableMapOf()
    val c5 = CellAddress("C5")

    @BeforeTest
    fun b() {

        for (c in 1..30) {
            for (r in 1..30) {
                celllayoutMap[CellAddress(c, r)] = mock<P6LayoutCoor>() {
                    val offset = Offset(
                        x = w * (c - 1),
                        y = h * (r - 1),
                    )
                    val intSize = IntSize(width = w.toInt(), height = h.toInt())
                    whenever(it.pixelSizeOrZero) doReturn intSize
                    /*
                    only cell within col[1->20], row[1->20] are attached and have a valid size and offset
                     */
                    whenever(it.isAttached) doReturn (r<=20 && c <=20)
                    if(r<=20 && c <=20){
                        whenever(it.posInWindowOrZero) doReturn offset
                        whenever(it.boundInWindowOrZero) doReturn Rect(
                            offset = offset,
                            size = intSize.toSize()
                        )
                        whenever(it.posInWindow) doReturn offset
                        whenever(it.boundInWindow) doReturn Rect(
                            offset = offset,
                            size = intSize.toSize()
                        )

                    }else{
                        whenever(it.posInWindowOrZero) doReturn Offset.Zero
                        whenever(it.boundInWindowOrZero) doReturn Rect(
                            offset = Offset.Zero,
                            size = Size.Zero
                        )
                        whenever(it.posInWindow) doReturn null
                        whenever(it.boundInWindow) doReturn null
                    }
                }
            }
        }
        s = ThumbStateImp(
            cursorIdSt = ms(mock()),
            mainCellSt = ms(CellAddress("C5")),
            cellLayoutCoorMapSt = ms(celllayoutMap),
            selectRectState = SelectRectStateImp(
                false,false, Offset.Zero,Offset.Zero
            )
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
            isShow = false,
            anchorPoint = Offset(x = 2 * w + 3, y = 4 * h + 3),
            movingPoint = Offset(x = 2 * w + 3, y = 7 * h + 1),
            isActive = false
        )
        val s1 = s.setSelectRectState(rect)
        val relCellMap = s1.getRelevantCells()
        assertEquals(
            (5..8).map { CellAddress("C${it}") },
            relCellMap.keys.toList()
        )

        val (topCell, botCell) = s1.getTopBotCells()!!
        assertEquals(c5, topCell)
        assertEquals(CellAddress("C8"), botCell)

        assertEquals(celllayoutMap[c5]?.posInWindowOrZero, s1.selectedRangeWindowOffsetOrZero)

        val height = relCellMap.map { (c, l) -> l.pixelSizeOrZero.height }.sum()
        val expectedSize = Size(relCellMap[c5]!!.pixelSizeOrZero.width.toFloat(), height.toFloat())
        assertEquals(expectedSize, s1.selectedRangeSizeOrZero)
    }

    @Test
    fun `getRelevantCell move up`() {
        // anchor == inside C5, moving point = C1
        val rect = SelectRectStateImp(
            false,false, Offset.Zero,Offset.Zero
        ).copy(
            anchorPoint = Offset(x = 2 * w + 3, y = 4 * h + 3),
            movingPoint = Offset(x = 2 * w + 3, y = 0 * h + 1)
        )
        val s2 = s.setSelectRectState(rect)
        val relCellMap = s2.getRelevantCells()
        assertEquals(
            (1..5).map { CellAddress("C${it}") },
            relCellMap.keys.toList()
        )

        val (topCell, botCell) = s2.getTopBotCells()!!
        val cellC1 = CellAddress("C1")
        assertEquals(cellC1, topCell)
        assertEquals(c5, botCell)

        assertEquals(celllayoutMap[cellC1]?.posInWindowOrZero, s2.selectedRangeWindowOffsetOrZero)

        val height = relCellMap.map { (c, l) -> l.pixelSizeOrZero.height }.sum()
        val expectedSize = Size(relCellMap[c5]!!.pixelSizeOrZero.width.toFloat(), height.toFloat())
        assertEquals(expectedSize, s2.selectedRangeSizeOrZero)
    }

    @Test
    fun `getRelevantCell move right`() {
        // anchor == inside C5, moving point = E5
        val rect = SelectRectStateImp(
            false,false, Offset.Zero,Offset.Zero
        ).copy(
            anchorPoint = Offset(x = 2 * w + 3, y = 4 * h + 3),
            movingPoint = Offset(x = 4 * w + 3, y = 4 * h + 3)
        )
        val s3 = s.setSelectRectState(rect)
        val relCellMap = s3.getRelevantCells()
        assertEquals(
            (3..5).map { CellAddress(it, 5) },
            relCellMap.keys.toList()
        )
        val (topCell, botCell) = s3.getTopBotCells()!!
        val cellE5 = CellAddress("E5")
        assertEquals(c5, topCell)
        assertEquals(cellE5, botCell)

        assertEquals(celllayoutMap[c5]?.posInWindowOrZero, s3.selectedRangeWindowOffsetOrZero)
        val width = relCellMap.map { (c, l) -> l.pixelSizeOrZero.width }.sum()
        val expectedSize = Size(width.toFloat(), relCellMap[c5]!!.pixelSizeOrZero.height.toFloat())
        assertEquals(expectedSize, s3.selectedRangeSizeOrZero)

    }

    @Test
    fun `getRelevantCell move left`() {
        // anchor == inside C5, moving point = A5
        val rect = SelectRectStateImp(
            false,false, Offset.Zero,Offset.Zero
        ).copy(
            anchorPoint = Offset(x = 2 * w + 3, y = 4 * h + 3),
            movingPoint = Offset(x = 0 * w + 3, y = 4 * h + 3)
        )
        val s3 = s.setSelectRectState(rect)
        val relCellMap = s3.getRelevantCells()
        assertEquals(
            (1..3).map { CellAddress(it, 5) },
            relCellMap.keys.toList()
        )

        val (topCell, botCell) = s3.getTopBotCells()!!
        val cellA5 = CellAddress("A5")
        assertEquals(cellA5, topCell)
        assertEquals(c5, botCell)

        assertEquals(celllayoutMap[cellA5]?.posInWindowOrZero, s3.selectedRangeWindowOffsetOrZero)
        val width = relCellMap.map { (c, l) -> l.pixelSizeOrZero.width }.sum()
        val expectedSize = Size(width.toFloat(), relCellMap[c5]!!.pixelSizeOrZero.height.toFloat())
        assertEquals(expectedSize, s3.selectedRangeSizeOrZero)
    }
}
