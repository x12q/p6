package com.qxdzbc.p6.app.document.worksheet

import com.qxdzbc.p6.app.common.table.TableCR
import com.qxdzbc.p6.app.common.table.ImmutableTableCR
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.d.Cell
import com.qxdzbc.p6.app.document.cell.d.CellImp
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddresses
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
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
        val table: TableCR<Int, Int, Ms<Cell>> = ImmutableTableCR(
            mapOf(
                1 to mapOf(
                    1 to CellImp(CellAddress("A1")).toMs(),
                    2 to CellImp(CellAddress("A2")).toMs(),
                ),
                2 to mapOf(
                    1 to CellImp(CellAddress("B1")).toMs(),
                    2 to CellImp(CellAddress("B2")).toMs(),
                ),
                3 to mapOf(
                    1 to CellImp(CellAddress("C1")).toMs(),
                    2 to CellImp(CellAddress("C2")).toMs(),
                    3 to CellImp(CellAddress("C3")).toMs(),
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
        val table: TableCR<Int, Int, Ms<Cell>> = ImmutableTableCR(
            mapOf(
                1 to mapOf(
                    1 to CellImp(CellAddress("A1")).toMs(),
                    2 to CellImp(CellAddress("A2")).toMs(),
                ),
                2 to mapOf(
                    1 to CellImp(CellAddress("B1")).toMs(),
                    2 to CellImp(CellAddress("B2")).toMs(),
                ),
                3 to mapOf(
                    1 to CellImp(CellAddress("C1")).toMs(),
                    2 to CellImp(CellAddress("C2")).toMs(),
                    3 to CellImp(CellAddress("C3")).toMs(),
                )
            )
        )
        val ws = WorksheetImp("Sheet1".toMs(), wbkMs,table)
        val rs = ws.getCellsInRange(RangeAddresses.wholeCol(1)).map { it.address }
        assertEquals(2, rs.size)
        listOf("A1", "A2").map { CellAddress(it) }.forEach {
            assertTrue { it in rs }
        }
    }

}
