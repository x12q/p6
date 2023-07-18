package com.qxdzbc.p6.app.action.worksheet.load_data

import com.github.michaelbull.result.Ok
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.CellContentImp
import com.qxdzbc.p6.app.document.cell.CellValue
import com.qxdzbc.p6.app.document.cell.IndCellImp
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.rpc.cell.msg.CellContentDM
import com.qxdzbc.p6.rpc.common_data_structure.IndependentCellDM
import com.qxdzbc.p6.rpc.common_data_structure.IndWorksheet
import com.qxdzbc.p6.rpc.worksheet.msg.LoadDataRequest
import com.qxdzbc.p6.rpc.worksheet.msg.LoadType
import com.qxdzbc.p6.rpc.worksheet.msg.WorksheetIdDM
import test.TestSample
import kotlin.test.*

internal class LoadDataActionImpTest {
    private lateinit var indWs: IndWorksheet
    lateinit var ts: TestSample
    lateinit var act: LoadDataAction
    lateinit var wbk: WorkbookKey
    lateinit var wsn: String

    @BeforeTest
    fun b() {
        ts = TestSample()
        act = ts.comp.loadDataAction()
        wbk = ts.wbKey1
        wsn = ts.wsn1
        indWs = IndWorksheet(
            id = WorksheetIdDM(wbk, wsn),
            cells = listOf(
                IndependentCellDM(CellAddress("A1"), CellContentDM(CellValue.from(11),originalText="11")),
                IndependentCellDM(CellAddress("A2"), CellContentDM(CellValue.from(12),originalText="12")),
                IndependentCellDM(CellAddress("B3"), CellContentDM(CellValue.from(23),originalText="23")),
            )
        )
    }

    @Test
    fun `loadData overwrite`() {
        val req = LoadDataRequest(
            loadType = LoadType.OVERWRITE,
            ws = indWs
        )
        val ws = ts.sc.getWs(wbk, wsn)
        assertNotNull(ws)
        ws.removeAllCell()
        ws.addOrOverwrite(
            IndCellImp(
                address = CellAddress("A2"),
                content = CellContentImp(
                    cellValueMs = ms(CellValue.from("overwrite me")),
                    originalText ="overwrite me"
                )
            )
        )

        // x: run action
        val rs = act.loadDataRs(req, false)

        // x: post condition
        assertTrue(rs is Ok)
        assertEquals(11.0, ws.getCell("A1")?.currentValue)
        assertEquals(12.0, ws.getCell("A2")?.currentValue)
        assertEquals(23.0, ws.getCell("B3")?.currentValue)
    }

    @Test
    fun `loadData not overwrite`() {
        val req = LoadDataRequest(
            loadType = LoadType.KEEP_OLD_DATA_IF_COLLIDE,
            ws = indWs
        )
        val ws = ts.sc.getWs(wbk, wsn)
        assertNotNull(ws)
        val a2Value = "Keep me"
        ws.removeAllCell()
        ws.addOrOverwrite(
            IndCellImp(
                address = CellAddress("A2"),
                content = CellContentImp(
                    cellValueMs = ms(CellValue.from(a2Value)),
                    originalText =a2Value
                )
            )
        )

        // x: precondition
        assertEquals("Keep me", ws.getCell("A2")?.currentValue)

        // x: run action
        val rs = act.loadDataRs(req, false)

        // x: post condition
        assertTrue(rs is Ok)
        assertTrue(ws.isNotEmpty())
        assertEquals(11.0, ws.getCell("A1")?.currentValue)
        assertEquals(a2Value, ws.getCell("A2")?.currentValue)
        assertEquals(23.0, ws.getCell("B3")?.currentValue)
    }
}
