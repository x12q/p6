package com.qxdzbc.p6.document_data_layer.worksheet

import com.qxdzbc.p6.common.table.TableCR
import com.qxdzbc.p6.common.table.ImmutableTableCR
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.document_data_layer.cell.Cell
import com.qxdzbc.p6.document_data_layer.cell.IndCellImp
import com.qxdzbc.p6.document_data_layer.range.address.RangeAddress
import com.qxdzbc.p6.document_data_layer.range.address.RangeAddressUtils
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.toMs
import test.TestSample
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class WorksheetImpTest {

    lateinit var ws: WorksheetImp
    lateinit var wbkMs: Ms<WorkbookKey>
    @BeforeTest
    fun beforeTest() {
        wbkMs = TestSample.wbk1.toMs()
        ws = WorksheetImp("Sheet1".toMs(), wbKeySt = wbkMs)
    }

    @Test(timeout = 1000)
    fun getCellsInRange() {
        val table: TableCR<Int, Int, Ms<com.qxdzbc.p6.document_data_layer.cell.Cell>> = ImmutableTableCR(
            mapOf(
                1 to mapOf(
                    1 to IndCellImp(CellAddress("A1")).toMs(),
                    2 to IndCellImp(CellAddress("A2")).toMs(),
                ),
                2 to mapOf(
                    1 to IndCellImp(CellAddress("B1")).toMs(),
                    2 to IndCellImp(CellAddress("B2")).toMs(),
                ),
                3 to mapOf(
                    1 to IndCellImp(CellAddress("C1")).toMs(),
                    2 to IndCellImp(CellAddress("C2")).toMs(),
                    3 to IndCellImp(CellAddress("C3")).toMs(),
                )
            )
        )
        val ws = WorksheetImp("Sheet1".toMs(), wbkMs ,table)
        val rs = ws.getCellsInRange(
            RangeAddress(1..2, 1..2)
        ).map { it.address }
        assertEquals(4, rs.size)
        listOf("A1", "A2", "B1", "B2").map { CellAddress(it) }.forEach {
            assertTrue { it in rs }
        }
    }

    @Test(timeout = 1000)
//    @Test
    fun getCellsInRange_wholeCol() {
        val table: TableCR<Int, Int, Ms<com.qxdzbc.p6.document_data_layer.cell.Cell>> = ImmutableTableCR(
            mapOf(
                1 to mapOf(
                    1 to IndCellImp(CellAddress("A1")).toMs(),
                    2 to IndCellImp(CellAddress("A2")).toMs(),
                ),
                2 to mapOf(
                    1 to IndCellImp(CellAddress("B1")).toMs(),
                    2 to IndCellImp(CellAddress("B2")).toMs(),
                ),
                3 to mapOf(
                    1 to IndCellImp(CellAddress("C1")).toMs(),
                    2 to IndCellImp(CellAddress("C2")).toMs(),
                    3 to IndCellImp(CellAddress("C3")).toMs(),
                )
            )
        )
        val ws = WorksheetImp("Sheet1".toMs(), wbkMs,table)
        val rs = ws.getCellsInRange(RangeAddressUtils.rangeForWholeCol(1)).map { it.address }
        assertEquals(2, rs.size)
        listOf("A1", "A2").map { CellAddress(it) }.forEach {
            assertTrue { it in rs }
        }
    }

}
