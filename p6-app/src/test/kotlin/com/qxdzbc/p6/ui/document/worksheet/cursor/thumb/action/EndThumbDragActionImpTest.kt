package com.qxdzbc.p6.ui.document.worksheet.cursor.thumb.action

import com.qxdzbc.p6.app.action.cell.cell_update.CellUpdateRequestDM
import com.qxdzbc.p6.app.action.cell.cell_update.UpdateCellAction
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.rpc.cell.msg.CellContentDM
import com.qxdzbc.p6.rpc.cell.msg.CellIdDM
import test.BaseTest
import kotlin.test.*

internal class EndThumbDragActionImpTest : BaseTest(){
    lateinit var act:EndThumbDragAction
    lateinit var updateCellAct:UpdateCellAction
    @BeforeTest
    override fun b(){
        super.b()
        act = ts.p6Comp.endThumbDragAction()
        updateCellAct = ts.p6Comp.updateCellAction()
    }


    @Test
    fun `invokeSuitableAction generate number sequence`() {
        val c10 = CellAddress("C10")
        val c10Value = 3.0
        val c10Id = CellIdDM(
            address = c10,
            wbKey = ts.wbKey1,
            wsName = ts.wsn1
        )
        updateCellAct.updateCellDM(
            request = CellUpdateRequestDM(
                cellId = c10Id,
                cellContent = CellContentDM.fromAny(c10Value)
            )
        )
        assertEquals(c10Value,ts.sc.getCell(c10Id)!!.currentValue)
        act.invokeSuitableAction(
            wbws = ts.sc.getWbWsSt(ts.wbKey1,ts.wsn1)!!,
            startCell = c10,
            endCell =  CellAddress("C19"),
            isCtrPressed = true
        )
        for(x in 0 .. 9){
            val c = CellAddress(c10.colIndex,c10.rowIndex+x)
            assertEquals(c10Value+x, ts.sc.getCell(CellIdDM(
                address = c,
                wbKey = ts.wbKey1,
                wsName = ts.wsn1,
            ))!!.currentValue, c.toLabel())
        }
    }
}
