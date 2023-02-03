package com.qxdzbc.common.error

import com.github.michaelbull.result.Err


data class MultiErrorReport (
    override val header: ErrorHeader,
    val singleErrorReportList: List<ErrorReport>
) : Throwable(), ErrorReport {

    override val data: List<ErrorReport>
        get() = singleErrorReportList

    override val exception: Throwable
        get() = this

    override fun toString(): String {
        return singleErrorReportList.joinToString("|") { it.toString() }
    }

    override fun toErr(): Err<ErrorReport> {
        return Err(this)
    }

    override fun toException(): Throwable {
        return this
    }

    /**
     * @return true if this report is reporting the same error (same error code) as another error header, false otherwise
     */
    override fun isType(errorHeader: ErrorHeader): Boolean {
        return this.header.isType(errorHeader)
    }

    /**
     * @return true if this report is reporting the same error (same error code) as another report, false otherwise
     */
    override fun isType(errorReport: ErrorReport): Boolean {
        return this.header.isType(errorReport.header)
    }

    /**
     * return stack trace as a string
     */
    override fun stackTraceStr(): String {
        val s = this.toException().stackTraceToString()
        return s
    }

    /**
     * @return true if this error report is identical in every way to another report
     */
    override fun identicalTo(another: ErrorReport): Boolean {
        val c1 = this.isType(another)
        return c1 && this.data == another.data && this.exception == another.exception
    }

    override fun plus(another: ErrorReport): ErrorReport {
        when (another) {
            is MultiErrorReport -> {
                return another.copy(
                    singleErrorReportList = this.singleErrorReportList + another.singleErrorReportList
                )
            }

            is SingleErrorReport -> {
                return this.copy(
                    singleErrorReportList = singleErrorReportList + another
                )
            }
        }
    }
}
