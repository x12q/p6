package com.qxdzbc.p6.app.common.utils

import com.qxdzbc.p6.app.common.err.ErrorReportWithNavInfo
import com.github.michaelbull.result.Result

typealias RseNav<T> = Result<T, ErrorReportWithNavInfo>
