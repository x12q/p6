package com.qxdzbc.p6.app.document.cell

import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.CellValue.Companion.toCellValue
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.p6.app.document.cell.CellContentImp
import com.qxdzbc.p6.app.document.cell.CellId
import com.qxdzbc.p6.app.document.cell.CellImp
import com.qxdzbc.p6.app.document.cell.CellValue
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import test.TestSample
import kotlin.test.*
class CellImpTest {

    lateinit var ts:TestSample
    lateinit var wbkSt:St<WorkbookKey>
    lateinit var wsNameSt:St<String>
    @BeforeTest
    fun b(){
        ts = TestSample()
        wbkSt = ts.wbKey1Ms
        wsNameSt = ts.wb1.getWs(ts.wsn1)!!.wsNameSt
    }

    @Test
    fun toProto() {
        val cell = CellImp(
            CellId(CellAddress("A1"),wbkSt,wsNameSt),
            content = CellContentImp(
                cellValueMs = 123.toCellValue().toMs(),
            )
        )
        val proto = cell.toProto()
        assertEquals(123.toCellValue().toProto(), proto.value)
        assertEquals(cell.address.toProto(), proto.id.cellAddress)

        val cell2 = CellImp(
            CellId(CellAddress("A1"),wbkSt,wsNameSt),
            content = CellContentImp(
                cellValueMs = 123.0.toCellValue().toMs(),
            )
        )
        val proto2 = cell2.toProto()
    }

    @Test
    fun `toProto missing value and formula`() {
        val cell = CellImp(
            CellId(CellAddress("A1"),wbkSt,wsNameSt),
            content = CellContentImp(
                cellValueMs = CellValue.empty.toMs(),
            )
        )
        val proto = cell.toProto()
        assertFalse { proto.hasFormula() }
    }
}
