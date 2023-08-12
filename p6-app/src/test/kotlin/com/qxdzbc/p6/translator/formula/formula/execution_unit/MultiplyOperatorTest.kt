package com.qxdzbc.p6.translator.formula.formula.execution_unit

import com.github.michaelbull.result.Ok
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.document_data_layer.range.address.RangeAddress
import com.qxdzbc.p6.translator.formula.execution_unit.obj_type_in_app.CellAddressUnit
import com.qxdzbc.p6.translator.formula.execution_unit.primitive.IntUnit.Companion.toExUnit
import com.qxdzbc.p6.translator.formula.execution_unit.operator.MinusOperator
import com.qxdzbc.p6.translator.formula.execution_unit.operator.MultiplyOperator
import kotlin.test.*

class MultiplyOperatorTest:OperatorBaseTest(){

    @Test
    fun `cell multiply cell`(){
        val u = MultiplyOperator(
            ots.makeMockCellUnit(2),ots.makeMockCellUnit(3)
        )
        assertEquals(Ok(6.0),u.runRs())
    }

    @Test
    fun `num multiply cell`(){
        val u = MultiplyOperator(
            2.toExUnit(),ots.makeMockCellUnit(3)
        )
        assertEquals(Ok(6.0),u.runRs())
    }

    @Test
    fun `num multiply num`(){
        val u = MultiplyOperator(
            2.toExUnit(),3.toExUnit()
        )
        assertEquals(Ok(6.0),u.runRs())
    }
    @Test
    fun getRange(){
        val u = MinusOperator(
            CellAddressUnit(CellAddress("C3")),
            CellAddressUnit(CellAddress("K3"))
        )
        assertEquals(
            listOf(RangeAddress(CellAddress("C3")), RangeAddress(CellAddress("K3"))),
            u.getRanges()
        )
    }
}
