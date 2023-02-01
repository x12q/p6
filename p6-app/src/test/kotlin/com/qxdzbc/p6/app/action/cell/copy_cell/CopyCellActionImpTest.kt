package com.qxdzbc.p6.app.action.cell.copy_cell

import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.qxdzbc.p6.app.action.cell.cell_update.CellUpdateRequestDM
import com.qxdzbc.p6.app.action.cell.cell_update.UpdateCellAction
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.rpc.cell.msg.CellContentDM
import com.qxdzbc.p6.rpc.cell.msg.CellIdDM
import com.qxdzbc.p6.rpc.cell.msg.CopyCellRequest
import com.qxdzbc.p6.ui.document.cell.state.format.text.CellFormatImp
import com.qxdzbc.p6.ui.format.FormatConfig
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import test.BaseAppStateTest
import kotlin.test.*

internal class CopyCellActionImpTest : BaseAppStateTest() {
    lateinit var toCell: CellIdDM
    lateinit var act: CopyCellActionImp
    lateinit var updateCellAct: UpdateCellAction
    lateinit var fromCell: CellIdDM

    @BeforeTest
    fun b() {
        act = ts.comp.copyCellActionImp()
        updateCellAct = ts.comp.updateCellAction()
        fromCell = CellIdDM(
            address = CellAddress("A1"),
            wbKey = ts.wbKey1,
            wsName = ts.wsn1
        )
        toCell = CellIdDM(
            address = CellAddress("A2"),
            wbKey = ts.wbKey1,
            wsName = ts.wsn1
        )
    }

    @Test
    fun makeCommand() {
        test("Copy from A1 -> A2, then undo") {
            val request = CopyCellRequest(
                fromCell, toCell,
                undoable = true
            )

            val toCellFormat = CellFormatImp(textSize = 123f, isCrossed = true)
            val toCellFormatConfig = FormatConfig.fromCellFormat(toCell.address,toCellFormat)

            val fromCellColor = Color.Black
            val fromCellFormat = CellFormatImp(textColor = fromCellColor)
            val fromCellFormatConfig = FormatConfig.fromCellFormat(fromCell.address, fromCellFormat)

            preCondition("A1 has value = 1, with custom text color, A2 has value 3, with custom text size") {
                updateCellAct.updateCellDM(
                    cellId = fromCell,
                    cellContent = CellContentDM.fromAny(1), false
                )
                updateCellAct.updateCellDM(
                    cellId = toCell,
                    cellContent = CellContentDM.fromAny(3), false
                )
                sc.getCell(fromCell)?.currentValue shouldBe 1.0
                sc.getCell(toCell)?.currentValue shouldBe 3.0

                sc.getCellFormatTableMs(fromCell)?.also {
                    it.value = it.value.applyConfig(fromCellFormatConfig)
                }

                sc.getCellFormatTableMs(toCell)?.also {
                    it.value = it.value.applyConfig(toCellFormatConfig)
                }

                sc.getCellFormatTable(toCell)!!.getFormat(toCell.address) shouldNotBe fromCellFormat

            }
            val command = act.makeCopyCommand(request)

            command.run()
            postCondition {
                sc.getCell(toCell)?.currentValue shouldBe 1.0
                sc.getCellFormatTable(toCell)!!.getFormat(toCell.address) shouldBe fromCellFormat
            }

            command.undo()
            postCondition {
                sc.getCell(toCell)?.currentValue shouldBe 3.0
                sc.getCellFormatTable(toCell)!!.getFormat(toCell.address) shouldBe toCellFormat
            }
        }

    }

    @Test
    fun copyFormat() {
        /*
        Copy format from cell A1 -> A2
         */
        val sc = ts.sc
        val a1Format = CellFormatImp.random()
        val fTable1Ms = sc.getCellFormatTableMs(fromCell)!!
        val fTable1 by fTable1Ms
        val formatTable2Ms = sc.getCellFormatTableMs(toCell)!!
        val fTable2 by formatTable2Ms

        preCondition {
            fTable1Ms.value = fTable1.setFormat(fromCell.address, a1Format)
            fTable1.getFormat(fromCell.address) shouldBe a1Format
            fTable2.getFormat(toCell.address) shouldNotBe a1Format
        }

        act.copyCellWithoutClipboard(
            CopyCellRequest(
                fromCell = fromCell,
                toCell = toCell,
                shiftRange = true
            )
        )

        postCondition {
            fTable1.getFormat(fromCell.address) shouldBe a1Format
            fTable2.getFormat(toCell.address) shouldBe a1Format
        }
    }

    @Test
    fun copyFormula() {
        test(
            """
        Let cell A1 = C1+1
        copy formula A1 to A2, expect A2 to have formula =C2+1 == 3    
        """
        ) {
            // write a value to C1
            updateCellAct.updateCellDM(
                request = CellUpdateRequestDM(
                    cellId = CellIdDM(
                        address = CellAddress("C1"),
                        wbKey = ts.wbKey1,
                        wsName = ts.wsn1
                    ),
                    cellContent = CellContentDM.fromAny(1)
                )
            )
            // write a value to D1
            updateCellAct.updateCellDM(
                request = CellUpdateRequestDM(
                    cellId = CellIdDM(
                        address = CellAddress("C2"),
                        wbKey = ts.wbKey1,
                        wsName = ts.wsn1
                    ),
                    cellContent = CellContentDM.fromAny(2)
                )
            )
            // write A1: =C1+1
            updateCellAct.updateCellDM(
                request = CellUpdateRequestDM(
                    cellId = fromCell,
                    cellContent = CellContentDM.fromFormula("=C1+1")
                )
            )

            preCondition {
                assertNull(ts.sc.getCellOrDefault(toCell)?.currentValue)
            }

            // copy A1 to A2, expect A2 to have formula =C2+1 == 3
            act.copyCellWithoutClipboard(
                CopyCellRequest(
                    fromCell = fromCell,
                    toCell = toCell,
                    shiftRange = true
                )
            )

            postCondition {
                assertTrue(ts.sc.getCellOrDefault(toCell)?.isFormula ?: false)
                assertEquals("=C2 + 1", ts.sc.getCellOrDefault(toCell)?.shortFormulaFromExUnit)
                assertEquals(3.0, ts.sc.getCellOrDefault(toCell)?.currentValue)
                ts.sc.getCellOrDefault(toCell)?.cachedDisplayText shouldBe "3"
                assertEquals(3.0, ts.sc.getCellOrDefault(toCell)?.valueAfterRun)
            }
        }
    }

    @Test
    fun copyValue() {
        val content = "abc"
        updateCellAct.updateCellDM(
            request = CellUpdateRequestDM(
                cellId = fromCell,
                cellContent = CellContentDM.fromAny(content)
            )
        )
        assertNull(ts.sc.getCellOrDefault(toCell)?.currentValue)
        act.copyCellWithoutClipboard(
            CopyCellRequest(
                fromCell = fromCell,
                toCell = toCell,
            )
        )
        ts.sc.getCellOrDefault(toCell)?.currentValue shouldBe content
        ts.sc.getCellOrDefault(toCell)?.cachedDisplayText shouldBe content
    }
}
