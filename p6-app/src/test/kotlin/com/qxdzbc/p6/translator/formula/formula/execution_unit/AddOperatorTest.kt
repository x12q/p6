package com.qxdzbc.p6.translator.formula.formula.execution_unit

import com.github.michaelbull.result.Ok
import com.qxdzbc.p6.app.document.cell.CellContentImp
import com.qxdzbc.p6.app.document.cell.IndCellImp
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.translator.formula.execution_unit.*
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.*
class AddOperatorTest {

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
            IntNum(1), DoubleNum(2.0)
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
        val mockGetCellUnit = mock<GetCell>().apply{
            whenever(this.runRs()) doReturn  Ok(
                IndCellImp(
                address = CellAddress("Q2"),
                content =  CellContentImp()
            )
            )
        }
        val u = AddOperator(
            IntNum(123),mockGetCellUnit
        )
        assertEquals(Ok(123.0), u.runRs())

        val u2 = AddOperator(
            mockGetCellUnit,IntNum(222)
        )
        assertEquals(Ok(222.0),u2.runRs())
        assertEquals(222.0,u2.run())
    }

}

