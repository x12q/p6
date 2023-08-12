package com.qxdzbc.p6.ui.worksheet.cursor.thumb.action

import com.qxdzbc.p6.composite_actions.cell.cell_update.CellUpdateRequest
import com.qxdzbc.p6.composite_actions.cell.cell_update.UpdateCellAction
import com.qxdzbc.p6.composite_actions.cell.multi_cell_update.UpdateMultiCellAction
import com.qxdzbc.p6.composite_actions.cell.multi_cell_update.UpdateMultiCellRequest
import com.qxdzbc.p6.document_data_layer.cell.CellId
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.rpc.cell.msg.CellContentDM
import com.qxdzbc.p6.rpc.cell.msg.CellIdDM
import com.qxdzbc.p6.rpc.common_data_structure.IndependentCellDM
import com.qxdzbc.p6.composite_actions.cursor.thumb.drag_thumb_action.EndThumbDragAction
import test.BaseAppStateTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class EndThumbDragActionImpTest : BaseAppStateTest() {
    lateinit var act: EndThumbDragAction
    lateinit var updateCellAct: UpdateCellAction
    lateinit var multiCellUpdateAct:UpdateMultiCellAction
    lateinit var startCell: CellAddress
    var startValue: Double = 0.0
    lateinit var startCellId: CellId

    @BeforeTest
    override fun _b() {
        super._b()
        act = ts.comp.endThumbDragAction()
        updateCellAct = ts.comp.updateCellAction()
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
                cellContentDM = CellContentDM.fromAny(startValue)
            )
        )
        multiCellUpdateAct=ts.comp.multiCellUpdateAction()
    }

    @Test
    fun `drag to copy formula `(){
        val wbkSt= ts.wbKey1Ms
        val wsnSt=ts.sc.getWsNameSt(ts.wbKey1Ms,ts.wsn1)!!
        multiCellUpdateAct.updateMultiCell(
            UpdateMultiCellRequest(
                wbKeySt = wbkSt,
                wsNameSt = wsnSt,
                cellUpdateList = listOf(
                    IndependentCellDM(CellAddress("A1"), CellContentDM.fromAny(1)),
                    IndependentCellDM(CellAddress("A2"), CellContentDM.fromAny(2)),
                    IndependentCellDM(CellAddress("B1"), CellContentDM.fromFormula("=A1+1")),
                )
            )
        )

        assertEquals(2.0,ts.sc.getCellOrDefault(wbkSt, wsnSt, CellAddress("B1"))!!.currentValue)

        // drag from B1->B2
        val endCell = CellAddress("B2")
        act.invokeSuitableAction(
            wbws = ts.sc.getWbWsSt(ts.wbKey1, ts.wsn1)!!,
            startCell = CellAddress("B1"),
            endCell = endCell,
            isCtrPressed = false
        )
        assertEquals(2.0,ts.sc.getCellOrDefault(wbkSt, wsnSt, CellAddress("B1"))!!.currentValue)
        assertEquals("2",ts.sc.getCellOrDefault(wbkSt, wsnSt, CellAddress("B1"))!!.attemptToAccessDisplayText())
        assertEquals(3.0,ts.sc.getCellOrDefault(wbkSt, wsnSt, CellAddress("B2"))!!.currentValue)
        assertEquals("3",ts.sc.getCellOrDefault(wbkSt, wsnSt, CellAddress("B2"))!!.attemptToAccessDisplayText())
    }


    @Test
    fun `drag down to copy`(){
        testDragCopy(4,0,1)
    }

    @Test
    fun `drag up to copy`(){
        testDragCopy(4,0,-1)
    }

    @Test
    fun `drag left to copy`(){
        testDragCopy(4,-1,0)
    }
    @Test
    fun `drag right to copy`(){
        testDragCopy(4,1,0)
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
        assertEquals(startValue, ts.sc.getCellOrDefault(startCellId)!!.currentValue)
        act.invokeSuitableAction(
            wbws = ts.sc.getWbWsSt(ts.wbKey1, ts.wsn1)!!,
            startCell = startCell,
            endCell = endCell,
            isCtrPressed = true
        )
        for (x in 0..count) {
            val c = CellAddress(startCell.colIndex+x*colSide, startCell.rowIndex +x*rowSide )
            assertEquals(
                startValue + x*valueSide, ts.sc.getCellOrDefault(
                    CellIdDM(
                        address = c,
                        wbKey = ts.wbKey1,
                        wsName = ts.wsn1,
                    )
                )!!.currentValue, c.label
            )
        }
    }
    /**
     * @param count number of cells in the accounted range
     * @param colSide "1" for moving to the right, "-1" for moving to the left
     * @param rowSide "1" for moving down, "-1" for moving up
     */
    fun testDragCopy(count:Int, colSide:Int, rowSide:Int) {
        val endCell = CellAddress(
            col = startCell.colIndex + count*colSide,
            row = startCell.rowIndex + count*rowSide
        )
        assertEquals(startValue, ts.sc.getCellOrDefault(startCellId)!!.currentValue)
        act.invokeSuitableAction(
            wbws = ts.sc.getWbWsSt(ts.wbKey1, ts.wsn1)!!,
            startCell = startCell,
            endCell = endCell,
            isCtrPressed = false
        )
        for (x in 0..count) {
            val c = CellAddress(startCell.colIndex+x*colSide, startCell.rowIndex +x*rowSide )
            assertEquals(
                startValue, ts.sc.getCellOrDefault(
                    CellIdDM(
                        address = c,
                        wbKey = ts.wbKey1,
                        wsName = ts.wsn1,
                    )
                )!!.currentValue, c.label
            )
        }
    }

}
