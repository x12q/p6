package com.qxdzbc.p6.app.common.err

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.qxdzbc.common.Rse

object ErrorReportWithNavInfos{
    /**
     * remove nav info from [ErrorReportWithNavInfo]
     */
    fun <T> Result<T, ErrorReportWithNavInfo>.noNav(): Rse<T> {
        when(this){
            is Ok -> return this
            is Err -> return Err(this.error.errorReport)
        }
    }

}
