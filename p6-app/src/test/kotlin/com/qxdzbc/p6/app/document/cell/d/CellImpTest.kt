package com.qxdzbc.p6.app.document.cell.d

import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.d.CellValue.Companion.toCellValue
import com.qxdzbc.common.compose.StateUtils.toMs
import kotlin.test.*
class CellImpTest {

    @Test
    fun toProto() {
        val cell = CellImp(
            address = CellAddress("A1"),
            content = CellContentImp(
                cellValueMs = 123.toCellValue().toMs(),
            )
        )
        val proto = cell.toProto()
        assertEquals(123.toCellValue().toProto(), proto.value)
        assertEquals(cell.address.toProto(), proto.address)

        val cell2 = CellImp(
            address = CellAddress("A1"),
            content = CellContentImp(
                cellValueMs = 123.0.toCellValue().toMs(),
            )
        )
        val proto2 = cell2.toProto()
    }

    @Test
    fun `toProto missing value and formula`() {
        val cell = CellImp(
            address = CellAddress("A1"),
            content = CellContentImp(
                cellValueMs = CellValue.empty.toMs(),
                formula = null
            )
        )
        val proto = cell.toProto()
        assertFalse { proto.hasFormula() }
    }
}
