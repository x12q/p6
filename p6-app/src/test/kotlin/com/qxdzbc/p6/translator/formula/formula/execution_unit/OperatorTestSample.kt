package com.qxdzbc.p6.translator.formula.formula.execution_unit

import com.github.michaelbull.result.Ok
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.p6.app.document.cell.CellContentImp
import com.qxdzbc.p6.app.document.cell.CellValue
import com.qxdzbc.p6.app.document.cell.IndCellImp
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.translator.formula.execution_unit.DoubleUnit
import com.qxdzbc.p6.translator.formula.execution_unit.GetCellUnit
import com.qxdzbc.p6.translator.formula.execution_unit.IntUnit
import com.qxdzbc.p6.translator.formula.execution_unit.StrUnit
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class OperatorTestSample {
    val getBlankCellUnit = mock<GetCellUnit>().apply {
        whenever(this.runRs()) doReturn Ok(
            ms(IndCellImp(
                address = CellAddress("Q2"),
                content = CellContentImp()
            ))
        )
        whenever(this.run()) doReturn
                IndCellImp(
                    address = CellAddress("Q2"),
                    content = CellContentImp()
                )

    }

    fun makeMockCellUnit(i: Any?): GetCellUnit {
        val getIntCellUnit = mock<GetCellUnit>().apply {
            whenever(this.runRs()) doReturn Ok(
                ms(IndCellImp(
                    address = CellAddress("Q2"),
                    content = CellContentImp(
                        cellValueMs = CellValue.fromAny(i).toMs()
                    )
                ))
            )
            whenever(this.run()) doReturn
                    IndCellImp(
                        address = CellAddress("Q2"),
                        content = CellContentImp(
                            cellValueMs = CellValue.fromAny(i).toMs()
                        )
                    )
        }
        return getIntCellUnit
    }

    val getIntCellUnit = makeMockCellUnit(33)
    val strUnit = StrUnit("string unit value")
    val intUnit = IntUnit(123)
    val doubleUnit = DoubleUnit(123.0)
}
