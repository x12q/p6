package com.qxdzbc.p6.app.oddity

import com.qxdzbc.p6.app.common.utils.WithSize
import com.qxdzbc.p6.common.exception.error.ErrorHeader
import com.qxdzbc.p6.common.exception.error.ErrorReport
import com.qxdzbc.p6.ui.common.compose.Ms

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
