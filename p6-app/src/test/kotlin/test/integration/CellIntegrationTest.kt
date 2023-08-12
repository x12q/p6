package test.integration

import com.qxdzbc.p6.composite_actions.cell.cell_update.CellUpdateRequestDM
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.rpc.cell.msg.CellContentDM
import com.qxdzbc.p6.rpc.cell.msg.CellIdDM
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
        val stateCont = ts.sc
        val cellViewAction = ts.comp.cellViewAction()
        cellViewAction.updateCellDM(
            CellUpdateRequestDM(
                CellIdDM(CellAddress("A1"), ts.wbKey1, ts.wsn1),
                cellContent = CellContentDM.fromFormula("=B1")
            )
        )
//        ColdInit()
        cellViewAction.updateCellDM(
            CellUpdateRequestDM(
                CellIdDM(CellAddress("B1"),ts.wbKey1, ts.wsn1,),
                CellContentDM.fromFormula("=A1")
            )
        )

        val a1 = stateCont.getCellOrDefault(ts.wbKey1, ts.wsn1, CellAddress("A1"))
        val b1 = stateCont.getCellOrDefault(ts.wbKey1, ts.wsn1, CellAddress("B1"))
        println(a1?.address)
        println(b1?.address)
    }
}
