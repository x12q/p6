package com.qxdzbc.p6.translator.formula.formula.execution_unit

import com.qxdzbc.p6.translator.formula.execution_unit.primitive.DoubleUnit
import com.qxdzbc.p6.translator.formula.execution_unit.function.GetCellUnit
import com.qxdzbc.p6.translator.formula.execution_unit.primitive.IntUnit
import com.qxdzbc.p6.translator.formula.execution_unit.primitive.StrUnit
import kotlin.test.BeforeTest

abstract class OperatorBaseTest {
    lateinit var ots: OperatorTestSample
    lateinit var getBlankCellUnit: GetCellUnit
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

