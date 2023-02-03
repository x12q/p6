package com.qxdzbc.common.error

import com.github.michaelbull.result.Err

class SingleErrorReport(
    override val header: ErrorHeader,
    override val data: Any? = null,
    override val exception: Throwable? = null,
) :Throwable(), ErrorReport {

    override fun toErr():Err<ErrorReport>{
        return Err(this)
    }

    /**
     * Convert this into an exception. If already hold an exception, return that exception
     */
    override fun toException(): Throwable {
        if (exception != null) {
            return exception
        }
        return this
    }

    override fun plus(another: ErrorReport): ErrorReport {
        when (another) {
            is MultiErrorReport -> {
                return another.copy(
                    singleErrorReportList = another.singleErrorReportList + this
                )
            }

            is SingleErrorReport -> {
                return CommonErrors.MultipleErrors.report(
                    listOf(this, another)
                )
            }
        }
    }

    override fun toString(): String {
        val rt = """
type: ${this.header}
${if (data != null) "data:${data}" else ""}
        """.trimIndent()
        return rt
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
     * produce stack trace as a string
     * @return stack trace as a string
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
}


