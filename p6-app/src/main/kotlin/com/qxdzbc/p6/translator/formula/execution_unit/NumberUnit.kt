package com.qxdzbc.p6.translator.formula.execution_unit

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.qxdzbc.common.error.ErrorReport

sealed class NumberUnit(val v: Number) : ExUnit {
    override fun run(): Result<Number, ErrorReport> {
        return Ok(v)
    }
}
