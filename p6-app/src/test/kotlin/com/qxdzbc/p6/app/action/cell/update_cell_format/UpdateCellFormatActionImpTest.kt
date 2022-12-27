package com.qxdzbc.p6.app.action.cell.update_cell_format

import androidx.compose.ui.graphics.Color
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.document.cell.CellId
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.ui.document.cell.state.CellStates
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.format2.CellFormatTableImp
import com.qxdzbc.p6.ui.format2.FormatTableImp
import com.qxdzbc.p6.ui.format2.RangeAddressSetImp
import io.kotest.matchers.shouldBe
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import test.BaseTest
import kotlin.test.BeforeTest
import kotlin.test.Test

internal class UpdateCellFormatActionImpTest : BaseTest() {

    lateinit var action: UpdateCellFormatActionImp
    lateinit var wbwsSt: WbWsSt
    val cellA1Id get() = CellId(CellAddress("A1"), wbwsSt)
    val cellB1Id get() = CellId(CellAddress("B1"), wbwsSt)
    val cellStateA1 = CellStates.blank(CellAddress("A1"))
    val cellStateB1 = CellStates.blank(CellAddress("B1"))


    @BeforeTest
    fun b() {
        action = UpdateCellFormatActionImp(
            stateContainerSt = comp.stateContMs()
        )
        wbwsSt = ts.sc.getWbWsSt(ts.wbKey1, ts.wsn1)!!
    }

    @Test
    fun updateCellFormatTableWithNewFormatValueOnSelectedCells(){
        val r1 = RangeAddress("B3:E12")
        val c1 = Color(123)
        val r2 = RangeAddress("C7:D16")
        val c2 = Color(222)
        val cursorState2 = mock<CursorState>{
            whenever(it.allRanges) doReturn (listOf(r2))
            whenever(it.allFragCells) doReturn (listOf(r2.topLeft))
        }
        val table0 = CellFormatTableImp().setCellBackgroundColorTable(
            FormatTableImp(mapOf(
                RangeAddressSetImp(r1) to c1
            ))
        )

        val table2= action.updateCellFormatTableWithNewFormatValueOnSelectedCells(
            formatValue =c2,
            ranges = cursorState2.allRanges,
            cells = cursorState2.allFragCells,
            currentFormatTable=table0,
            getFormatTable={
                it.cellBackgroundColorTable
            },
            updateCellFormatTable = { newTable,cft->
                cft.setCellBackgroundColorTable(newTable)
            }
        )
        table2.cellBackgroundColorTable.getFirstValue(r2) shouldBe c2

        val i=r2.cellIterator
        while(i.hasNext()){
            table2.cellBackgroundColorTable.getFirstValue(i.next()) shouldBe c2
        }

        r1.getNotIn(r2).forEach {
            table2.cellBackgroundColorTable.getFirstValue(it) shouldBe c1
        }

    }
}
