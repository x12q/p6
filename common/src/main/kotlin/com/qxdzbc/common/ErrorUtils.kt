package com.qxdzbc.common
import com.github.michaelbull.result.Result
import com.qxdzbc.common.error.CommonErrors
import com.qxdzbc.common.error.ErrorReport
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.get

object ErrorUtils {

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
