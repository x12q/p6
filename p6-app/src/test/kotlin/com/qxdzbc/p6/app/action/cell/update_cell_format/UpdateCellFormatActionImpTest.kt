package com.qxdzbc.p6.app.action.cell.update_cell_format

import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.document.cell.CellId
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.ui.document.cell.state.CellStates
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.format2.*
import io.kotest.matchers.nulls.shouldBeNull
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
    fun makeCommandToApplyFormatConfig() {
        val formatConfig = FormatConfig.random()
        val command = action.makeCommandToApplyFormatConfig(
            wbwsSt, formatConfig
        )

        fun resultFormat(): FormatConfig? {
            return sc.getCellFormatTable(wbwsSt)?.getFormatConfigForConfig_Respectively(formatConfig)
        }
        resultFormat()?.isInvalid() shouldBe true
        command.run()
        val q = resultFormat()
        q shouldBe formatConfig


    }


    @Test
    fun makeCommandForFormatOneCell() {
        /*
         Action set cell A1 text size to 123.
         When undid, this action should reverse cell A1 text size to null
         */

        val cellFormatTableMs = ts.sc.getCellFormatTableMs(cellA1Id)!!
        val cellFormatTable by cellFormatTableMs
        val fontSize = 123f
        val action = action.makeCommandForFormatOneCell(
            cellId = cellA1Id,
            formatValue = fontSize,
            cellFormatTableMs = cellFormatTableMs,
            getFormatTable = CellFormatTable::textSizeTable,
            updateCellFormatTable = CellFormatTable::setTextSizeTable
        )
        cellFormatTable.textSizeTable.getFirstValue(cellA1Id.address).shouldBeNull()
        action.run()
        cellFormatTable.textSizeTable.getFirstValue(cellA1Id.address) shouldBe fontSize
        action.undo()
        cellFormatTable.textSizeTable.getFirstValue(cellA1Id.address).shouldBeNull()
    }

    @Test
    fun makeCommandForFormattingOnSelectedCells() {
        /*

         */
        val mockCursorState = mock<CursorState> {
            whenever(it.allRanges) doReturn listOf("A1:A3", "B2:D10").map { RangeAddress(it) }
            whenever(it.allFragCells) doReturn listOf("Q10").map { CellAddress(it) }
        }
        val cellFormatTableMs = ts.sc.getCellFormatTableMs(cellA1Id)!!
        val cellFormatTable by cellFormatTableMs
        val fontSize = 123f
        val action = action.makeCommandForFormattingOnSelectedCells(
            formatValue = fontSize,
            cursorState = mockCursorState,
            cellFormatTableMs = cellFormatTableMs,
            getFormatTable = CellFormatTable::textSizeTable,
            updateCellFormatTable = CellFormatTable::setTextSizeTable
        )
        preCondition {
            mockCursorState.allRanges.forEach { r ->
                cellFormatTable.textSizeTable.getFirstValue(r).shouldBeNull()
            }
            mockCursorState.allFragCells.forEach { c ->
                cellFormatTable.textSizeTable.getFirstValue(c).shouldBeNull()
            }
        }

        action.run()
        postCondition {
            mockCursorState.allRanges.forEach { r ->
                cellFormatTable.textSizeTable.getFirstValue(r) shouldBe fontSize
            }
            mockCursorState.allFragCells.forEach { c ->
                cellFormatTable.textSizeTable.getFirstValue(c) shouldBe fontSize
            }
        }

        action.undo()

        postCondition {
            mockCursorState.allRanges.forEach { r ->
                cellFormatTable.textSizeTable.getFirstValue(r).shouldBeNull()
            }
            mockCursorState.allFragCells.forEach { c ->
                cellFormatTable.textSizeTable.getFirstValue(c).shouldBeNull()
            }
        }
    }

    @Test
    fun updateCellFormatTableWithNewFormatValueOnSelectedCells() {
        val r1 = RangeAddress("B3:E12")
        val c1 = Color(123)
        val r2 = RangeAddress("C7:D16")
        val c2 = Color(222)
        val cursorState2 = mock<CursorState> {
            whenever(it.allRanges) doReturn (listOf(r2))
            whenever(it.allFragCells) doReturn (listOf(r2.topLeft))
        }
        val table0 = CellFormatTableImp().setCellBackgroundColorTable(
            FormatTableImp(
                mapOf(
                    RangeAddressSetImp(r1) to c1
                )
            )
        )

        val table2 = action.updateCellFormatTableWithNewFormatValueOnSelectedCells(
            formatValue = c2,
            ranges = cursorState2.allRanges,
            cells = cursorState2.allFragCells,
            currentFormatTable = table0,
            getFormatTable = {
                it.cellBackgroundColorTable
            },
            updateCellFormatTable = { cft, newTable ->
                cft.setCellBackgroundColorTable(newTable)
            }
        )
        table2.cellBackgroundColorTable.getFirstValue(r2) shouldBe c2

        val i = r2.cellIterator
        while (i.hasNext()) {
            table2.cellBackgroundColorTable.getFirstValue(i.next()) shouldBe c2
        }

        r1.getNotIn(r2).forEach {
            table2.cellBackgroundColorTable.getFirstValue(it) shouldBe c1
        }

    }
}
