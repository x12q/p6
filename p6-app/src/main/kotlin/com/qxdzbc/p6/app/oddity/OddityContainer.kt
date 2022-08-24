package com.qxdzbc.p6.app.oddity

import com.qxdzbc.common.WithSize
import com.qxdzbc.common.error.ErrorHeader
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.common.compose.Ms

interface OddityContainer : WithSize {
    val oddList: List<OddMsg>
    fun add(msg: OddMsg): OddityContainer
    fun addErrorReport(errorReport: ErrorReport?):OddityContainer
    fun addFatalErrorReport(errorReport: ErrorReport?):OddityContainer
    fun remove(oddMsg: OddMsg): OddityContainer
    fun containErrorReportOfType(errorHeader: ErrorHeader):Boolean
    fun containErrorReportOfType(errorReport: ErrorReport):Boolean
    fun containErrorReport(errorReport: ErrorReport):Boolean
}

fun Ms<OddityContainer>.addError(errorReport: ErrorReport?) {
    this.value = this.value.addErrorReport(errorReport)
}

fun Ms<OddityContainer>.remove(oddMsg:OddMsg){
    this.value = this.value.add(oddMsg)
}
