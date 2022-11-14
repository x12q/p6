package com.qxdzbc.p6.app.document.cell

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.map
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.CellValue.Companion.toCellValue
import com.qxdzbc.common.compose.StateUtils.toMs
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
    fun reRunRs(){
        val cell=CellImp(
            id = CellId(CellAddress("A1"),ts.sc.getWbWsSt(ts.wbKey1,ts.wsn1)!!),
            content=CellContentImp(
                ms(CellValue.from(123))
            )
        )
        assertEquals("",cell.cachedDisplayText)
        assertEquals(null,cell.error0)
        val c2Rs = cell.reRunRs().map { it.evaluateDisplayText() }
        assertTrue(c2Rs is Ok)
        val c2 = c2Rs.value
        assertEquals("123",c2.cachedDisplayText)
        assertEquals(null,c2.error0)
    }

    @Test
    fun `reRunRs circular reference`(){
//        val cellId = CellId(CellAddress("A1"),ts.sc.getWbWsSt(ts.wbKey1,ts.wsn1)!!)
//        val cell = ts.sc.getCellRs(cellId)
//        val cell=CellImp(
//            id = cellId,
//            content=CellContentImp(
//                exUnit = LambdaUnit{
//                    ts.sc.getCellRs(cellId)
//                }
//            )
//        )
//        assertEquals("", cell.displayText)
//        assertEquals(null, cell.error0)
//        val c2Rs = cell.reRunRs()
//        assertTrue(c2Rs is Ok)
//        val c2 = c2Rs.value
//        assertNotNull(c2.error0)
//        assertEquals("#ERR",c2.displayText)
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
