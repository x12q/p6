package test.integration

import androidx.compose.runtime.getValue
import com.qxdzbc.p6.app.action.cell.cell_update.CellUpdateRequest
import com.qxdzbc.p6.app.action.cell.cell_update.CellUpdateRequest2
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.rpc.cell.msg.CellContentProtoDM
import test.TestSample
import kotlin.test.BeforeTest
import kotlin.test.Test

class CellIntegrationTest {
    lateinit var ts: TestSample

    @BeforeTest
    fun b() {
        ts = TestSample()
    }

    @Test
    fun `circular reference`() {
        val stateCont by ts.p6Comp.stateContMs()
        val cellViewAction = ts.p6Comp.cellViewAction()
        cellViewAction.updateCell2(
            CellUpdateRequest2(
                ts.wbKey1, ts.wsn1, CellAddress("A1"),
                cellContent = CellContentProtoDM.fromFormula("=B1")
            )
        )
        cellViewAction.updateCell2(
            CellUpdateRequest2(
                ts.wbKey1, ts.wsn1, CellAddress("B1"),
                        CellContentProtoDM.fromFormula("=A1")
            )
        )

        val a1 = stateCont.getCell(ts.wbKey1, ts.wsn1, CellAddress("A1"))
        val b1 = stateCont.getCell(ts.wbKey1, ts.wsn1, CellAddress("B1"))
        println(a1?.address)
        println(b1?.address)
    }
}
