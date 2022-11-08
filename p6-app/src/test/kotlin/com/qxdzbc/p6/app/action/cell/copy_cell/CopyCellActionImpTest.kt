package com.qxdzbc.p6.app.action.cell.copy_cell

import com.qxdzbc.p6.app.action.cell.cell_update.CellUpdateRequestDM
import com.qxdzbc.p6.app.action.cell.cell_update.UpdateCellAction
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.rpc.cell.msg.CellContentDM
import com.qxdzbc.p6.rpc.cell.msg.CellIdDM
import com.qxdzbc.p6.rpc.cell.msg.CopyCellRequest
import test.BaseTest
import kotlin.test.*

internal class CopyCellActionImpTest: BaseTest(){
    lateinit var toCell: CellIdDM
    lateinit var act:CopyCellAction
    lateinit var updateCellAct:UpdateCellAction
    lateinit var fromCell:CellIdDM
    @BeforeTest
    override fun b() {
        super.b()
        act = ts.p6Comp.copyCellAction()
        updateCellAct = ts.p6Comp.updateCellAction()
        fromCell = CellIdDM(
            address = CellAddress("A1"),
            wbKey = ts.wbKey1,
            wsName = ts.wsn1
        )
        toCell = CellIdDM(
            address = CellAddress("A2"),
            wbKey = ts.wbKey1,
            wsName = ts.wsn1
        )
    }

    @Test
    fun copyFormula(){
        // write a value to C1
        updateCellAct.updateCellDM(
            request = CellUpdateRequestDM(
                cellId = CellIdDM(
                    address = CellAddress("C1"),
                    wbKey = ts.wbKey1,
                    wsName = ts.wsn1
                ),
                cellContent = CellContentDM.fromAny(1)
            )
        )
        // write a value to D1
        updateCellAct.updateCellDM(
            request = CellUpdateRequestDM(
                cellId = CellIdDM(
                    address = CellAddress("C2"),
                    wbKey = ts.wbKey1,
                    wsName = ts.wsn1
                ),
                cellContent = CellContentDM.fromAny(2)
            )
        )
        // write A1: =C1+1
        updateCellAct.updateCellDM(
            request = CellUpdateRequestDM(
                cellId = fromCell,
                cellContent = CellContentDM.fromFormula("=C1+1")
            )
        )
        // copy A1 to A2, expect A2 to have formula =C2+1 == 3
        assertNull(ts.sc.getCell(toCell)?.currentValue)
        act.copyCell(
            CopyCellRequest(
                fromCell = fromCell,
                toCell = toCell,
                shiftRange = true
            )
        )
        assertTrue(ts.sc.getCell(toCell)?.isFormula?:false)
        assertEquals("=C2 + 1", ts.sc.getCell(toCell)?.shortFormula)
        assertEquals(3.0,ts.sc.getCell(toCell)?.currentValue)
        assertEquals("3",ts.sc.getCell(toCell)?.attemptToAccessDisplayText())
        assertEquals(3.0,ts.sc.getCell(toCell)?.valueAfterRun)

    }

    @Test
    fun copyValue(){
        val content = "abc"
        updateCellAct.updateCellDM(
            request = CellUpdateRequestDM(
                cellId = fromCell,
                cellContent = CellContentDM.fromAny(content)
            )
        )
        assertNull(ts.sc.getCell(toCell)?.currentValue)
        act.copyCell(
            CopyCellRequest(
                fromCell = fromCell,
                toCell = toCell,
            )
        )
        assertEquals(content,ts.sc.getCell(toCell)?.currentValue)
    }
}
