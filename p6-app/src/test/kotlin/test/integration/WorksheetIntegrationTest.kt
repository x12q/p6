package test.integration

import androidx.compose.runtime.getValue
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
    val sc get() = ts.stateContMs().value
    lateinit var updateCellAction: UpdateCellAction

    @BeforeTest
    fun b() {
        ts = TestSample()
        updateCellAction = ts.p6Comp.updateCellAction()
    }


    @Test
    fun `formula correctness after changing ws name`() {
        // x: setup
        val ws1Ms = sc.getWsMs(ts.wbKey1, ts.wsn1)
        val ws2Ms = sc.getWsMs(ts.wbKey2, ts.wsn1)
        assertNotNull(ws1Ms)
        assertNotNull(ws2Ms)
        val ws1 by ws1Ms
        val ws2 by ws2Ms

        updateCellAction.updateCellDM(
            CellUpdateRequestDM(
                cellId = CellIdDM(
                    wbKey = ws1.wbKey,
                    wsName = ws1.name,
                    address = CellAddress("B23")
                ),
                cellContent = CellContentDM.fromAny(123)
            )
        )
        assertEquals(123.0, ws1.getCell("B23")?.currentValue)
        updateCellAction.updateCellDM(
            CellUpdateRequestDM(
                cellId = CellIdDM(
                    wbKey = ws2.wbKey,
                    wsName = ws2.name,
                    address = CellAddress("A1")
                ),
                cellContent = CellContentDM.fromFormula("=B23@'${ws1.name}'@'${ws1.wbKey.name}' + 1")
            )
        )
        assertEquals(124.0, ws2.getCell("A1")?.currentValue)

        // x: rename ws1
        val newName = "newName"
        ws1Ms.value = ws1.setWsName(newName)
        assertEquals(newName, ws1.name)

        val c = ws2.getCell("A1")
        assertEquals(124.0, c?.currentValue)
        println(c?.fullFormula)
        assertEquals("=B23@'${ws1.name}'@'${ws1.wbKey.name}' + 1", c?.fullFormula)


    }
}
