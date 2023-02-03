package com.qxdzbc.p6.app.oddity

import com.qxdzbc.common.error.CommonErrors
import com.qxdzbc.common.error.SingleErrorReport
import com.qxdzbc.common.error.ErrorReport

sealed interface ErrMsg {
    val msg: String
    val type: ErrorType
    val errorReport: ErrorReport

    /**
     * Use with care
     */
    data class StrError(override val msg: String) : ErrMsg {
        override val type: ErrorType = ErrorType.ERROR
        override val errorReport: SingleErrorReport =
            SingleErrorReport(
                header = CommonErrors.Unknown.header.copy(errorDescription = msg),
            )
    }

    data class StrWarning(override val msg: String) : ErrMsg {
        override val type: ErrorType = ErrorType.WARNING
        override val errorReport: SingleErrorReport =
            SingleErrorReport(
                header = CommonErrors.Unknown.header.copy(errorDescription = msg),
            )
    }

    data class StrFatalError(override val msg: String) : ErrMsg {
        override val type: ErrorType = ErrorType.FATAL
        override val errorReport: SingleErrorReport =
            SingleErrorReport(
                header = CommonErrors.Unknown.header.copy(errorDescription = msg),
            )
    }

    data class Error(override val errorReport: ErrorReport) : ErrMsg {
        override val msg: String = errorReport.header.toString()
        override val type: ErrorType = ErrorType.ERROR
    }

    data class Warning(override val errorReport: SingleErrorReport) : ErrMsg {
        override val msg: String = errorReport.header.toString()
        override val type: ErrorType = ErrorType.WARNING
    }

    data class FatalError(override val errorReport: ErrorReport) : ErrMsg {
        override val msg: String = errorReport.header.toString()
        override val type: ErrorType = ErrorType.FATAL
    }
}

enum class ErrorType {
    ERROR, WARNING, FATAL
}

