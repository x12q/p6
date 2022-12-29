package com.qxdzbc.p6.ui.document.worksheet.state

import androidx.compose.runtime.getValue
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper
import com.qxdzbc.p6.app.document.cell.CellContentImp
import com.qxdzbc.p6.app.document.cell.CellValue.Companion.toCellValue
import com.qxdzbc.p6.app.document.cell.IndCellImp
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.address.CellAddresses
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookImp
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.p6.app.document.worksheet.WorksheetImp
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorIdImp
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorId
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorStateImp
import com.qxdzbc.p6.ui.document.worksheet.cursor.thumb.state.ThumbStateImp
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetStateFactory.Companion.createThenRefresh
import com.qxdzbc.p6.ui.document.cell.state.format.text.CellFormat
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.mockito.kotlin.mock
import test.BaseTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class WorksheetStateImpTest :BaseTest(){
    lateinit var wsStateForWb0Sheet1: WorksheetStateImp
    lateinit var wb0: Workbook
    lateinit var wb1: Workbook
    lateinit var worksheetIDMs: St<WorksheetId>

    @BeforeTest
    fun b() {
        wb0 = WorkbookImp(
            WorkbookKey("Book0").toMs(),
        ).addMultiSheetOrOverwrite(
            listOf(
                WorksheetImp("Sheet1".toMs(), mock()).let {
                    val ws = it
                        .addOrOverwrite(
                            IndCellImp(
                                CellAddress("A1"),
                                CellContentImp(cellValueMs = "a1".toCellValue().toMs())
                            )
                        )
                    ws
                },
            )
        )


        wb1 = WorkbookImp(
            WorkbookKey("Book1").toMs(),
        ).addMultiSheetOrOverwrite(
            listOf(
                WorksheetImp("Sheet1_2".toMs(), mock()).let {
                    val z = it
                        .addOrOverwrite(
                            IndCellImp(
                                CellAddress("A1"),
                                CellContentImp(cellValueMs = "a1".toCellValue().toMs())
                            )
                        )
                        .addOrOverwrite(
                            IndCellImp(
                                CellAddress("A2"),
                                CellContentImp(cellValueMs = "a2".toCellValue().toMs())
                            )
                        )

                    z
                },
                WorksheetImp("Sheet2_2".toMs(), mock()).let {
                    val z = it
                        .addOrOverwrite(
                            IndCellImp(
                                CellAddress("B1"),
                                CellContentImp(cellValueMs = "b1".toCellValue().toMs())
                            )
                        )
                        .addOrOverwrite(IndCellImp(CellAddress("B2"), CellContentImp("b2".toCellValue().toMs())))

                    z
                }
            )

        )

        val p6Comp = ts.comp
        val wsStateFactory = ts.comp.worksheetStateFactory()
        ts.wbContMs.value = ts.wbContMs.value.addOrOverWriteWb(wb0).addOrOverWriteWb(wb1)
        val wssIdMs: Ms<WorksheetId> = ms(
            WorksheetIdImp(
                wsNameMs = "Sheet1".toMs(),
                wbKeySt = wb0.keyMs
            )
        )
        val cellLayoutCoorMapMs: Ms<Map<CellAddress, LayoutCoorWrapper>> = ms(emptyMap())
        val cursorIdMs:Ms<CursorId> = ms(CursorIdImp(wsStateIDMs = wssIdMs))
        val mainCellMs = ms(CellAddresses.A1)
        wsStateForWb0Sheet1 = wsStateFactory.createThenRefresh(
            wsMs = wb0.getWsMs(0)!!,
            sliderMs = p6Comp.gridSliderFactory().create().toMs(),

            cursorStateMs = ms(
                CursorStateImp.forTest(
                    cursorIdMs = cursorIdMs,
                    cellLayoutCoorsMapSt = cellLayoutCoorMapMs,
                    mainCellMs = mainCellMs,
                    thumbStateMs = ms(
                        ThumbStateImp(
                            cursorIdSt =  cursorIdMs,
                            mainCellSt = mainCellMs,
                            cellLayoutCoorMapSt = cellLayoutCoorMapMs
                        )
                    )
                )
            ),
            cellLayoutCoorMapMs = cellLayoutCoorMapMs

        ) as WorksheetStateImp
        worksheetIDMs = wsStateForWb0Sheet1.idMs
    }

    @Test
    fun refreshCellState(){
        test("""
            refresh a worksheet state in which:
            - cell A1 point to valid cell => should be kept
            - cell M10 points to no where => should be removed
            - cell K12 points to no where => should be removed
        """.trimIndent()){
            val labelA1 = "A1"
            val labelK12="K12"
            val labelM10="M10"
            val wsState2 = wsStateForWb0Sheet1
                .addBlankCellState(CellAddress(labelM10))
                .addBlankCellState(CellAddress(labelK12))

            preCondition {
                val cellState = wsState2.getCellState(labelM10)
                cellState.shouldNotBeNull()
                cellState.cell.shouldBeNull()
            }

            val wsState3=wsState2.refreshCellState()

            postCondition {
                wsState3.cellStateCont.allElements.size shouldBe 1

                val cellStateA1 = wsState3.getCellState(labelA1)
                cellStateA1.shouldNotBeNull()
                cellStateA1.cell.shouldNotBeNull()

                wsState3.getCellState(labelK12).shouldBeNull()
                wsState3.getCellState(labelM10).shouldBeNull()
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

    @Test
    fun `effect of chaning ws`() {
        val newWsMs: Ms<Worksheet> = WorksheetImp(nameMs = ms("NewWorksheet"), wbKeySt = ms(ts.wbKey1)).toMs()

        val wsState2 = wsStateForWb0Sheet1.setWsMs(newWsMs)

        assertEquals(newWsMs, wsState2.wsMs)
        assertEquals(newWsMs.value.nameMs, wsState2.id.wsNameMs)
        assertEquals(newWsMs.value.wbKeySt, wsState2.id.wbKeySt)
        assertEquals(newWsMs.value.wbKeySt.value, wsState2.cursorState.id.wbKey)

        assertEquals(newWsMs.value.nameMs.value, wsState2.rowRulerState.wsName)
        assertEquals(newWsMs.value.wbKeySt.value, wsState2.rowRulerState.wbKey)

        assertEquals(newWsMs.value.nameMs.value, wsState2.colRulerState.wsName)
        assertEquals(newWsMs.value.wbKeySt.value, wsState2.colRulerState.wbKey)

    }
}
