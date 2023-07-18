package test.integration

import com.qxdzbc.p6.app.action.cell.cell_update.CellUpdateRequestDM
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.rpc.cell.msg.CellContentDM
import com.qxdzbc.p6.rpc.cell.msg.CellIdDM
import com.qxdzbc.p6.app.action.cell.cell_update.UpdateCellAction
import test.TestSample
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class WorksheetIntegrationTest {
    lateinit var ts: TestSample
    val sc get() = ts.sc
    lateinit var updateCellAction: UpdateCellAction

    @BeforeTest
    fun b() {
        ts = TestSample()
        updateCellAction = ts.comp.updateCellAction()
    }


    @Test
    fun `formula correctness after changing ws name`() {
        // x: setup
        val ws1Ms = sc.getWs(ts.wbKey1, ts.wsn1)
        val ws2Ms = sc.getWs(ts.wbKey2, ts.wsn1)
        assertNotNull(ws1Ms)
        assertNotNull(ws2Ms)

        updateCellAction.updateCellDM(
            CellUpdateRequestDM(
                cellId = CellIdDM(
                    wbKey = ws1Ms.wbKey,
                    wsName = ws1Ms.name,
                    address = CellAddress("B23")
                ),
                cellContent = CellContentDM.fromAny(123)
            )
        )
        assertEquals(123.0, ws1Ms.getCell("B23")?.currentValue)
        updateCellAction.updateCellDM(
            CellUpdateRequestDM(
                cellId = CellIdDM(
                    wbKey = ws2Ms.wbKey,
                    wsName = ws2Ms.name,
                    address = CellAddress("A1")
                ),
                cellContent = CellContentDM.fromFormula("=B23@'${ws1Ms.name}'@'${ws1Ms.wbKey.name}' + 1")
            )
        )
        assertEquals(124.0, ws2Ms.getCell("A1")?.currentValue)

        // x: rename ws1
        val newName = "newName"
        ws1Ms.setWsName(newName)
        assertEquals(newName, ws1Ms.name)

        val c = ws2Ms.getCell("A1")
        assertEquals(124.0, c?.currentValue)
        println(c?.fullFormulaFromExUnit)
        assertEquals("=B23@'${ws1Ms.name}'@'${ws1Ms.wbKey.name}' + 1", c?.fullFormulaFromExUnit)


    }
}
