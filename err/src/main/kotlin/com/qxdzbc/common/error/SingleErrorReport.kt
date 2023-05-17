package com.qxdzbc.common.error

import com.github.michaelbull.result.Err

/**
 * container information about a single error.
 */
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
        return this.mergeWith(another)
    }

    override fun mergeWith(another: ErrorReport): ErrorReport {
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

    override fun isType(errorHeader: ErrorHeader): Boolean {
        return this.header.isType(errorHeader)
    }

    override fun isType(errorReport: ErrorReport): Boolean {
        return this.header.isType(errorReport.header)
    }

    override fun stackTraceStr(): String {
        val s = this.toException().stackTraceToString()
        return s
    }

    override fun identicalTo(another: ErrorReport): Boolean {
        val c1 = this.isType(another)
        return c1 && this.data == another.data && this.exception == another.exception
    }

    companion object{
        fun random():SingleErrorReport{
            return SingleErrorReport(header = ErrorHeader.random())
        }
    }
}
