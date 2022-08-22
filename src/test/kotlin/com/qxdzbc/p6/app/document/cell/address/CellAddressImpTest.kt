package com.qxdzbc.p6.app.document.cell.address

import kotlin.test.*


class CellAddressImpTest {

    lateinit var c1:CellAddressImp
    @BeforeTest
    fun before(){
        c1 = CellAddressImp(5, 9)
    }
    @Test
    fun `create cell address from label`(){

    }

    @Test
    fun shiftRelative(){
        val c= CellAddressImp(3,10)
        val c2 = c.shift(CellAddress("A1"), CellAddress("A3"))
        assertEquals(CellAddress(3,12),c2)

        val c3 = c.shift(CellAddress("A1"), CellAddress("C3"))
        assertEquals(CellAddress(5,12),c3)

        val c4 = c.shift(CellAddress("C3"),CellAddress("A1"))
        assertEquals(CellAddress(1,8),c4)
    }

    @Test
    fun isValid(){
        assertTrue { CellAddressImp(1,1).isValid() }
        assertTrue { CellAddressImp(2,3).isValid() }
        assertFalse { CellAddressImp(2,0).isValid() }
        assertFalse { CellAddressImp(0,123).isValid() }
        assertFalse { CellAddressImp(0,0).isValid() }
        assertFalse { CellAddressImp(3,-1).isValid() }
        assertFalse { CellAddressImp(0,-1).isValid() }
        assertFalse { CellAddressImp(-1,0).isValid() }
        assertFalse { CellAddressImp(-1,3).isValid() }
    }

    @Test
    fun increaseRowBy() {
        val c2 = c1.increaseRowBy(100)
        assertEquals(100+c1.rowIndex,c2.rowIndex)

        val c3 = c1.increaseRowBy(-100)
        assertEquals(1,c3.rowIndex)

        val c4 = c1.increaseRowBy(-3)
        assertEquals(c1.rowIndex-3,c4.rowIndex)

        val c5 = c1.increaseRowBy(0)
        assertEquals(c1.rowIndex,c5.rowIndex)

        val c6 = CellAddress(123,Int.MAX_VALUE)
        val c7 = c6.increaseRowBy(123)
        assertEquals(c6,c7)

        val c8 = CellAddress(123,Int.MIN_VALUE)
        val c9 = c8.increaseRowBy(-23)
        assertEquals(c8,c9)
    }

    @Test
    fun increaseColBy() {
        val c2 = c1.increaseColBy(100)
        assertEquals(100 + c1.colIndex, c2.colIndex)

        val c3 = c1.increaseColBy(-100)
        assertEquals(1, c3.colIndex)

        val c4 = c1.increaseColBy(-3)
        assertEquals(c1.colIndex - 3, c4.colIndex)

        val c5 = c1.increaseColBy(0)
        assertEquals(c1.colIndex, c5.colIndex)

        val c6 = CellAddress(Int.MAX_VALUE,123)
        assertEquals(c6, c6.increaseColBy(123))

        val c7 = CellAddress(Int.MIN_VALUE, 123)
        assertEquals(c7, c7.increaseColBy(-123))

    }

    @Test
    fun downOneRow(){
        val c2 = c1.downOneRow()
        assertEquals(c1.rowIndex+1,c2.rowIndex)
    }

    @Test
    fun upOneRow(){
        val c2 = c1.upOneRow()
        assertEquals(c1.rowIndex-1,c2.rowIndex)
    }

    @Test
    fun leftOneCol(){
        val c2 = c1.leftOneCol()
        assertEquals(c1.colIndex-1,c2.colIndex)
    }

    @Test
    fun rightOneCol(){
        val c2 = c1.rightOneCol()
        assertEquals(c1.colIndex+1,c2.colIndex)
    }
}
