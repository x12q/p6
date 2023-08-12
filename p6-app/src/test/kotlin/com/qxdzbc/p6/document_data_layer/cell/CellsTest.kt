package com.qxdzbc.p6.document_data_layer.cell

import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import io.kotest.matchers.shouldBe
import kotlin.test.*
import test.TestSample

class CellsTest {

    @Test
    fun number(){
        val cell = Cells.number("A2",1)
        cell.address shouldBe CellAddress("A2")
        cell.currentValue shouldBe 1.0
    }

    @Test
    fun isLegalReturnType() {
        assertTrue { Cells.isLegalCellType(Ok(1)) }
        assertTrue { Cells.isLegalCellType(Ok("123")) }
        assertTrue { Cells.isLegalCellType(Ok(true)) }
        assertTrue { Cells.isLegalCellType(Err(TestSample.sampleErrorReport)) }

        assertFalse { Cells.isLegalCellType(Err(123)) }
        assertFalse { Cells.isLegalCellType(Ok(Unit)) }
        assertFalse { Cells.isLegalCellType(Err(Unit)) }
        assertFalse { Cells.isLegalCellType(Ok(CellAddress(1, 2))) }
        assertFalse { Cells.isLegalCellType(Err(CellAddress(1, 2))) }
    }

}
