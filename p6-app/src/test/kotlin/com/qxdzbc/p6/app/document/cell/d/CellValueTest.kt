package com.qxdzbc.p6.app.document.cell.d

import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.Range
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.common.exception.error.ErrorReport
import com.github.michaelbull.result.Err
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.*

class CellValueTest {
    val mockCell = mock<Cell>().apply {
        whenever(this.valueAfterRun).thenReturn("ABC")
        whenever(this.displayValue) doReturn "ABC"
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
    fun value() {
        assertEquals(1.0, CellValue(number = 1.0).valueAfterRun)
        assertEquals(true, CellValue(bool = true).valueAfterRun)
        assertEquals("qwe", CellValue(str = "qwe").valueAfterRun)
        val mockErr = mock<ErrorReport>()
        assertEquals(mockErr, CellValue(errorReport = mockErr).valueAfterRun)
        assertEquals(null, CellValue().valueAfterRun)
        assertEquals("ABC", CellValue(cell = mockCell).valueAfterRun)
        assertEquals("ABC", CellValue(range = singleCellRange).valueAfterRun)
        assertEquals(mockRange2, CellValue(range = mockRange2).valueAfterRun)
    }

    @Test
    fun `setValue fail`() {
        class B
        val c = CellValue(number = 123.0)
        val c2 = c.setValue(B())
        assertTrue(c2.currentValue is ErrorReport)
    }

    @Test
    fun isEmpty() {
        assertTrue { CellValue().isEmpty() }
        assertFalse { CellValue(number = 123.0).isEmpty() }
        assertFalse { CellValue(str = "123.0").isEmpty() }
        assertFalse { CellValue(bool = true).isEmpty() }
        assertFalse { CellValue(cell = mock()).isEmpty() }
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
        assertEquals(null, CellValue(cell = mockCell).editableValue)
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
            CellValue(cell = mockCell),
            CellValue(range = singleCellRange)
        )
        notEditables.forEach {
//            assertFalse { it.isEditable }
        }
    }

    @Test
    fun displayStr() {
        assertEquals("abc", CellValue(str = "abc").displayStr)
        assertEquals("1", CellValue(number = 1.0).displayStr)
        assertEquals("1.2", CellValue(number = 1.2).displayStr)
        assertEquals("TRUE", CellValue(bool = true).displayStr)
        assertEquals("FALSE", CellValue(bool = false).displayStr)
        assertEquals(mockCell.displayValue, CellValue(cell = mockCell).displayStr)
        assertEquals("ABC", CellValue(range = singleCellRange).displayStr)
        assertEquals("Range[${mockRange2.address.label}]", CellValue(range = mockRange2).displayStr)
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
