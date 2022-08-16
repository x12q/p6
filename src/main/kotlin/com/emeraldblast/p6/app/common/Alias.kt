package com.emeraldblast.p6.app.common

import com.emeraldblast.p6.app.common.err.ErrorReportWithNavInfo
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.github.michaelbull.result.Result

typealias Rs<T,E> = Result<T,E>
typealias Rse<T> = Result<T,ErrorReport>
typealias RseNav<T> = Result<T,ErrorReportWithNavInfo>
