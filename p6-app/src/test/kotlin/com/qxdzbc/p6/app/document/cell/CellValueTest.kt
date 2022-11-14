package com.qxdzbc.p6.app.document.cell

import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.Range
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.common.error.ErrorReport
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.*

class CellValueTest {
    val mockCell = mock<Cell>().apply {
        whenever(this.valueAfterRun).thenReturn("ABC")
        whenever(this.attemptToAccessDisplayText()) doReturn "ABC"
    }
    val singleCellRange = mock<Range>().apply {
        whenever(isCell) doReturn true
        whenever(cells) doReturn listOf(mockCell)
        whenever(this.address) doReturn RangeAddress(CellAddress("B1"))
    }
    val mockRange2 = mock<Range>().apply {
        whenever(isCell) doReturn false
        whenever(this.address) doReturn RangeAddress("B1:C3")
    }

    @Test
    fun constructorException() {
        assertFails {
            CellValue(
                number = 1.0,
                bool = false
            )
        }
    }

    @Test
    fun `setValue fail`() {
        class B
        val c = CellValue(number = 123.0)
        val c2 = c.setValue(B())
        assertTrue(c2.value is ErrorReport)
    }

    @Test
    fun isEmpty() {
        assertTrue { CellValue().isEmpty() }
        assertFalse { CellValue(number = 123.0).isEmpty() }
        assertFalse { CellValue(str = "123.0").isEmpty() }
        assertFalse { CellValue(bool = true).isEmpty() }
        assertFalse { CellValue(cellSt = mock()).isEmpty() }
        assertFalse { CellValue(range = mock()).isEmpty() }
        assertFalse { CellValue(errorReport = mock()).isEmpty() }
    }

    @Test
    fun editableValue() {
        assertEquals("abc", CellValue(str = "abc").editableValue)
        assertEquals("1", CellValue(number = 1.0).editableValue)
        assertEquals("1.2", CellValue(number = 1.2).editableValue)
        assertEquals("TRUE", CellValue(bool = true).editableValue)
        assertEquals("FALSE", CellValue(bool = false).editableValue)
        assertEquals(null, CellValue(cellSt = ms(mockCell)).editableValue)
        assertEquals(null, CellValue(range = singleCellRange).editableValue)
    }

    @Test
    fun isEditable() {
        val editables = listOf(
            CellValue(str = "abc"),
            CellValue(number = 1.0),
            CellValue(number = 1.2),
            CellValue(bool = true),
            CellValue(bool = false),
        )
        editables.forEach {
//            assertTrue { it.isEditable }
        }
        val notEditables = listOf(
            CellValue(cellSt = ms(mockCell)),
            CellValue(range = singleCellRange)
        )
        notEditables.forEach {
//            assertFalse { it.isEditable }
        }
    }

    @Test
    fun displayStr() {
        assertEquals("abc", CellValue(str = "abc").displayText)
        assertEquals("1", CellValue(number = 1.0).displayText)
        assertEquals("1.2", CellValue(number = 1.2).displayText)
        assertEquals("TRUE", CellValue(bool = true).displayText)
        assertEquals("FALSE", CellValue(bool = false).displayText)
        assertEquals(mockCell.attemptToAccessDisplayText(), CellValue(cellSt = ms(mockCell)).displayText)
        assertEquals("ABC", CellValue(range = singleCellRange).displayText)
        assertEquals("Range[${mockRange2.address.label}]", CellValue(range = mockRange2).displayText)
    }

    @Test
    fun toProto() {
        val c = CellValue(str = "abc")
        val p = c.toProto()
        assertEquals("abc", p.str)
        assertFalse { p.hasBool() }
        assertFalse { p.hasNum() }
    }

}
