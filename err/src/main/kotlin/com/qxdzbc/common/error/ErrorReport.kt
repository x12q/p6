package com.qxdzbc.common.error

import com.github.michaelbull.result.Err

sealed interface ErrorReport {
    val header: ErrorHeader
    val data: Any?
    val exception: Throwable?
    fun toErr(): Err<ErrorReport>
    fun toException(): Throwable
    fun isType(errorHeader: ErrorHeader): Boolean
    fun isType(errorReport: ErrorReport): Boolean
    fun stackTraceStr(): String
    fun identicalTo(another: ErrorReport): Boolean
    operator fun plus(another: ErrorReport): ErrorReport
}
