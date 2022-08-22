package com.qxdzbc.p6.app.common.utils

import com.qxdzbc.p6.app.common.err.ErrorReportWithNavInfo
import com.qxdzbc.p6.common.exception.error.ErrorReport
import com.github.michaelbull.result.Result

typealias Rs<T,E> = Result<T,E>
typealias Rse<T> = Result<T,ErrorReport>
typealias RseNav<T> = Result<T,ErrorReportWithNavInfo>
