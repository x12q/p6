package com.qxdzbc.p6.translator.formula.formula.execution_unit

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.document.cell.Cell
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.translator.formula.execution_unit.obj_type_in_app.CellAddressUnit
import com.qxdzbc.p6.translator.formula.execution_unit.primitive.DoubleUnit
import com.qxdzbc.p6.translator.formula.execution_unit.primitive.IntUnit
import com.qxdzbc.p6.translator.formula.execution_unit.operator.MinusOperator
import kotlin.test.*

class MinusOperatorTest:OperatorBaseTest(){

    @Test
    fun `num-str`(){
        val u = MinusOperator(
            intUnit,strUnit
        )
        assertTrue(u.runRs() is Err)
    }

    @Test
    fun `str - num`(){
        val u = MinusOperator(
            strUnit,intUnit
        )
        assertTrue(u.runRs() is Err)
    }

    @Test
    fun `blank cell-blank cell`(){
        val u = MinusOperator(
            getBlankCellUnit,getBlankCellUnit
        )
        val e = 0.0
        assertEquals(Ok(e), u.runRs())
    }

    @Test
    fun `blank cell-num`(){
        val u = MinusOperator(
            getBlankCellUnit,intUnit,
        )
        val e = -intUnit.v.toDouble()
        assertEquals(Ok(e), u.runRs())
    }

    @Test
    fun `num - blank cell`(){
        val u = MinusOperator(
            intUnit, getBlankCellUnit
        )
        val e = intUnit.v.toDouble()
        assertEquals(Ok(e), u.runRs())
    }
    @Test
    fun `num cell - num`(){
        val u2 = MinusOperator(
            ots.getIntCellUnit,intUnit
        )
        val e = intUnit.v - ((ots.getIntCellUnit.runRs().component1() as St<Cell>).value.valueAfterRun as Double)
        assertEquals(Ok(-e),u2.runRs())
    }

    @Test
    fun `num - num cell`(){
        val u = MinusOperator(
            intUnit, ots.getIntCellUnit
        )
        val e = intUnit.v - ((ots.getIntCellUnit.run() as Cell).valueAfterRun as Double)
        assertEquals(Ok(e),u.runRs())
    }
    @Test
    fun `num - num`(){
        val u = MinusOperator(
            intUnit, IntUnit(33)
        )
        val e = intUnit.v - 33.0
        assertEquals(Ok(e),u.runRs())

        val u2 = MinusOperator(
            doubleUnit, DoubleUnit(22.0)
        )
        val e2 = doubleUnit.v - 22.0
        assertEquals(Ok(e2),u2.runRs())

        val u3 = MinusOperator(
            doubleUnit, IntUnit(11)
        )
        val e3 = doubleUnit.v - 11
        assertEquals(Ok(e3),u3.runRs())
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

