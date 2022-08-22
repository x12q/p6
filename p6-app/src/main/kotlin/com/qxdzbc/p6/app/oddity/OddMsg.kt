package com.qxdzbc.p6.app.oddity

import com.qxdzbc.p6.common.exception.error.CommonErrors
import com.qxdzbc.p6.common.exception.error.ErrorReport

sealed interface OddMsg {
    val msg: String
    val type: OddityType
    val errorReport: ErrorReport

    /**
     * Use with care
     */
    data class StrError(override val msg: String) : OddMsg {
        override val type: OddityType = OddityType.ERROR
        override val errorReport: ErrorReport =
            ErrorReport(
                header = CommonErrors.Unknown.header.copy(errorDescription = msg),
            )
    }

    data class StrWarning(override val msg: String) : OddMsg {
        override val type: OddityType = OddityType.WARNING
        override val errorReport: ErrorReport =
            ErrorReport(
                header = CommonErrors.Unknown.header.copy(errorDescription = msg),
            )
    }

    data class StrFatalError(override val msg: String) : OddMsg {
        override val type: OddityType = OddityType.FATAL
        override val errorReport: ErrorReport =
            ErrorReport(
                header = CommonErrors.Unknown.header.copy(errorDescription = msg),
            )
    }

    data class Error(override val errorReport: ErrorReport) : OddMsg {
        override val msg: String = errorReport.header.toString()
        override val type: OddityType = OddityType.ERROR
    }

    data class Warning(override val errorReport: ErrorReport) : OddMsg {
        override val msg: String = errorReport.header.toString()
        override val type: OddityType = OddityType.WARNING
    }

    data class FatalError(override val errorReport: ErrorReport) : OddMsg {
        override val msg: String = errorReport.header.toString()
        override val type: OddityType = OddityType.FATAL
    }
}

enum class OddityType {
    ERROR, WARNING, FATAL
}

