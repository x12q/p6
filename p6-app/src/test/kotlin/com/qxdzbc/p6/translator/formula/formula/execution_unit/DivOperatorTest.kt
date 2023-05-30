package com.qxdzbc.p6.translator.formula.formula.execution_unit

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.qxdzbc.common.ResultUtils.toOk
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.translator.formula.execution_unit.obj_type_in_app.CellAddressUnit
import com.qxdzbc.p6.translator.formula.execution_unit.operator.DivOperator
import com.qxdzbc.p6.translator.formula.execution_unit.primitive.IntUnit.Companion.toExUnit
import kotlin.test.*

class DivOperatorTest:OperatorBaseTest(){

    @Test
    fun `cell div cell`(){
        val i1 = ots.makeMockCellUnit(3)
        val i2 = ots.makeMockCellUnit(6)
        val u = DivOperator(i2,i1)
        assertEquals(Ok(2.0),u.runRs())
    }

    @Test
    fun `cell div num`(){
        val i1 = 3.toExUnit()
        val i2 = ots.makeMockCellUnit(6)
        val u = DivOperator(i2,i1)
        assertEquals(Ok(2.0),u.runRs())
    }

    @Test
    fun `num div cell`(){
        val i1 = 6.toExUnit()
        val i2 = ots.makeMockCellUnit(2)
        val u = DivOperator(i1,i2)
        assertEquals(Ok(3.0),u.runRs())
    }

    @Test
    fun `num div 0`(){
        val i1 = 6.toExUnit()
        val i2 = 0.toExUnit()
        val u = DivOperator(i1,i2)
        assertTrue(u.runRs() is Err)
    }


    @Test
    fun `num div num`(){
        val i1 = 6.toExUnit()
        val i2 = 2.toExUnit()
        val u = DivOperator(i1,i2)
        assertEquals(3.0.toOk(),u.runRs())
    }
    @Test
    fun getRange(){
        val u = DivOperator(
            CellAddressUnit(CellAddress("C3")),
            CellAddressUnit(CellAddress("K3"))
        )
        assertEquals(
            listOf(RangeAddress(CellAddress("C3")), RangeAddress(CellAddress("K3"))),
            u.getRanges()
        )
    }
}

