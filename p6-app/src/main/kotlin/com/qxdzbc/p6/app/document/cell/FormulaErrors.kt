package com.qxdzbc.p6.app.document.cell

import com.qxdzbc.common.error.ErrorHeader
import com.qxdzbc.common.error.ErrorReport

/**
 * This object contains ErrorReport that are only used for reporting errors to the end user encountered when running formulas in cells. This also contains some utility function for formating information in ErrorReports
 */
object FormulaErrors {
    val prefix = "ERR"

    object DivByZeroErr {
        val header = ErrorHeader(errorCode = "${prefix}2", "divide by zero")
        fun report(detail: String? = null): ErrorReport {
            return (detail?.let { header.setDescription(it) } ?: header).toErrorReport()
        }
    }

    object InvalidFunctionArgument {
        val header = ErrorHeader(errorCode = "${prefix}1", "invalid function argument")
        fun report(detail: String): ErrorReport {
            return header.setDescription(detail).toErrorReport()
        }
    }

    object Unknown {
        val header = ErrorHeader(errorCode = "${prefix}0", "unknow error")
        fun report(detail: String): ErrorReport {
            return header.setDescription(detail).toErrorReport()
        }
    }

    /**
     * Extract a representative string to show inside a cell on the UI
     */
    fun getCellRepStr(err: ErrorReport): String {
        return err.header.errorCode
    }

    /**
     * Extract error detail to be showed somewhere on the UI (tooltips, status bar, or similar places)
     */
    fun getDetailStr(err: ErrorReport): String {
        return err.header.errorDescription
    }
}
