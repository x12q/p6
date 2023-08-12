package com.qxdzbc.p6.translator.formula.formula.execution_unit

import com.github.michaelbull.result.Ok
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.document_data_layer.range.address.RangeAddress
import com.qxdzbc.p6.translator.formula.execution_unit.obj_type_in_app.CellAddressUnit
import com.qxdzbc.p6.translator.formula.execution_unit.operator.AddOperator
import com.qxdzbc.p6.translator.formula.execution_unit.primitive.DoubleUnit
import com.qxdzbc.p6.translator.formula.execution_unit.primitive.IntUnit
import com.qxdzbc.p6.translator.formula.execution_unit.primitive.StrUnit
import kotlin.test.*
class AddOperatorTest : OperatorBaseTest(){

    @Test
    fun getRange(){
        val u = AddOperator(
            CellAddressUnit(CellAddress("C3")),
            CellAddressUnit(CellAddress("K3"))
        )
        assertEquals(
            listOf(RangeAddress(CellAddress("C3")), RangeAddress(CellAddress("K3"))),
            u.getRanges()
        )
    }

    @Test
    fun `run on number`() {
        val u = AddOperator(
            IntUnit(1), DoubleUnit(2.0)
        )
        assertEquals(Ok(3.0), u.runRs())
    }

    @Test
    fun `run on string`() {
        val u = AddOperator(
            StrUnit("abc"), StrUnit("qwe")
        )
        assertEquals(Ok("abcqwe"), u.runRs())
    }

    @Test
    fun `number + blank cell`() {
        val u = AddOperator(
            IntUnit(123),getBlankCellUnit
        )
        assertEquals(Ok(123.0), u.runRs())

        val u2 = AddOperator(
            getBlankCellUnit, IntUnit(222)
        )
        assertEquals(Ok(222.0),u2.runRs())
    }

    @Test
    fun `blank cell + blank cell`() {
        val u = AddOperator(
            getBlankCellUnit,getBlankCellUnit
        )
        assertEquals(Ok(0.0), u.runRs())
    }

}

