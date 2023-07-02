package com.qxdzbc.p6.app.oddity

import com.qxdzbc.common.WithSize
import com.qxdzbc.common.error.ErrorHeader
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.error.ErrorReport

interface ErrorContainer : WithSize {
    val errList: List<ErrMsg>
    fun add(msg: ErrMsg): ErrorContainer
    fun addErrorReport(errorReport: ErrorReport?):ErrorContainer
    fun addFatalErrorReport(errorReport: ErrorReport?):ErrorContainer
    fun remove(errMsg: ErrMsg): ErrorContainer
    fun containErrorReportOfType(errorHeader: ErrorHeader):Boolean
    fun containErrorReportOfType(errorReport: ErrorReport):Boolean
    fun containErrorReport(errorReport: ErrorReport):Boolean
}