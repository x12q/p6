package com.qxdzbc.p6.app.action.worksheet.load_data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.github.michaelbull.result.Ok
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.d.CellContentImp
import com.qxdzbc.p6.app.document.cell.d.CellValue
import com.qxdzbc.p6.app.document.cell.d.IndCellImp
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.rpc.cell.msg.CellContentDM
import com.qxdzbc.p6.rpc.common_data_structure.IndCellDM
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
        act = ts.p6Comp.loadDataAction()
        wbk = ts.wbKey1
        wsn = ts.wsn1
        indWs = IndWorksheet(
            id = WorksheetIdDM(wbk, wsn),
            cells = listOf(
                IndCellDM(CellAddress("A1"), CellContentDM(CellValue.from(11))),
                IndCellDM(CellAddress("A2"), CellContentDM(CellValue.from(12))),
                IndCellDM(CellAddress("B3"), CellContentDM(CellValue.from(23))),
            )
        )
    }

    @Test
    fun `loadData overwrite`() {
        val req = LoadDataRequest(
            loadType = LoadType.OVERWRITE,
            ws = indWs
        )
        val wsMs = ts.stateCont.getWsMs(wbk, wsn)
        assertNotNull(wsMs)
        var ws by wsMs
        ws = ws.removeAllCell()
        ws.addOrOverwrite(
            IndCellImp(
                address = CellAddress("A2"),
                content = CellContentImp(
                    cellValueMs = ms(CellValue.from("overwrite me"))
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
        val wsMs = ts.stateCont.getWsMs(wbk, wsn)
        assertNotNull(wsMs)
        var ws by wsMs
        val a2Value = "Keep me"
        ws = ws.removeAllCell()
        ws = ws.addOrOverwrite(
            IndCellImp(
                address = CellAddress("A2"),
                content = CellContentImp(
                    cellValueMs = ms(CellValue.from(a2Value))
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
