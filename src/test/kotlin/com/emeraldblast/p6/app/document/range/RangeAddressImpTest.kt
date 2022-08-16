package com.emeraldblast.p6.app.document.range

import com.emeraldblast.p6.app.document.cell.address.CellAddress
import com.emeraldblast.p6.app.document.cell.address.CellAddresses
import com.emeraldblast.p6.app.document.range.address.RangeAddress
import com.emeraldblast.p6.app.document.range.address.RangeAddressImp
import com.emeraldblast.p6.ui.common.R
import kotlin.test.*

class RangeAddressImpTest {
    @Test
    fun getCellCycle() {
        val rangeAddr = RangeAddress("C5:G13")
        val r = rangeAddr
        val io = mapOf(
            "C5" to ("C5"),
            "H5" to ("C5"),
            "C14" to ("C5"),

            "G14" to ("G5"),
            "B5" to ("G5"),
            "G5" to ("G5"),

            "C4" to ("C13"),
            "H13" to ("C13"),
            "C13" to ("C13"),

            "B13" to ("G13"),
            "G4" to ("G13"),
            "G13" to ("G13"),

            "I6" to ("D6"),
            "D6" to ("D6"),

            "A5" to ("F5"),
            "F5" to ("F5"),

            "A6" to ("F6"),
            "J6" to ("E6"),
            "F6" to ("F6"),
        )
        for ((input, expect) in io) {
            assertEquals(CellAddress(expect), r.getCellAddressCycle(CellAddress(input)), "input = ${input}")
        }
    }

    @Test
    fun getCycleInt() {
        val range = 3..7
        val io = mapOf(
            3 to 3,
            8 to 3,
            9 to 4,
            10 to 5,
            11 to 6,
            16 to 6,
            12 to 7,
            17 to 7,

            2 to 7,
            1 to 6,
            0 to 5,
            -1 to 4,
            -2 to 3,
            -3 to 7,
            -4 to 6,
            -5 to 5,
            -6 to 4,
            -7 to 3,
        )
        for ((i, o) in io) {
            assertEquals(o, RangeAddressImp.getCycleInt(range, i), "input = ${i}")
        }
    }

    @Test
    fun strictMerge() {
        val r1 = RangeAddressImp(
            topLeft = CellAddress("C3"),
            botRight = CellAddress("K5")
        )
        assertEquals(RangeAddress("C3:L5"), r1.strictMerge(RangeAddress("L3:L5")))
        assertEquals(RangeAddress("C3:N5"), r1.strictMerge(RangeAddress("L3:N5")))
        assertEquals(RangeAddress("C3:K10"), r1.strictMerge(RangeAddress("C6:K10")))
        assertEquals(RangeAddress("C1:K5"), r1.strictMerge(RangeAddress("C1:K2")))
        assertEquals(RangeAddress("A3:K5"), r1.strictMerge(RangeAddress("A3:B5")))
        assertEquals(RangeAddress("A3:K5"), r1.strictMerge(RangeAddress("A3:F5")))
        assertEquals(r1, r1.strictMerge(RangeAddress("E3:I5")))
        assertNull(r1.strictMerge(RangeAddress(CellAddress("C1"))))
        assertNull(r1.strictMerge(RangeAddress("A1:K3")))
        assertNull(r1.strictMerge(RangeAddress("A1:K3")))
        assertNull(r1.strictMerge(RangeAddress("H1:O3")))
        assertNull(r1.strictMerge(RangeAddress("I4:O10")))
        assertNull(r1.strictMerge(RangeAddress("C3:D10")))

        val r2 = RangeAddressImp(
            topLeft = CellAddress("C3"),
            botRight = CellAddress("C10")
        )
        assertNull(r2.strictMerge(RangeAddress("C12:C15")))
        assertEquals(RangeAddress("C3:C15"), r2.strictMerge(RangeAddress("C11:C15")))

        val r3 = RangeAddressImp(
            topLeft = CellAddress("B4"),
            botRight = CellAddress("K4"),
        )
        assertNull(r3.strictMerge(RangeAddress("T4:Z4")))
        assertEquals(RangeAddress("B4:Z4"), r3.strictMerge(RangeAddress("L4:Z4")))
    }

    @Test
    fun strictMergeCell() {
        val r1 = RangeAddressImp(
            topLeft = CellAddress("C3"),
            botRight = CellAddress("C8")
        )
        assertEquals(RangeAddress("C3:C9"), r1.strictMerge(CellAddress("C9")))
        assertEquals(RangeAddress("C2:C8"), r1.strictMerge(CellAddress("C2")))
        assertEquals(r1, r1.strictMerge(CellAddress("C5")))
        assertEquals(r1, r1.strictMerge(r1.topLeft))
        assertEquals(r1, r1.strictMerge(r1.botRight))
        assertNull(r1.strictMerge(CellAddress("C10")))
        assertNull(r1.strictMerge(CellAddress("D3")))
        assertNull(r1.strictMerge(CellAddress("A1")))
        assertNull(r1.strictMerge(CellAddress("C1")))

        val r2 = RangeAddressImp(
            topLeft = CellAddress("C3"),
            botRight = CellAddress("K3")
        )

        assertEquals(RangeAddress("B3:K3"), r2.strictMerge(CellAddress("B3")))
        assertEquals(RangeAddress("C3:L3"), r2.strictMerge(CellAddress("L3")))
        assertEquals(r2, r2.strictMerge(CellAddress("D3")))
        assertEquals(r2, r2.strictMerge(r2.topLeft))
        assertEquals(r2, r2.strictMerge(r2.botRight))
        assertNull(r2.strictMerge(CellAddress("C10")))
        assertNull(r2.strictMerge(CellAddress("A1")))
        assertNull(r2.strictMerge(CellAddress("C1")))
        assertNull(r2.strictMerge(CellAddress("C4")))
        assertNull(r2.strictMerge(CellAddress("A3")))
        assertNull(r2.strictMerge(CellAddress("Z3")))

        val r3 = RangeAddressImp(
            topLeft = CellAddress("C3"),
            botRight = CellAddress("K5")
        )

        assertEquals(r3, r3.strictMerge(r3.topLeft))
        assertEquals(r3, r3.strictMerge(r3.botRight))
        assertEquals(r3, r3.strictMerge(CellAddress("D4")))
        assertNull(r3.strictMerge(CellAddress("C2")))
        assertNull(r3.strictMerge(CellAddress("B3")))
        assertNull(r3.strictMerge(CellAddress("K6")))
        assertNull(r3.strictMerge(CellAddress("L5")))
    }

    fun testIterator(r: RangeAddressImp, expect: List<String>) {
        val iterator = r.cellIterator
        val rs = mutableListOf<CellAddress>()
        while (iterator.hasNext()) {
            rs.add(iterator.next())
        }
        assertEquals(expect.map { CellAddress(it) }, rs)
    }

    @Test
    fun intersect() {
        val r1 = RangeAddress(CellAddress("B3"), CellAddress("D6"))
        val r2 = RangeAddress(CellAddress("C1"), CellAddress("E15"))

        val r3 = r1.intersect(r2)
        val r4 = r2.intersect(r1)
        assertEquals(r3, r4)
        assertEquals(RangeAddress(CellAddress("C3"), CellAddress("D6")), r3)
        assertEquals(r1, r1.intersect(r1))
        assertEquals(r2, r2.intersect(r2))
    }

    @Test
    fun cellIterator1() {
        testIterator(
            RangeAddressImp(CellAddress("A1"), CellAddress("B2")),
            listOf("A1", "A2", "B1", "B2")
        )

        testIterator(
            RangeAddressImp(CellAddress("A3"), CellAddress("D3")),
            listOf("A3", "B3", "C3", "D3")
        )

        testIterator(
            RangeAddressImp(CellAddress("A3"), CellAddress("A5")),
            listOf("A3", "A4", "A5")
        )
    }

    @Test
    fun `remove 2nd row`() {
        val r1 = RangeAddressImp(CellAddress("E1"), CellAddress("I9"))
        val rs = r1.removeCell(CellAddress("G2"))
        assertEquals(4, rs.size)
        println(rs.map { it.rawLabel })
    }

    @Test
    fun `remove edge cases`() {
        val r1 = RangeAddressImp(CellAddress("B2"), CellAddress("F12"))
        // x: remove cell not in range
        val rs1 = r1.removeCell(CellAddress("A1"))
        assertEquals(1, rs1.size)
        assertTrue { r1 in rs1 }

        // x: remove invalid cell
        val rs2 = r1.removeCell(CellAddress(-1, -2))
        assertEquals(1, rs2.size)
        assertTrue { r1 in rs2 }

        // x: remove the only cell in range
        val r2 = RangeAddress(CellAddress("B33"))
        val rs3 = r2.removeCell(CellAddress("B33"))
        assertTrue { rs3.isEmpty() }
    }

    @Test
    fun `remove standard cases`() {
        val r1 = RangeAddressImp(CellAddress("B2"), CellAddress("F12"))

        // x: remove cell enclosed in range
        val rs1 = r1.removeCell(CellAddress("D6"))
        println(rs1.map { it.rawLabel })
        assertEquals(4, rs1.size)
        assertTrue { RangeAddress(CellAddress("B2"), CellAddress("F5")) in rs1 }
        assertTrue { RangeAddress(CellAddress("B6"), CellAddress("C6")) in rs1 }
        assertTrue { RangeAddress(CellAddress("E6"), CellAddress("F6")) in rs1 }
        assertTrue { RangeAddress(CellAddress("B7"), CellAddress("F12")) in rs1 }

        // x: remove cells on edge and vertices

        val rs2 = r1.removeCell(CellAddress("B2"))
        assertEquals(2, rs2.size)
        assertTrue { RangeAddress(CellAddress("C2"), CellAddress("F2")) in rs2 }
        assertTrue { RangeAddress(CellAddress("B3"), CellAddress("F12")) in rs2 }

        val rs3 = r1.removeCell(CellAddress("B4"))
        assertEquals(3, rs3.size)
        assertTrue { RangeAddress(CellAddress("B2"), CellAddress("F3")) in rs3 }
        assertTrue { RangeAddress(CellAddress("C4"), CellAddress("F4")) in rs3 }
        assertTrue { RangeAddress(CellAddress("B5"), CellAddress("F12")) in rs3 }

        val rs4 = r1.removeCell(CellAddress("B12"))
        assertEquals(2, rs4.size)
        assertTrue { RangeAddress(listOf("B2", "F11").map { CellAddress(it) }) in rs4 }
        assertTrue { RangeAddress(listOf("C12", "F12").map { CellAddress(it) }) in rs4 }

        val rs5 = r1.removeCell(CellAddress("D12"))
        assertEquals(3, rs5.size)
        assertTrue { RangeAddress(listOf("B2", "F11").map { CellAddress(it) }) in rs5 }
        assertTrue { RangeAddress(listOf("B12", "C12").map { CellAddress(it) }) in rs5 }
        assertTrue { RangeAddress(listOf("E12", "F12").map { CellAddress(it) }) in rs5 }

        val rs6 = r1.removeCell(CellAddress("F12"))
        assertEquals(2, rs6.size)
        assertTrue { RangeAddress(listOf("B2", "F11").map { CellAddress(it) }) in rs6 }
        assertTrue { RangeAddress(listOf("B12", "E12").map { CellAddress(it) }) in rs6 }


        val rs7 = r1.removeCell(CellAddress("F8"))
        assertEquals(3, rs7.size)
        assertTrue { RangeAddress(listOf("B2", "F7").map { CellAddress(it) }) in rs7 }
        assertTrue { RangeAddress(listOf("B8", "E8").map { CellAddress(it) }) in rs7 }
        assertTrue { RangeAddress(listOf("B9", "F12").map { CellAddress(it) }) in rs7 }


        val rs8 = r1.removeCell(CellAddress("F2"))
        assertEquals(2, rs8.size)
        assertTrue { RangeAddress(listOf("B2", "E2").map { CellAddress(it) }) in rs8 }
        assertTrue { RangeAddress(listOf("B3", "F12").map { CellAddress(it) }) in rs8 }

        val rs9 = r1.removeCell(CellAddress("D2"))
        assertEquals(3, rs9.size)
        assertTrue { RangeAddress(listOf("B2", "C2").map { CellAddress(it) }) in rs9 }
        assertTrue { RangeAddress(listOf("E2", "F2").map { CellAddress(it) }) in rs9 }
        assertTrue { RangeAddress(listOf("B3", "F12").map { CellAddress(it) }) in rs9 }
    }

    @Test
    fun faultyConstructorCall() {
        assertFailsWith(IllegalArgumentException::class) {
            RangeAddressImp(
                topLeft = CellAddresses.fromIndices(2, 3),
                botRight = CellAddresses.fromIndices(1, 1),
            )
        }
        assertFailsWith(IllegalArgumentException::class) {
            RangeAddressImp(
                topLeft = CellAddresses.fromIndices(1, 3),
                botRight = CellAddresses.fromIndices(2, 1)
            )
        }
        assertFailsWith(IllegalArgumentException::class) {
            RangeAddressImp(
                topLeft = CellAddresses.fromIndices(2, 1),
                botRight = CellAddresses.fromIndices(1, 3)
            )
        }
    }

    @Test
    fun testLabel_1() {
        val p = RangeAddressImp(
            topLeft = CellAddresses.fromIndices(1, 1),
            botRight = CellAddresses.fromIndices(2, 3)
        )
        assertEquals("A1:B3", p.rawLabel)
        assertEquals("@A1:B3", p.label)
    }

    @Test
    fun testLabel_wholeCol() {
        val p = RangeAddressImp(
            topLeft = CellAddresses.fromIndices(1, 1),
            botRight = CellAddresses.fromIndices(2, R.worksheetValue.rowLimit)
        )
        assertEquals("A:B", p.rawLabel)
        assertEquals("@A:B", p.label)
    }

    @Test
    fun testLabel_wholeRow() {
        val p = RangeAddressImp(
            topLeft = CellAddresses.fromIndices(1, 1),
            botRight = CellAddresses.fromIndices(R.worksheetValue.colLimit, 5)
        )
        assertEquals("1:5", p.rawLabel)
        assertEquals("@1:5", p.label)
    }

    @Test
    fun contains() {
        val p = RangeAddressImp(
            topLeft = CellAddresses.fromIndices(1, 1),
            botRight = CellAddresses.fromIndices(6, 5)
        )
        assertTrue(p.contains(p.topLeft))
        assertTrue(p.contains(p.botRight))
        assertTrue(p.contains(CellAddresses.fromIndices(3, 4)))
        assertFalse(p.contains(CellAddresses.fromIndices(100, 2)))
    }

    @Test
    fun singleCellRange() {
        val p = RangeAddressImp(
            topLeft = CellAddresses.fromIndices(1, 1),
            botRight = CellAddresses.fromIndices(1, 1)
        )
        assertTrue(p.contains(p.topLeft))
        assertTrue(p.contains(p.botRight))
    }

}
