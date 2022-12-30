package com.qxdzbc.p6.app.document.range.copy_paste

import com.qxdzbc.common.error.CommonErrors
import com.qxdzbc.common.error.ErrorHeader
import com.qxdzbc.common.error.ErrorReport

object ClipboardReaderErrors {
    private const val prefix = "Clipboard reader error "

    val l get()= listOf(InvalidWbWs, ExceptionWhileTryingToReadData)

    fun coldInit() {
        val q = listOf(this) + l
        for (e in q) {
            println("cold init ${e::class}")
        }
    }

    object InvalidWbWs {
        val header = ErrorHeader("${prefix}${l.indexOf(InvalidWbWs)}", "invalid wb key, ws name")
        fun report(detail: String?=null): ErrorReport {
            return CommonErrors.makeCommonReport(header, detail)
        }
    }

    object ExceptionWhileTryingToReadData {
        val header =
            ErrorHeader("${prefix}${l.indexOf(ExceptionWhileTryingToReadData)}", "Encounter error while trying to read data from clipboard")
        fun report(exception: Exception,detail: String?=null): ErrorReport {
            return CommonErrors.makeCommonExceptionErrorReport(
                header, detail, exception
            )
        }
    }
}
