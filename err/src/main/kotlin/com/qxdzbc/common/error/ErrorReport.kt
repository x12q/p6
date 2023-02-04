package com.qxdzbc.common.error

import com.github.michaelbull.result.Err

sealed interface ErrorReport {
    val header: ErrorHeader
    val data: Any?
    val exception: Throwable?
    fun toErr(): Err<ErrorReport>
    /**
     * Convert this into an exception.
     */
    fun toException(): Throwable

    /**
     * @return true if this report's header is the same as [errorHeader], false otherwise
     */
    fun isType(errorHeader: ErrorHeader): Boolean
    /**
     * @return true if this report is reporting the same error (same error code) as another error header, false otherwise
     */
    fun isType(errorReport: ErrorReport): Boolean
    /**
     * @return stack trace (as a string) of the call stack that lead to this ErrorReport
     */
    fun stackTraceStr(): String

    /**
     * @return true if this error report is identical in every way to another report
     */
    fun identicalTo(another: ErrorReport): Boolean

    /**
     * Merge this [ErrorReport] with [another] [ErrorReport]
     */
    operator fun plus(another: ErrorReport): ErrorReport
}
