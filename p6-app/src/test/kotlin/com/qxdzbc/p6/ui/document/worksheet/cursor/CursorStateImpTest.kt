package com.qxdzbc.p6.ui.document.worksheet.cursor

import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorStateImp
import org.mockito.kotlin.mock
import test.TestUtils.compare2ListIgnoreOrder
import kotlin.test.*


internal class CursorStateImpTest {

    lateinit var cursorState: CursorStateImp

    @BeforeTest
    fun beforeTest() {
        cursorState = CursorStateImp.default(mock(),mock())
    }

    @Test
    fun exhaustiveMergeCell(){
        val cells = listOf(
            "C2","C4","C3","C10",
            "A3","A1","A2","A1",
            "K12","L12", "X12",
            "F1","F2","E1","E2",
        ).map{CellAddress(it)}
        val (rr, unUsed) = CursorStateImp.exhaustiveMergeCell(cells)
        assertTrue { compare2ListIgnoreOrder(
            listOf("C2:C4","A1:A3","K12:L12","E1:F2").map{ RangeAddress(it) },
            rr) }
        assertTrue { compare2ListIgnoreOrder(listOf("C10","X12").map{CellAddress(it)}, unUsed) }
    }

    @Test
    fun recursiveMerge() {
        val l1 = listOf(
            "D8:F11",
            "G8:G12",
            "D12:F12",
            "D13:G13",
            "J9:K15",
        ).map { RangeAddress(it) }

        val expect = listOf(
            "D8:G13", "J9:K15",
        ).map { RangeAddress(it) }
        val rs = CursorStateImp.exhaustiveMergeRanges(l1)
        assertEquals(2, rs.size)
        assertTrue { rs.containsAll(expect) }
    }

    @Test
    fun recursiveExpandUpdateFragRanges() {
        val l1 = listOf(
            RangeAddress("A1:B3"),
            RangeAddress("D3:K8"),
            RangeAddress("C3:C9"),
            RangeAddress("C11:C15")
        )
        val (cellWasConsumed,l2) = CursorStateImp.exhaustiveMergeRanges(CellAddress("C10"), l1)

        assertTrue { cellWasConsumed }
        assertEquals(listOf(
            RangeAddress("A1:B3"),
            RangeAddress("D3:K8"),
            RangeAddress("C3:C15")
        ),l2)
    }

    @Test
    fun addFragCell() {
//        val c = cursorState.addFragRanges(
//            listOf(
//                RangeAddress("A1:B3"),
//                RangeAddress("D3:K8"),
//                RangeAddress("C3:C9"),
//                RangeAddress("C12:C15")
//            )
//        ).setMainRange(RangeAddress("Q3:Q9"))
//
//        val c2 = c.addFragCell(CellAddress("C10"))
//        assertTrue { RangeAddress("C3:C10") in c2.fragmentedRanges }
//        assertTrue { RangeAddress("C3:C9") !in c2.fragmentedRanges }
//        assertTrue { CellAddress("C10") !in c2.fragmentedCells }
//
//        val c3 = c2.addFragCell(CellAddress("C11"))
//        assertTrue { RangeAddress("C3:C15") in c3.fragmentedRanges }
//        assertTrue { RangeAddress("C3:C10") !in c3.fragmentedRanges }
//        assertTrue { RangeAddress("C12:C15") !in c3.fragmentedRanges }
//        assertTrue { CellAddress("C11") !in c2.fragmentedCells }
//
//        val c4 = c3.addFragCell(CellAddress("Q2"))
//        assertEquals(RangeAddress("Q2:Q9"), c4.mainRange)
//        assertTrue { CellAddress("Q2") !in c2.fragmentedCells }
//
//        val c5 = c4.addFragCell(CellAddress("KH1"))
//        assertTrue { CellAddress("KH1") in c5.fragmentedCells }
//        assertEquals(c4.fragmentedRanges, c5.fragmentedRanges)
//        assertEquals(c4.mainRange, c5.mainRange)
    }

    @Test
    fun addFragCells(){
//        val c = cursorState.addFragRanges(
//            listOf(
//                RangeAddress("A1:B3"),
//                RangeAddress("D3:K8"),
//                RangeAddress("C3:C9"),
//                RangeAddress("C12:C15")
//            )
//        ).setMainRange(RangeAddress("Q3:Q9"))
//        val c2 = c.addFragCells(listOf(
//            "C10", "KH1","Q2","C11",
//        ).map{ CellAddress(it) })
//        assertTrue { CellAddress("KH1") in c2.fragmentedCells }
//        assertEquals(
//            listOf(
//                RangeAddress("A1:B3"),
//                RangeAddress("D3:K8"),
//                RangeAddress("C3:C15")
//            ).toSet(), c2.fragmentedRanges
//        )
    }

    @Test
    fun allRanges() {
        val r1 = RangeAddress(listOf("A1", "B4").map { CellAddress(it) })
        val r2 = RangeAddress(listOf("K1", "B4").map { CellAddress(it) })
        val c = cursorState.addFragRange(r1).addFragRange(r2)
        val l = c.allRanges
        assertEquals(2, l.size)
        assertTrue { r1 in l }
        assertTrue { r2 in l }
    }

    @Test
    fun allRanges2() {
        val r1 = RangeAddress(listOf("A1", "B4").map { CellAddress(it) })
        val r2 = RangeAddress(listOf("K1", "B4").map { CellAddress(it) })
        val r3 = RangeAddress(listOf("K1", "X4").map { CellAddress(it) })
        val c = cursorState.addFragRange(r1).addFragRange(r2).setMainRange(r3)
        val l = c.allRanges
        assertEquals(3, l.size)
        assertTrue { r1 in l }
        assertTrue { r2 in l }
        assertTrue { r3 in l }
    }

    @Test
    fun allCells() {
        val c1 = cursorState.mainCell.downOneRow()
        val c2 = c1.downOneRow()
        val cr = cursorState.addFragCell(c1).addFragCell(c2)

        assertEquals(3, cr.allFragCells.size)
        assertTrue { c1 in cr.allFragCells }
        assertTrue { c2 in cr.allFragCells }
        assertTrue { cr.mainCell in cr.allFragCells }
    }

    @Test
    fun isPointingTo() {
        val cursorState = CursorStateImp
            .default(mock(), mock())
            .setMainCell(CellAddress(1, 2))
        assertTrue(cursorState.isPointingTo(CellAddress(1, 2)))
        assertFalse(cursorState.isPointingTo(CellAddress(1, 3)))

        val c2 = cursorState.addFragCell(CellAddress(2, 3))
        assertTrue { c2.isPointingTo(CellAddress(2, 3)) }
    }
}
