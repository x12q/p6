package com.qxdzbc.p6.ui.document.worksheet.cursor.thumb.action

import com.qxdzbc.p6.app.action.cell.cell_update.CellUpdateRequest
import com.qxdzbc.p6.app.action.cell.cell_update.UpdateCellAction
import com.qxdzbc.p6.app.document.cell.CellId
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.rpc.cell.msg.CellContentDM
import com.qxdzbc.p6.rpc.cell.msg.CellIdDM
import test.BaseTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class EndThumbDragActionImpTest : BaseTest() {
    lateinit var act: EndThumbDragAction
    lateinit var updateCellAct: UpdateCellAction
    lateinit var startCell: CellAddress
    var startValue: Double = 0.0
    lateinit var startCellId: CellId

    @BeforeTest
    override fun b() {
        super.b()
        act = ts.p6Comp.endThumbDragAction()
        updateCellAct = ts.p6Comp.updateCellAction()
        startCell = CellAddress("K10")
        startValue = 3.0
        startCellId = CellId(
            address = startCell,
            wbKeySt = ts.wbKey1Ms,
            wsNameSt = ts.sc.getWsNameSt(ts.wbKey1, ts.wsn1)!!
        )
        updateCellAct.updateCell(
            request = CellUpdateRequest(
                cellId = startCellId,
                cellContent = CellContentDM.fromAny(startValue)
            )
        )
    }

    @Test
    fun `generate number sequence when drag down`() {
        testDrag(4,0,1,1)
    }

    @Test
    fun `generate number sequence when drag up`() {
        testDrag(4,0,-1,-1)
    }

    @Test
    fun `generate number sequence when drag to the left`() {
        testDrag(4,-1,0,-1)
    }

    @Test
    fun `generate number sequence when drag to the right`() {
        testDrag(4,1,0,1)
    }

    /**
     * @param count number of cells in the accounted range
     * @param colSide "1" for moving to the right, "-1" for moving to the left
     * @param rowSide "1" for moving down, "-1" for moving up
     * @param valueSide "1" to increase the value, "-1" to decrease the value
     */
    fun testDrag(count:Int, colSide:Int, rowSide:Int, valueSide:Int) {
        val endCell = CellAddress(
            col = startCell.colIndex + count*colSide,
            row = startCell.rowIndex + count*rowSide
        )
        assertEquals(startValue, ts.sc.getCell(startCellId)!!.currentValue)
        act.invokeSuitableAction(
            wbws = ts.sc.getWbWsSt(ts.wbKey1, ts.wsn1)!!,
            startCell = startCell,
            endCell = endCell,
            isCtrPressed = true
        )
        for (x in 0..count) {
            val c = CellAddress(startCell.colIndex+x*colSide, startCell.rowIndex +x*rowSide )
            assertEquals(
                startValue + x*valueSide, ts.sc.getCell(
                    CellIdDM(
                        address = c,
                        wbKey = ts.wbKey1,
                        wsName = ts.wsn1,
                    )
                )!!.currentValue, c.toLabel()
            )
        }
    }

}
