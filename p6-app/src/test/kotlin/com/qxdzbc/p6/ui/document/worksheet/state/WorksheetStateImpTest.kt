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
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorStateId
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorStateImp
import com.qxdzbc.p6.ui.document.worksheet.cursor.thumb.state.ThumbStateImp
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetStateFactory.Companion.createRefresh
import org.mockito.kotlin.mock
import test.TestSample
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class WorksheetStateImpTest {
    lateinit var wsState: WorksheetStateImp
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
                    val z = it
                        .addOrOverwrite(
                            IndCellImp(
                                CellAddress("A1"),
                                CellContentImp(cellValueMs = "a1".toCellValue().toMs())
                            )
                        )
                    z
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

        val testSample = TestSample()
        val p6Comp = testSample.comp
        val wsStateFactory = testSample.comp.worksheetStateFactory()
        testSample.wbContMs.value = testSample.wbContMs.value.addOrOverWriteWb(wb0).addOrOverWriteWb(wb1)
        val wssIdMs: Ms<WorksheetId> = ms(
            WorksheetIdImp(
                wsNameMs = "Sheet1".toMs(),
                wbKeySt = wb0.keyMs
            )
        )
        val cellLayoutCoorMapMs: Ms<Map<CellAddress, LayoutCoorWrapper>> = ms(emptyMap())
        val cursorIdMs:Ms<CursorStateId> = ms(CursorIdImp(wsStateIDMs = wssIdMs))
        val mainCellMs = ms(CellAddresses.A1)
        wsState = wsStateFactory.createRefresh(
            wsMs = wb0.getWsMs(0)!!,
            sliderMs = p6Comp.gridSliderFactory().create().toMs(),

            cursorStateMs = ms(
                CursorStateImp.default(
                    cursorIdMs = cursorIdMs,
                    cellLayoutCoorsMapSt = cellLayoutCoorMapMs,
                    mainCellMs = mainCellMs,
                    thumbStateMs = ms(
                        ThumbStateImp(
                            cursorStateIdSt =  cursorIdMs,
                            mainCellSt = mainCellMs,
                            cellLayoutCoorMapSt = cellLayoutCoorMapMs
                        )
                    )
                )
            ),
            cellLayoutCoorMapMs = cellLayoutCoorMapMs

        ) as WorksheetStateImp
        worksheetIDMs = wsState.idMs
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
        val newWsMs: Ms<Worksheet> = WorksheetImp(nameMs = ms("NewWorksheet"), wbKeySt = ms(TestSample.wbk1)).toMs()

        val wsState2 = wsState.setWsMs(newWsMs)

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
