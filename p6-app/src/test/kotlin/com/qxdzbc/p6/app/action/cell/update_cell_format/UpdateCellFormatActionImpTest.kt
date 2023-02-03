package com.qxdzbc.p6.app.action.cell.update_cell_format

import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.document.cell.CellId
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.ui.document.cell.state.CellStates
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.format.*
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import test.BaseAppStateTest
import kotlin.test.BeforeTest
import kotlin.test.Test

internal class UpdateCellFormatActionImpTest : BaseAppStateTest() {

    lateinit var action: UpdateCellFormatActionImp
    lateinit var wbwsSt: WbWsSt
    val cellA1Id get() = CellId(CellAddress("A1"), wbwsSt)
    val cellB1Id get() = CellId(CellAddress("B1"), wbwsSt)
    val cellStateA1 = CellStates.blank(CellAddress("A1"))
    val cellStateB1 = CellStates.blank(CellAddress("B1"))


    @BeforeTest
    fun b() {
        action = comp.updateCellFormatAction()
        wbwsSt = ts.sc.getWbWsSt(ts.wbKey1, ts.wsn1)!!
    }
    @Test
    fun makeClearFormatCommand(){
        val config1 = FormatConfig.random()

        fun getConfig1(): FormatConfig? {
            return sc.getCellFormatTable(wbwsSt)?.getFormatConfigForConfig_Respectively(config1)
        }
        preCondition {
            action.applyFormatConfig(wbwsSt,config1,undoable = false)
            getConfig1() shouldBe config1
        }

        val command = action.makeClearFormatCommandUsingFormatConfig_Respective(wbwsSt,config1)

        command.run()
        getConfig1() shouldNotBe config1
        getConfig1()?.isInvalid() shouldBe true
        command.undo()
        getConfig1() shouldBe config1

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
        resultFormat() shouldBe formatConfig

        command.undo()
        resultFormat() shouldNotBe formatConfig
        resultFormat()?.isInvalid() shouldBe true
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
        cellFormatTable.textSizeTable.getFormatValue(cellA1Id.address).shouldBeNull()
        action.run()
        cellFormatTable.textSizeTable.getFormatValue(cellA1Id.address) shouldBe fontSize
        action.undo()
        cellFormatTable.textSizeTable.getFormatValue(cellA1Id.address).shouldBeNull()
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
                cellFormatTable.textSizeTable.getFormatValue(r).shouldBeNull()
            }
            mockCursorState.allFragCells.forEach { c ->
                cellFormatTable.textSizeTable.getFormatValue(c).shouldBeNull()
            }
        }

        action.run()
        postCondition {
            mockCursorState.allRanges.forEach { r ->
                cellFormatTable.textSizeTable.getFormatValue(r) shouldBe fontSize
            }
            mockCursorState.allFragCells.forEach { c ->
                cellFormatTable.textSizeTable.getFormatValue(c) shouldBe fontSize
            }
        }

        action.undo()

        postCondition {
            mockCursorState.allRanges.forEach { r ->
                cellFormatTable.textSizeTable.getFormatValue(r).shouldBeNull()
            }
            mockCursorState.allFragCells.forEach { c ->
                cellFormatTable.textSizeTable.getFormatValue(c).shouldBeNull()
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
        table2.cellBackgroundColorTable.getFormatValue(r2) shouldBe c2

        val i = r2.cellIterator
        while (i.hasNext()) {
            table2.cellBackgroundColorTable.getFormatValue(i.next()) shouldBe c2
        }

        r1.getNotIn(r2).forEach {
            table2.cellBackgroundColorTable.getFormatValue(it) shouldBe c1
        }

    }
}
