package com.qxdzbc.p6.translator.formula.formula.execution_unit

import com.github.michaelbull.result.Err
import com.qxdzbc.common.ResultUtils.toOk
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.document_data_layer.range.address.RangeAddress
import com.qxdzbc.p6.translator.formula.execution_unit.obj_type_in_app.CellAddressUnit
import com.qxdzbc.p6.translator.formula.execution_unit.primitive.DoubleUnit.Companion.toExUnit
import com.qxdzbc.p6.translator.formula.execution_unit.primitive.IntUnit.Companion.toExUnit
import com.qxdzbc.p6.translator.formula.execution_unit.operator.PowerByUnit
import com.qxdzbc.p6.translator.formula.execution_unit.primitive.StrUnit.Companion.toExUnit
import test.eq
import kotlin.test.*

class PowerByTest:OperatorBaseTest(){
    @Test
    fun `num^str`(){
        val u = PowerByUnit(
            2.0.toExUnit(),"qwe".toExUnit()
        )
        assertTrue(u.runRs() is Err)
    }


    @Test
    fun `cell^num`(){
        val u = PowerByUnit(
            this.ots.makeMockCellUnit(2),3.0.toExUnit()
        )
        eq(8.0.toOk(),u.runRs())
    }

    @Test
    fun `cell^cell`(){
        val u = PowerByUnit(
            this.ots.makeMockCellUnit(2),this.ots.makeMockCellUnit(3)
        )
        eq(8.0.toOk(),u.runRs())
    }

    @Test
    fun `num^cell`(){
        val u = PowerByUnit(
            2.toExUnit(),this.ots.makeMockCellUnit(3)
        )
        eq(8.0.toOk(),u.runRs())
    }


    @Test
    fun `num^num`(){
        val u = PowerByUnit(
            2.toExUnit(),3.toExUnit()
        )
        eq(8.0.toOk(),u.runRs())
    }

    @Test
    fun getRange(){
        val u = PowerByUnit(
            CellAddressUnit(CellAddress("C3")),
            CellAddressUnit(CellAddress("K3"))
        )
        assertEquals(
            listOf(RangeAddress(CellAddress("C3")), RangeAddress(CellAddress("K3"))),
            u.getRanges()
        )
    }
}

