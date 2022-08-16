package com.emeraldblast.p6.app.document.range

import com.emeraldblast.p6.app.common.utils.CellLabelNumberSystem
import com.emeraldblast.p6.app.document.cell.address.CellAddress
import com.emeraldblast.p6.app.document.cell.address.CellAddresses
import com.emeraldblast.p6.app.document.range.address.RangeAddressImp
import com.emeraldblast.p6.app.document.range.address.RangeAddresses
import com.emeraldblast.p6.ui.common.R
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class RangeAddressesTest {

    @Test
    fun fromLabel(){
        val range = RangeAddresses.fromLabel("A1:B32")
        assertEquals(CellAddress("A1"),range.topLeft)
        assertEquals(CellAddress("B32"),range.botRight)

        val r2 = RangeAddresses.fromLabel("F:X")
        assertEquals(CellAddress("F1"), r2.topLeft)
        assertEquals(CellAddress("X${R.worksheetValue.rowLimit}"),r2.botRight)

        val r3 = RangeAddresses.fromLabel("33:44")
        assertEquals(CellAddress("A33"),r3.topLeft)
        assertEquals(CellAddress(CellLabelNumberSystem.numberToLabel(R.worksheetValue.colLimit)+"44"),r3.botRight)
        assertFailsWith(IllegalArgumentException::class){
            RangeAddresses.fromLabel("123abc")
        }

        assertFailsWith(IllegalArgumentException::class){
            RangeAddresses.fromLabel("1:A")
        }
        assertFailsWith(IllegalArgumentException::class){
            RangeAddresses.fromLabel("a:1")
        }
    }

    @Test
    fun fromCells() {
        val c1 = CellAddresses.fromIndices(1, 1)
        val c2 = CellAddresses.fromIndices(3, 2)
        val p1 = RangeAddresses.from2Cells(c1, c2)
        val p2 = RangeAddresses.from2Cells(c2, c1)
        val e = RangeAddressImp(c1, c2)
        assertEquals(e, p1)
        assertEquals(e, p2)
    }

    @Test
    fun singleCell(){
        val c2 = CellAddresses.fromIndices(3, 2)
        val p1 = RangeAddresses.singleCell(c2)
        assertEquals(c2,p1.topLeft)
        assertEquals(c2,p1.botRight)
    }

    @Test
    fun wholeCol(){
        val p = RangeAddresses.wholeCol(333)
        assertEquals(CellAddresses.fromIndices(333,1),p.topLeft)
        assertEquals(CellAddresses.fromIndices(333, R.worksheetValue.rowLimit),p.botRight)
    }

    @Test
    fun wholeRow(){
        val p = RangeAddresses.wholeRow(312)
        assertEquals(CellAddresses.fromIndices(1, 312), p.topLeft)
        assertEquals(CellAddresses.fromIndices(R.worksheetValue.colLimit, 312), p.botRight)
    }

    @Test
    fun fromManyCell(){
        val cells = listOf(CellAddress(1,2), CellAddress(2,3), CellAddress(4,2))
        val p = RangeAddresses.fromManyCells(cells)
        assertEquals(CellAddress(1,2),p.topLeft)
        assertEquals(CellAddress(4,3),p.botRight)
    }

    @Test
    fun wholeMultiRow(){
        val p = RangeAddresses.wholeMultiRow(2,4)
        assertEquals(CellAddress(1,2),p.topLeft)
        assertEquals(CellAddress(R.worksheetValue.colLimit,4),p.botRight)
        assertEquals(RangeAddresses.wholeMultiRow(2,4), RangeAddresses.wholeMultiRow(4,2))
    }

    @Test
    fun wholeMultiCol(){
        val p = RangeAddresses.wholeMultiCol(2,4)
        assertEquals(CellAddress(2,1),p.topLeft)
        assertEquals(CellAddress(4,R.worksheetValue.rowLimit),p.botRight)
        assertEquals(RangeAddresses.wholeMultiCol(2,4), RangeAddresses.wholeMultiCol(4,2))
    }
}
