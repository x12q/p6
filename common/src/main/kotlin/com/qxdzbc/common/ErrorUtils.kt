package com.qxdzbc.common
import com.github.michaelbull.result.Result
import com.qxdzbc.common.error.CommonErrors
import com.qxdzbc.common.error.SingleErrorReport
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.get
import com.qxdzbc.common.error.ErrorReport

object ErrorUtils {

    /**
     * return the wrapped value if a [Result] is ok, otherwise throw the [SingleErrorReport] as an exception
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
