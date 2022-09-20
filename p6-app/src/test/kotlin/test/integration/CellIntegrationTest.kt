package test.integration
import androidx.compose.runtime.getValue
import com.qxdzbc.p6.app.action.cell.cell_update.CellUpdateRequest
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import test.TestSample
import kotlin.test.*
class CellIntegrationTest {
    lateinit var ts:TestSample

    @BeforeTest
    fun b(){
        ts = TestSample()
    }

    @Test
    fun `circular reference`(){
            val stateCont by ts.p6Comp.stateContMs()
            val cellViewAction = ts.p6Comp.cellViewAction()
            cellViewAction.updateCell(
                CellUpdateRequest(
                    ts.wbKey1,ts.wsn1, CellAddress("A1"),
                    formula = "=B1"
                )
            )
            cellViewAction.updateCell(
                CellUpdateRequest(
                    ts.wbKey1,ts.wsn1, CellAddress("B1"),
                    formula = "=A1"
                )
            )

            val a1 = stateCont.getCell(ts.wbKey1,ts.wsn1, CellAddress("A1"))
            val b1 = stateCont.getCell(ts.wbKey1,ts.wsn1, CellAddress("B1"))
            println(a1?.address)
            println(b1?.address)
    }
}
