package com.qxdzbc.p6.translator.formula.formula.execution_unit

import com.github.michaelbull.result.Ok
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.p6.app.document.cell.CellContentImp
import com.qxdzbc.p6.app.document.cell.CellValue
import com.qxdzbc.p6.app.document.cell.IndCellImp
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.translator.formula.execution_unit.DoubleUnit
import com.qxdzbc.p6.translator.formula.execution_unit.GetCell
import com.qxdzbc.p6.translator.formula.execution_unit.IntUnit
import com.qxdzbc.p6.translator.formula.execution_unit.StrUnit
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.BeforeTest

abstract class OperatorBaseTest {
    lateinit var ots: OperatorTestSample
    lateinit var getBlankCellUnit: GetCell
    lateinit var strUnit: StrUnit
    lateinit var intUnit: IntUnit
    lateinit var doubleUnit: DoubleUnit

    @BeforeTest
    fun b() {
        ots = OperatorTestSample()
        getBlankCellUnit = ots.getBlankCellUnit
        strUnit = ots.strUnit
        intUnit = ots.intUnit
        doubleUnit = ots.doubleUnit
    }
}

