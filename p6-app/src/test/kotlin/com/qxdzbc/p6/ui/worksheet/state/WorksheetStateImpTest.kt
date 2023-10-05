package com.qxdzbc.p6.ui.worksheet.state

import androidx.compose.runtime.getValue
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.common.compose.layout_coor_wrapper.P6Layout
import com.qxdzbc.p6.document_data_layer.cell.CellContentImp
import com.qxdzbc.p6.document_data_layer.cell.CellValue.Companion.toCellValue
import com.qxdzbc.p6.document_data_layer.cell.IndCellImp
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddresses
import com.qxdzbc.p6.document_data_layer.workbook.Workbook
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookImp
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import com.qxdzbc.p6.document_data_layer.worksheet.WorksheetImp
import com.qxdzbc.p6.ui.worksheet.cursor.state.CursorIdImp
import com.qxdzbc.p6.ui.worksheet.cursor.state.CursorId
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.mockito.kotlin.mock
import test.BaseAppStateTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class WorksheetStateImpTest : BaseAppStateTest() {
    lateinit var wsStateForWb0Sheet1: WorksheetStateImp
    lateinit var wb0: Workbook
    lateinit var wb1: Workbook
    lateinit var worksheetIDMs: St<WorksheetId>

    @BeforeTest
    fun b() {
        wb0 = WorkbookImp(
            WorkbookKey("Book0").toMs(),
        ).apply {
            addMultiSheetOrOverwrite(
                listOf(
                    WorksheetImp("Sheet1".toMs(), mock()).apply {

                        addOrOverwrite(
                            IndCellImp(
                                CellAddress("A1"),
                                CellContentImp(cellValueMs = "a1".toCellValue().toMs())
                            )
                        )
                    },
                )
            )
        }


        wb1 = WorkbookImp(
            WorkbookKey("Book1").toMs(),
        ).apply {
            addMultiSheetOrOverwrite(
                listOf(
                    WorksheetImp("Sheet1_2".toMs(), mock()).apply {
                        addOrOverwrite(
                            IndCellImp(
                                CellAddress("A1"),
                                CellContentImp(cellValueMs = "a1".toCellValue().toMs())
                            )
                        )
                        addOrOverwrite(
                            IndCellImp(
                                CellAddress("A2"),
                                CellContentImp(cellValueMs = "a2".toCellValue().toMs())
                            )
                        )
                    },
                    WorksheetImp("Sheet2_2".toMs(), mock()).apply {
                        addOrOverwrite(
                            IndCellImp(
                                CellAddress("B1"),
                                CellContentImp(cellValueMs = "b1".toCellValue().toMs())
                            )
                        )
                        addOrOverwrite(IndCellImp(CellAddress("B2"), CellContentImp("b2".toCellValue().toMs())))
                    }
                )

            )
        }

        val p6Comp = ts.comp
        val wsStateFactory = ts.comp.wsStateFactory()
        ts.wbCont.apply {
            removeWb(wb0.key)
            removeWb(wb1.key)
            addWb(wb0)
            addWb(wb1)
        }
        val wssIdMs: Ms<WorksheetId> = ms(
            WorksheetIdImp(
                wsNameMs = "Sheet1".toMs(),
                wbKeySt = wb0.keyMs
            )
        )
        val cellLayoutCoorMapMs: Ms<Map<CellAddress, P6Layout>> = ms(emptyMap())
        val cursorIdMs: Ms<CursorId> = ms(CursorIdImp(wsStateIDMs = wssIdMs))
        val mainCellMs = ms(CellAddresses.A1)
        wsStateForWb0Sheet1 = wsStateFactory.create(
            wsMs = wb0.getWsMs(0)!!,
        ) as WorksheetStateImp
        worksheetIDMs = wsStateForWb0Sheet1.idMs
    }

    @Test
    fun refreshCellState() {
        test(
            """
            refresh a worksheet state in which:
            - cell A1 point to valid cell => should be kept
            - cell M10 points to no where => should be removed
            - cell K12 points to no where => should be removed
        """.trimIndent()
        ) {
            val labelA1 = "A1"
            val labelK12 = "K12"
            val labelM10 = "M10"
            val wsState2 = wsStateForWb0Sheet1.apply {
                addBlankCellState(CellAddress(labelM10))
                addBlankCellState(CellAddress(labelK12))
            }


            preCondition {
                val cellState = wsState2.getCellState(labelM10)
                cellState.shouldNotBeNull()
                cellState.cell.shouldBeNull()
            }

            wsState2.refreshCellState()

            postCondition {
                wsState2.cellStateCont.allElements.size shouldBe 1

                val cellStateA1 = wsState2.getCellState(labelA1)
                cellStateA1.shouldNotBeNull()
                cellStateA1.cell.shouldNotBeNull()

                wsState2.getCellState(labelK12).shouldBeNull()
                wsState2.getCellState(labelM10).shouldBeNull()
            }
        }
    }

    @Test
    fun `effects of changing ws state id`() {
        val wsId by worksheetIDMs
        val nn = ms("new name")
        val newWsId = wsId.pointToWsNameMs(nn)
        assertEquals(nn.value, newWsId.wsName)
    }

//    @Test
//    fun `effect of chaning ws`() {
//        val newWsMs: Ms<Worksheet> = WorksheetImp(nameMs = ms("NewWorksheet"), wbKeySt = ms(ts.wbKey1)).toMs()
//
//        val wsState2 = wsStateForWb0Sheet1.setWsMs(newWsMs)
//
//        assertEquals(newWsMs, wsState2.wsMs)
//        assertEquals(newWsMs.value.nameMs, wsState2.id.wsNameMs)
//        assertEquals(newWsMs.value.wbKeySt, wsState2.id.wbKeySt)
//        assertEquals(newWsMs.value.wbKeySt.value, wsState2.cursorState.id.wbKey)
//
//        assertEquals(newWsMs.value.nameMs.value, wsState2.rowRulerState.wsName)
//        assertEquals(newWsMs.value.wbKeySt.value, wsState2.rowRulerState.wbKey)
//
//        assertEquals(newWsMs.value.nameMs.value, wsState2.colRulerState.wsName)
//        assertEquals(newWsMs.value.wbKeySt.value, wsState2.colRulerState.wbKey)
//
//    }
}
