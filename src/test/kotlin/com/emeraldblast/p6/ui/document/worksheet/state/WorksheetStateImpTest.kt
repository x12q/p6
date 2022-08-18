package com.emeraldblast.p6.ui.document.worksheet.state

import androidx.compose.runtime.getValue
import com.emeraldblast.p6.app.document.cell.address.CellAddress
import com.emeraldblast.p6.app.document.cell.d.CellImp
import com.emeraldblast.p6.app.document.cell.d.CellValue.Companion.toCellValue
import com.emeraldblast.p6.app.document.cell.d.CellContentImp
import com.emeraldblast.p6.app.document.workbook.Workbook
import com.emeraldblast.p6.app.document.workbook.WorkbookImp
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.app.document.worksheet.Worksheet
import com.emeraldblast.p6.app.document.worksheet.WorksheetImp
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.common.compose.StateUtils.toMs
import com.emeraldblast.p6.ui.common.compose.St
import com.emeraldblast.p6.ui.common.compose.ms
import com.emeraldblast.p6.ui.document.worksheet.cursor.state.CursorStateImp
import com.emeraldblast.p6.ui.document.worksheet.state.WorksheetStateFactory.Companion.createRefresh
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
                            CellImp(
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
                            CellImp(
                                CellAddress("A1"),
                                CellContentImp(cellValueMs = "a1".toCellValue().toMs())
                            )
                        )
                        .addOrOverwrite(
                            CellImp(
                                CellAddress("A2"),
                                CellContentImp(cellValueMs = "a2".toCellValue().toMs())
                            )
                        )

                    z
                },
                WorksheetImp("Sheet2_2".toMs(), mock()).let {
                    val z = it
                        .addOrOverwrite(
                            CellImp(
                                CellAddress("B1"),
                                CellContentImp(cellValueMs = "b1".toCellValue().toMs())
                            )
                        )
                        .addOrOverwrite(CellImp(CellAddress("B2"), CellContentImp("b2".toCellValue().toMs())))

                    z
                }
            )

        )

        val testSample = TestSample()
        val p6Comp = testSample.p6Comp
        val wsStateFactory = testSample.p6Comp.worksheetStateFactory()
        testSample.wbContMs.value = testSample.wbContMs.value.addOrOverWriteWb(wb0).addOrOverWriteWb(wb1)
        val wssIdMs: Ms<WorksheetId> = ms(
            WorksheetIdImp(
                wsNameMs = "Sheet1".toMs(),
                wbKeySt = wb0.keyMs
            )
        )
        wsState = wsStateFactory.createRefresh(
//            idMs = wssIdMs,
            worksheetMs = wb0.getWsMs(0)!!,
            sliderMs = p6Comp.gridSliderFactory().create().toMs(),
            cursorStateMs = ms(
                CursorStateImp
                    .default2(
                        worksheetIDMs = wssIdMs
                    )
            )
        ) as WorksheetStateImp
        worksheetIDMs = wsState.idMs
    }

    @Test
    fun `effects of changing ws state id`() {
        val wsId by worksheetIDMs
        val nn = ms("new name")
        val newWsId= wsId.pointToWsNameMs(nn)
        assertEquals(nn.value, newWsId.wsName)
    }

    @Test
    fun `effect of chaning ws`(){
        val newWsMs:Ms<Worksheet> = WorksheetImp(nameMs = ms("NewWorksheet"),wbKeySt = ms(TestSample.wbk1)).toMs()

        val wsState2 = wsState.setWsMs(newWsMs)

        assertEquals(newWsMs,wsState2.wsMs)
        assertEquals(newWsMs.value.nameMs,wsState2.id.wsNameMs)
        assertEquals(newWsMs.value.wbKeySt,wsState2.id.wbKeySt)
        assertEquals(newWsMs.value.wbKeySt.value, wsState2.cursorState.id.wbKey)

        assertEquals(newWsMs.value.nameMs.value,wsState2.rowRulerState.wsName)
        assertEquals(newWsMs.value.wbKeySt.value,wsState2.rowRulerState.wbKey)

        assertEquals(newWsMs.value.nameMs.value,wsState2.colRulerState.wsName)
        assertEquals(newWsMs.value.wbKeySt.value,wsState2.colRulerState.wbKey)

    }

}
