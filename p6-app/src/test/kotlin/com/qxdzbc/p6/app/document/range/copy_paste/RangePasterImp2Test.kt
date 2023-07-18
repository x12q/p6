package com.qxdzbc.p6.app.document.range.copy_paste

import com.qxdzbc.p6.app.action.range.RangeIdDM
import com.qxdzbc.p6.app.document.cell.Cells
import com.qxdzbc.p6.app.document.range.RangeCopy
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookImp
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.worksheet.WorksheetImp
import com.qxdzbc.common.compose.StateUtils.toMs
import com.github.michaelbull.result.Ok
import io.kotest.matchers.shouldBe
import kotlin.test.*
class RangePasterImp2Test {

    /**
     * Paste A1:A3 -> B5:C9
     */
    @Test
    fun paste() {
        val wbk = WorkbookKey("")
        val wbkMs = wbk.toMs()
        val ws = run{
            val a1 =  Cells.number("A1",1)
            val cells = listOf(
                a1,
                Cells.number("A2",2),
//                Cells.number("A3",3),
            ).map{it.toMs()}
            cells[0].value.currentValue shouldBe 1.0
            WorksheetImp.fromCellList(
                name = "S1",
                cellList = cells,
                wbKeyMs =wbkMs
            )
        }
        ws.getCell("A1")!!.currentValue shouldBe 1.0
        val wb = WorkbookImp(
            keyMs = wbkMs,
            worksheetMsList = listOf(ws)
        )
        val rangeCopy = RangeCopy(
            rangeId = RangeIdDM(
                rangeAddress = RangeAddress("A1:A3"),
                wbKey = wbk,
                wsName = "S1"
            ),
            cells = wb.worksheets[0].cells
        )
        val target= RangeIdDM(
            rangeAddress = RangeAddress("B5:C9"),
            wbKey = wbk,
            wsName = "S1"
        )
        val rs = RangeRangePasterImp.pasteRs(rangeCopy,target,wb)
        assertTrue { rs is Ok }
        val wb2 = rs.component1()!!
        val s2 = wb2.worksheets[0]

        val expect = mapOf(
            "B5" to 1.0,
//            "C5" to 1.0,
//            "D5" to null,
//            "B6" to 2.0, "C6" to 2.0, "D6" to null,
//            "B7" to 3.0, "C7" to 3.0,
//            "B8" to 1.0, "C8" to 1.0,
//            "B9" to 2.0, "C9" to 2.0,
//            "B10" to null, "C10" to null

        )
        for((l,o) in expect){
            assertEquals(o,s2.getCell(l)?.currentValue as Double?,l)
        }
    }
}
