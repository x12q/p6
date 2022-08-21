package com.emeraldblast.p6.app.common.utils

import com.emeraldblast.p6.app.common.err.ErrorReportWithNavInfo
import com.emeraldblast.p6.common.exception.error.CommonErrors
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.get

object ErrorUtils {

    fun <T> Result<T, ErrorReportWithNavInfo>.noNav(): Rse<T> {
        when(this){
            is Ok -> return this
            is Err -> return Err(this.error.errorReport)
        }
    }

    /**
     * return the wrapped value if a [Result] is ok, otherwise throw the [ErrorReport] as an exception
     */
    fun <V> Result<V, ErrorReport>.getOrThrow(): V {
        CommonErrors.ExceptionError
        if (this is Err) {
            throw this.error.toException()
        } else {
            return this.get()!!
        }
    }
}
